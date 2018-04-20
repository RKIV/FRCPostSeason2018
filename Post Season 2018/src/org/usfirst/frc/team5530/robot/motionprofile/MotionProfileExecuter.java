/**
 * Example logic for firing and managing motion profiles.
 * This example sends MPs, waits for them to finish
 * Although this code uses a CANTalon, nowhere in this module do we changeMode() or call set() to change the output.
 * This is done in Robot.java to demonstrate how to change control modes on the fly.
 * 
 * The only routines we call on Talon are....
 * 
 * changeMotionControlFramePeriod
 * 
 * getMotionProfileStatus		
 * clearMotionProfileHasUnderrun     to get status and potentially clear the error flag.
 * 
 * pushMotionProfileTrajectory
 * clearMotionProfileTrajectories
 * processMotionProfileBuffer,   to push/clear, and process the trajectory points.
 * 
 * getControlMode, to check if we are in Motion Profile Control mode.
 * 
 * Example of advanced features not demonstrated here...
 * [1] Calling pushMotionProfileTrajectory() continuously while the Talon executes the motion profile, thereby keeping it going indefinitely.
 * [2] Instead of setting the sensor position to zero at the start of each MP, the program could offset the MP's position based on current position. 
 */

package org.usfirst.frc.team5530.robot.motionprofile;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;

public class MotionProfileExecuter {

	private MotionProfileStatus _status = new MotionProfileStatus();

	double _pos=0,_vel=0,_heading=0;

	double _endHeading = 0;
	
	private WPI_TalonSRX _motorController;
	
	private int _state = 0;

	private int _loopTimeout = -1;

	private boolean _bStart = false;
	
	private boolean _bForward = false;


	private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;

	private static final int kMinPointsInTalon = 5;
	
	private static final int kNumLoopsTimeout = 10;
	
	
	class PeriodicRunnable implements java.lang.Runnable {
	    public void run() {  _motorController.processMotionProfileBuffer();    }
	}
	Notifier _notifer = new Notifier(new PeriodicRunnable());
	

	/**
	 * C'tor
	 * 
	 * @param talon
	 *            reference to Talon object to fetch motion profile status from.
	 */
	public MotionProfileExecuter(WPI_TalonSRX motorController) {
		_motorController = motorController;
		_motorController.changeMotionControlFramePeriod(5);
		_notifer.startPeriodic(0.005);
	}

	/**
	 * Called to clear Motion profile buffer and reset state info during
	 * disabled and when Talon is not in MP control mode.
	 */
	public void reset() {
		_motorController.clearMotionProfileTrajectories();
		_setValue = SetValueMotionProfile.Disable;
		_state = 0;
		_loopTimeout = -1;
		_bStart = false;
	}
	
	boolean IsMotionProfile(ControlMode controlMode) {
		if (controlMode == ControlMode.MotionProfile)
			return true;
		if (controlMode == ControlMode.MotionProfileArc)
			return true;
		return false;
	}

