package org.usfirst.frc.team5530.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team5530.robot.subsystems.DrivetrainSS;

import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.Notifier;
import com.ctre.phoenix.motorcontrol.can.*;

public class MotionProfile{

    WPI_TalonSRX _talon;
	
    private MotionProfileStatus _status = new MotionProfileStatus();
    
    private int _state = 0;
    
    private int _loopTimeout = -1;
    
    private boolean _bStart = false;
    
    private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;
    
    private static final int MIN_POINTS_IN_TALON = 5;
    
    private static final int NUM_LOOPS_TIMEOUT = 10;
    
    class PeriodicRunnable implements java.lang.Runnable {
	    public void run() {  _talon.processMotionProfileBuffer();    }
	}
	Notifier _notifer = new Notifier(new PeriodicRunnable());
    
	public MotionProfile(WPI_TalonSRX talon) {
		_talon = talon;
		_talon.changeMotionControlFramePeriod(5);
		_notifer.startPeriodic(0.005);
	}
    
	public void reset() {
		_talon.clearMotionProfileTrajectories();
		_setValue = SetValueMotionProfile.Disable;
		_state = 0;
		_loopTimeout = -1;
		_bStart = false;
	}
    
	public void control() {
		/* Get the motion profile status every loop */
		_talon.getMotionProfileStatus(_status);

		//Check loop timeout
		if (_loopTimeout < 0) {
			/* do nothing, timeout is disabled */
		} else {
			/* our timeout is nonzero */
			if (_loopTimeout == 0) {
				/*
				 * something is wrong. Talon is not present, unplugged, breaker
				 * tripped
				 */
//				instrumentation.OnNoProgress();
			} else {
				--_loopTimeout;
			}
		}

		/* first check if we are in MP mode */
		if (_talon.getControlMode() != ControlMode.MotionProfile) {
			/*
			 * we are not in MP mode. We are probably driving the robot around
			 * using gamepads or some other mode.
			 */
			_state = 0;
			_loopTimeout = -1;
		} else {
			/*
			 * we are in MP control mode. That means: starting Mps, checking Mp
			 * progress, and possibly interrupting MPs if thats what you want to
			 * do.
			 */
			switch (_state) {
				case 0: /* wait for application to tell us to start an MP */
					if (_bStart) {
						_bStart = false;
	
						_setValue = SetValueMotionProfile.Disable;
						startFilling();
						/*
						 * MP is being sent to CAN bus, wait a small amount of time
						 */
						_state = 1;
						_loopTimeout = NUM_LOOPS_TIMEOUT;
					}
					break;
				case 1: /*
						 * wait for MP to stream to Talon, really just the first few
						 * points
						 */
					/* do we have a minimum numberof points in Talon */
					if (_status.btmBufferCnt > MIN_POINTS_IN_TALON) {
						/* start (once) the motion profile */
						_setValue = SetValueMotionProfile.Enable;
						/* MP will start once the control frame gets scheduled */
						_state = 2;
						_loopTimeout = NUM_LOOPS_TIMEOUT;
					}
					break;
				case 2: /* check the status of the MP */
					/*
					 * if talon is reporting things are good, keep adding to our
					 * timeout. Really this is so that you can unplug your talon in
					 * the middle of an MP and react to it.
					 */
					if (_status.isUnderrun == false) {
						_loopTimeout = NUM_LOOPS_TIMEOUT;
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
		}
		/* printfs and/or logging */
//		instrumentation.process(_status);
	}
    
	/** Start filling the MPs to all of the involved Talons. */
	private void startFilling() {
		/* since this example only has one talon, just update that one */
		startFilling(GeneratedMotionProfile.Points, GeneratedMotionProfile.NUM_OF_POINTS);
	}

	private void startFilling(double[][] profile, int totalCnt) {

		TrajectoryPoint point = new TrajectoryPoint();

		if (_status.hasUnderrun) {
			/* better log it so we know about it */
//			instrumentation.OnUnderrun();
			_talon.clearMotionProfileHasUnderrun(1);
		}
		
		_talon.clearMotionProfileTrajectories();

		for (int i = 0; i < totalCnt; ++i) {
			point.position = profile[i][0];
			point.velocity = profile[i][1];
			point.timeDur = TrajectoryDuration.Trajectory_Duration_10ms;
			point.profileSlotSelect0 = 0; 
			point.zeroPos = false;
			if (i == 0)
				point.zeroPos = true; 

			point.isLastPoint = false;
			if ((i + 1) == totalCnt)
				point.isLastPoint = true; 

			_talon.pushMotionProfileTrajectory(point);
		}
	}

	void startMotionProfile() {
		_bStart = true;
	}

	SetValueMotionProfile getSetValue() {
		return _setValue;
	}


}
