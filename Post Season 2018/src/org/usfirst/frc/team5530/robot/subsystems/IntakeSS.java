package org.usfirst.frc.team5530.robot.subsystems;

import org.usfirst.frc.team5530.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class IntakeSS extends Subsystem {

	public static WPI_TalonSRX Intake0 = new WPI_TalonSRX(RobotMap.I0);
	public static WPI_TalonSRX Intake1 = new WPI_TalonSRX(RobotMap.I1);
	
	public static boolean intakeOn = false;

	public static void initialize() {
		Intake1.set(ControlMode.Follower, (double)RobotMap.I0);
		Intake1.setInverted(true);
	}
    public void initDefaultCommand() {
    	
    }
}