	/**
	 * Called every loop.
	 */
	public void control() {
		_motorController.getMotionProfileStatus(_status);


		if (_loopTimeout < 0) {
		} else {
			if (_loopTimeout == 0) {
				Instrumentation.OnNoProgress();
			} else {
				--_loopTimeout;
			}
		}

		if (false == IsMotionProfile(_motorController.getControlMode())) {
			_state = 0;
			_loopTimeout = -1;
		} else {
			switch (_state) {
				case 0: 
					if (_bStart) {
						_bStart = false;
	
						_setValue = SetValueMotionProfile.Disable;
						startFilling();

						_state = 1;
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 1: 
					if (_status.btmBufferCnt > kMinPointsInTalon) {
						_setValue = SetValueMotionProfile.Enable;
						_state = 2;
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 2: 
					if (_status.isUnderrun == false) {
						_loopTimeout = kNumLoopsTimeout;
					}
					/*
					 * If we are executing an MP and the MP finished, start loading
					 * another. We will go into hold state so robot servo's
					 * position.
					 */
					if (_status.activePointValid && _status.isLast) {
						/*
						 * because we set the last point's isLast to true, we will
						 * get here when the MP is done
						 */
						_setValue = SetValueMotionProfile.Hold;
						_state = 0;
						_loopTimeout = -1;
					}
					break;
			}

			_motorController.getMotionProfileStatus(_status);
			_heading = _motorController.getActiveTrajectoryHeading();
			_pos = _motorController.getActiveTrajectoryPosition();
			_vel = _motorController.getActiveTrajectoryVelocity();

			Instrumentation.process(_status, _pos, _vel, _heading);
		}
	}
	/**
	 * Find enum value if supported.
	 * @param durationMs
	 * @return enum equivalent of durationMs
	 */
	private TrajectoryDuration GetTrajectoryDuration(int durationMs)
	{	 
		/* create return value */
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		/* convert duration to supported type */
		retval = retval.valueOf(durationMs);
		/* check that it is valid */
		if (retval.value != durationMs) {
			DriverStation.reportError("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead", false);		
		}
		/* pass to caller */
		return retval;
	}
	/** Start filling the MPs to all of the involved Talons. */
	private void startFilling() {
		/* since this example only has one talon, just update that one */
		startFilling(MotionProfile.Points, MotionProfile.kNumPoints);
	}

	private void startFilling(double[][] profile, int totalCnt) {

		/* create an empty point */
		TrajectoryPoint point = new TrajectoryPoint();

		/* did we get an underrun condition since last time we checked ? */
		if (_status.hasUnderrun) {
			/* better log it so we know about it */
			Instrumentation.OnUnderrun();
			/*
			 * clear the error. This flag does not auto clear, this way 
			 * we never miss logging it.
			 */
			_motorController.clearMotionProfileHasUnderrun(Constants.kTimeoutMs);
		}
		/*
		 * just in case we are interrupting another MP and there is still buffer
		 * points in memory, clear it.
		 */
		_motorController.clearMotionProfileTrajectories();

		/* set the base trajectory period to zero, use the individual trajectory period below */
		_motorController.configMotionProfileTrajectoryPeriod(Constants.kBaseTrajPeriodMs, Constants.kTimeoutMs);
		
		/* squirell away the final target distance, we will use this for heading generation */
		double finalPositionRot = profile[totalCnt-1][0];
		
		/* This is fast since it's just into our TOP buffer */
		for (int i = 0; i < totalCnt; ++i) {
			double direction = _bForward ? +1 : -1;
			double positionRot = profile[i][0];
			double velocityRPM = profile[i][1];
			double heading = _endHeading * positionRot / finalPositionRot; /* scale heading progress to position progress */

			/* for each point, fill our structure and pass it to API */
			point.position = direction * positionRot * Constants.kSensorUnitsPerRotation * 2; //Convert Revolutions to Units
			point.velocity = direction * velocityRPM * Constants.kSensorUnitsPerRotation / 600.0; //Convert RPM to Units/100ms
			point.headingDeg = heading; /* scaled such that 3600 => 360 deg */
			point.profileSlotSelect0 = Constants.kSlot_MotProf; /* which set of gains would you like to use [0,3]? */
			point.profileSlotSelect1 = Constants.kSlot_Turning; /* auxiliary PID [0,1], leave zero */
			point.timeDur = GetTrajectoryDuration((int)profile[i][2]);
			point.zeroPos = false;
			if (i == 0)
				point.zeroPos = true; /* set this to true on the first point */

			point.isLastPoint = false;
			if ((i + 1) == totalCnt)
				point.isLastPoint = true; /* set this to true on the last point  */

			_motorController.pushMotionProfileTrajectory(point);
		}
	}
	/**
	 * Called by application to signal Talon to start the buffered MP (when it's
	 * able to).
	 */
	void start(double endHeading, boolean bForward) {
		_bStart = true;
		_bForward = bForward;
		_endHeading = endHeading;
	}

	/**
	 * 
	 * @return the output value to pass to Talon's set() routine. 0 for disable
	 *         motion-profile output, 1 for enable motion-profile, 2 for hold
	 *         current motion profile trajectory point.
	 */
	SetValueMotionProfile getSetValue() {
		return _setValue;
	}
}