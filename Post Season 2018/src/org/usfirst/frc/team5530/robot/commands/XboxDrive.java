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
    
    public double getModifiedStick(double stick) {
    		//Reverses stick value based on the direction of the robot
		if (rTrigger - lTrigger >= 0) return stick;
		return -stick;
	}
    
    public double RightSpeed() {
		return -(rTrigger - lTrigger - getModifiedStick(lStick));
	}
	
	public double LeftSpeed(){
		return rTrigger - lTrigger + getModifiedStick(lStick);
	
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		if (RightSpeed() > 0) DrivetrainSS.frontRight.set(ControlMode.PercentOutput, Math.pow(RightSpeed(), 2) * .75);
		else DrivetrainSS.frontRight.set(ControlMode.PercentOutput, -Math.pow(RightSpeed(), 2) * .75);
		if (LeftSpeed() > 0) DrivetrainSS.frontLeft.set(ControlMode.PercentOutput, Math.pow(LeftSpeed(), 2) * .75);
		else DrivetrainSS.frontLeft.set(ControlMode.PercentOutput, -Math.pow(LeftSpeed(), 2) * .75);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
