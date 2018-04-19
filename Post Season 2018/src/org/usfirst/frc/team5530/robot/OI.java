/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5530.robot;


import org.usfirst.frc.team5530.robot.commands.*;
import org.usfirst.frc.team5530.robot.subsystems.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;


public class OI {
	
	public static Joystick stick1 = new Joystick(0);
	public static XboxController XBController = new XboxController(1);
	public static Button[] buttons = new Button[13];
	
	public OI(){
		//Making an array of Twelve Buttons
		for(int i=1; i <= 12; i++) {
			buttons[i] = new JoystickButton(stick1, i);
		}
		//Making the xBox Buttons
		Button xboxButtonA = new JoystickButton(XBController, 1),
				xboxButtonB = new JoystickButton(XBController, 2),
				xboxButtonX = new JoystickButton(XBController, 3),
				xboxButtonY = new JoystickButton(XBController, 4),
				xboxButtonLB = new JoystickButton(XBController, 5),
				xboxButtonRB = new JoystickButton(XBController, 6);
		
		buttons[9].toggleWhenPressed(new Intake(true));
		buttons[10].whenPressed(new MoveElevator(ElevatorSS.Position.BOT));
		buttons[11].whenPressed(new MoveElevator(ElevatorSS.Position.TOP));
	}
	
	
	public static boolean getButtonValue(int i) {
		return buttons[i].get();
	}
	
}
