package org.usfirst.frc.team5530.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team5530.robot.subsystems.DrivetrainSS;

import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.Notifier;
import com.ctre.phoenix.motorcontrol.can.*;

/**
 *
 */
public class MotionProfile extends Command {

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
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 1: /*
						 * wait for MP to stream to Talon, really just the first few
						 * points
						 */
					/* do we have a minimum numberof points in Talon */
					if (_status.btmBufferCnt > kMinPointsInTalon) {
						/* start (once) the motion profile */
						_setValue = CANTalon.SetValueMotionProfile.Enable;
						/* MP will start once the control frame gets scheduled */
						_state = 2;
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 2: /* check the status of the MP */
					/*
					 * if talon is reporting things are good, keep adding to our
					 * timeout. Really this is so that you can unplug your talon in
					 * the middle of an MP and react to it.
					 */
					if (_status.isUnderrun == false) {
						_loopTimeout = kNumLoopsTimeout;
					}
					/*
					 * If we are executing an MP and the MP finished, start loading
					 * another. We will go into hold state so robot servo's
					 * position.
					 */
					if (_status.activePointValid && _status.activePoint.isLastPoint) {
						/*
						 * because we set the last point's isLast to true, we will
						 * get here when the MP is done
						 */
						_setValue = CANTalon.SetValueMotionProfile.Hold;
						_state = 0;
						_loopTimeout = -1;
					}
					break;
			}
		}
		/* printfs and/or logging */
		instrumentation.process(_status);
	}
    
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
    		DrivetrainSS.frontRight.set(ControlMode.MotionProfile, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
