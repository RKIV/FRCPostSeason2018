package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.DrivetrainSS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForward extends Command{
	double encodeDistance;
	int startDistanceR;
	int startDistanceL;
	double timeout;
	
	public DriveForward(double distance, double timeout) {
		requires(Robot.drivetrainSS);
		double rotations = distance / (6 * Math.PI);
		this.encodeDistance = rotations * 1024;
	}
	
	protected void initialize() {
		startDistanceR = DrivetrainSS.frontRight.getSelectedSensorPosition(0);
		startDistanceL = DrivetrainSS.frontLeft.getSelectedSensorPosition(0);
	}
	
	protected void execute() {
		DrivetrainSS.frontRight.set(ControlMode.Position, startDistanceR + Math.rint(encodeDistance));
		DrivetrainSS.frontLeft.set(ControlMode.Position, startDistanceL + Math.rint(encodeDistance));
	}
	
	protected boolean isFinished() {
		if (((encodeDistance - DrivetrainSS.frontLeft.getSelectedSensorPosition(0)) < 200)) return true;
		return false;
	}
	
	protected void end() {
		DrivetrainSS.frontRight.set(0);
		DrivetrainSS.frontLeft.set(0);
	}
	
	protected void interrupted() {
		DrivetrainSS.frontRight.set(0);
		DrivetrainSS.frontLeft.set(0);
	}
	
}
