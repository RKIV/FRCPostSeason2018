package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.OI;
import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.DrivetrainSS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class XboxDrive extends Command {
	double lStick;
	double rTrigger;
	double lTrigger;
	
	
    public XboxDrive() {
        requires(Robot.drivetrain);
    }
    
    
    /**
     * Inverts the joystickstick based on the diretion of the robot
     * @param stick Joystick to be modified
     * @return The modified stick
     */
    public double getModifiedStick(double stick) {
    		//Reverses stick value based on the direction of the robot
		if (rTrigger - lTrigger >= 0) return stick;
		return -stick;
	}
    /** 
     * @return The speed to be set to the right side
     */
    public double getRightSpeed() {
		return rTrigger - lTrigger - getModifiedStick(lStick);
	}
	/**
	 * @return The speed to be set to the left side
	 */
	public double getLeftSpeed(){
		return rTrigger - lTrigger + getModifiedStick(lStick);
	
	}

	
	
    protected void initialize() {
    }

    protected void execute() {
    		//Updating XBox Controller Values
    		lStick = OI.XBController.getX(Hand.kLeft);
    		rTrigger = OI.XBController.getTriggerAxis(Hand.kRight);
    		lTrigger = OI.XBController.getTriggerAxis(Hand.kLeft);
    		
    	
    		//Raising the output to a power
    		if (getRightSpeed() > 0) DrivetrainSS.frontRight.set(ControlMode.PercentOutput, Math.pow(getRightSpeed(), 2) * .75);
		else DrivetrainSS.frontRight.set(ControlMode.PercentOutput, -Math.pow(getRightSpeed(), 2) * .75);
    		
		if (getLeftSpeed() > 0) DrivetrainSS.frontLeft.set(ControlMode.PercentOutput, Math.pow(getLeftSpeed(), 2) * .75);
		else DrivetrainSS.frontLeft.set(ControlMode.PercentOutput, -Math.pow(getLeftSpeed(), 2) * .75);
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }


    protected void interrupted() {
    }
}
