package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.DrivetrainSS;
import org.usfirst.frc.team5530.robot.motionprofile.*;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MotionProfile extends Command {
	MotionProfileExecuter MP = new MotionProfileExecuter(DrivetrainSS.frontRight);
	
    public MotionProfile() {
        requires(Robot.drivetrainSS);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		DrivetrainSS.frontRight.set(ControlMode.MotionProfile, 0);
    		MP.reset();
    		MP.startMotionProfile();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		MP.control();
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
