/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5530.robot;


public class RobotMap {
	/**CAN IDs**/
	//Driving
	public static int FR = 1; //1
	public static int FL = 2; //2
	public static int BR = 3;
	public static int BL = 4; 
	
	//Arm
	public static int A0 = 10; //10
	
	//Elevator
	public static int E0 = 11; //11 
	public static int E1 = 12; //12
	
	//Intake
	public static int I0 = 20;
	public static int I1 = 21;
	
	//Climb
	public static int C0 = 30; //30
	public static int C1 = 31; //31
	
	/**INPUT**/
	//Potentiometer - Analog Input
	public static int armPotentiometer = 0;
	
	//Limit Switches - Digital Input
	public static int LS0 = 0;
	public static int LS1 = 1;
	public static int LS2 = 2;
	public static int LS3 = 3;
	
	/**ENUMS**/
	public static enum Sides{
		LEFT(false, true, "left"), RIGHT(true, false, "right");
		
		private final boolean isRight;
		private final boolean isLeft;
		private final String name;
		Sides(boolean isRight, boolean isLeft, String name) {
			this.isRight = isRight;
			this.isLeft = isLeft;
			this.name = name;
		}
		public boolean isRight() { return isRight; }
	    public boolean isLeft() { return isLeft; }
	    public String toString() { return name; }
	}
	
}
