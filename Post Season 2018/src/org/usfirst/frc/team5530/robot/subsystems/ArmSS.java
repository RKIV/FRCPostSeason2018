package org.usfirst.frc.team5530.robot.subsystems;

import org.usfirst.frc.team5530.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ArmSS extends Subsystem {

	public static final double MAX_ARM_HEIGHT = 460;
	public static final double RESTING_ARM_HEIGHT = 2900;
	
	public static WPI_TalonSRX arm = new WPI_TalonSRX(RobotMap.A0);
	
	public static AnalogInput potentiometer0 = new AnalogInput(RobotMap.armPotentiometer);

    public void initDefaultCommand() {
    	
    }
}

