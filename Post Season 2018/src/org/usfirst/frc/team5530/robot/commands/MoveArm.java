package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.ArmSS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;


public class MoveArm extends Command {
	ArmSS.Position position;
	int counter;
	public static final int MAX_HOLD_TIME = 175;
	/**
	 * Move the Arm to a specific position
	 * @param position Position enum with options Stored, Resting, HoldResting, Mid, Top,
	 */
    public MoveArm(ArmSS.Position position) {
    		requires(Robot.armSS);
    		this.position = position;
    }

    protected void initialize(){
    		counter = 0;
    }

    protected void execute() {
	    	switch(position){
			case Stored:
				ArmSS.arm.set(-1);
				break;
			case Resting:
				ArmSS.arm.set(-1);
				break;
			case HoldResting:
				ArmSS.arm.set(ControlMode.PercentOutput, .0057*(ArmSS.potentiometer0.getValue() - ArmSS.RESTING_HEIGHT) + .16);
				break;
			case Mid:
				ArmSS.arm.set(1);
				break;
			case Top:
				ArmSS.arm.set(ControlMode.PercentOutput, .0057*(ArmSS.potentiometer0.getValue() - ArmSS.MAX_ARM_HEIGHT) + .16);
				break;
			}
    }
    
    protected boolean isFinished() {
	    	switch(position){
			case Stored:
				if (ArmSS.potentiometer0.getValue() >= 3300) return true; //TODO Set this arbitrary value(3300) to be correct
				break;
			case Resting:
				if (ArmSS.potentiometer0.getValue() >= 2500) return true;
				break;
			case HoldResting:
				break;
			case Mid:
				if(counter < MAX_HOLD_TIME) counter++;
				else return true;
				break;
			case Top:
				if(counter < MAX_HOLD_TIME) counter ++;
				else return true;
				break;
			}
        return false;
    }

    protected void end() {
    		ArmSS.arm.stopMotor();
    }

    protected void interrupted() {
    }
}
