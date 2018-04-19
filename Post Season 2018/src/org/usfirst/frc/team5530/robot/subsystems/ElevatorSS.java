package org.usfirst.frc.team5530.robot.subsystems;

import org.usfirst.frc.team5530.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ElevatorSS extends Subsystem {

	public static final double MIN_HEIGHT = 0;
	public static final double SLOW_HEIGHT = 28000;
	public static final double MAX_HEIGHT = 32800;
	
	public static WPI_TalonSRX Elevator0  = new WPI_TalonSRX(RobotMap.E0);
	public static WPI_TalonSRX Elevator1  = new WPI_TalonSRX(RobotMap.E1);
	
	public static DigitalInput elevatorSwitchTop = new DigitalInput(RobotMap.LS2);
	public static DigitalInput elevatorSwitchBot = new DigitalInput(RobotMap.LS3);
	
	public static enum Position{
		BOT, MID, TOP;
	}
	
	public static void initialize() {
		Elevator1.set(ControlMode.Follower, (double)RobotMap.E0);
		
		Elevator0.setInverted(true);
		Elevator1.setInverted(true);
		
		Elevator0.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);	
	}

    public void initDefaultCommand() {
        
    }
}

