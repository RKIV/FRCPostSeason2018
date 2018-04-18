/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5530.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team5530.robot.subsystems.*;

public class Robot extends TimedRobot {
	public static OI oi;
	public static DrivetrainSS drivetrain;
	
	//Removing Magic Strings
	static final String driveForward = "Drive Forward";
	static final String driveForwardAndDeliver = "Drive Forward and Deliver";
	static final String farLeft = "Far Left";
	static final String farRight = "Far Right";
	static final String centerSwitch = "Center Switch";
	
	//Creating the Autonomous Chooser
	SendableChooser<String> autonChooser = new SendableChooser<>();
	//The Command that will be set and called in autonomous 
	Command autonCommand;

	@Override
	public void robotInit() {
		oi = new OI();
		drivetrain = new DrivetrainSS();
		
		autonChooser.addDefault(driveForward, driveForward);
		autonChooser.addObject(driveForwardAndDeliver, driveForwardAndDeliver);
		autonChooser.addObject(centerSwitch, centerSwitch);
		autonChooser.addObject(farRight, farRight);
		autonChooser.addObject(farLeft, farLeft);
		SmartDashboard.putData("Auton Chooser",autonChooser);
		
		
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		String autonSelected = autonChooser.getSelected();
		switch(autonSelected) {
			case driveForward:
				//TODO: Create Drive Forward Command
				break;
			case driveForwardAndDeliver:
				//TODO: Create Drive Forward and Deliver Command
				break;
			case centerSwitch:
				//TODO: Create Center Switch Command
				break;
			case farRight:
				//TODO: Add Far Right Logic
				break;
			case farLeft:
				//TODO: ADD Far Left Logic
				break;
		}


		// schedule the autonomous command (example)
		if (autonCommand != null) {
			autonCommand.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

		if (autonCommand != null) {
			autonCommand.cancel();
		}
	}


	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}


	@Override
	public void testPeriodic() {
	}
}
