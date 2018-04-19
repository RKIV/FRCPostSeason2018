package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.ArmSS;
import org.usfirst.frc.team5530.robot.subsystems.ElevatorSS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveElevator extends Command {
	ElevatorSS.Position position;
	/**
	 * Move the elevator to a certain position
	 * @param position Position enum with Bot, Mid, or Top
	 */
    public MoveElevator(ElevatorSS.Position position) {
        requires(Robot.elevatorSS);
        this.position = position;
    }

    protected void initialize() {
    }

    protected void execute() {
    		switch(position){
    		case Bot:
    			ElevatorSS.Elevator0.set(ControlMode.PercentOutput, -.25); 
    			break;
    		case Mid: 
    			if (ElevatorSS.Elevator0.getSelectedSensorPosition(0) < 19000) ElevatorSS.Elevator0.set(.75);
    			else ElevatorSS.Elevator0.set(.15);
    			break;
    		case Top:
    			if (ElevatorSS.Elevator0.getSelectedSensorPosition(0) < ElevatorSS.SLOW_HEIGHT) ElevatorSS.Elevator0.set(ControlMode.PercentOutput, 1);
    			else ElevatorSS.Elevator0.set(0.4);	
    			break;
    		}
    }

    protected boolean isFinished() {
	    	switch(position){
			case Bot:
				if(!ElevatorSS.elevatorSwitchBot.get()) return true;
				break;
			case Mid:
				break;
			case Top:
				if(!ElevatorSS.elevatorSwitchTop.get()) return true;
				break;
			}
        return false;
    }

    protected void end() {
    	switch(position){
		case Bot:
			ElevatorSS.Elevator0.set(0);
			ElevatorSS.Elevator0.setSelectedSensorPosition(0, 0, 0);
			break;
		case Mid:
			ElevatorSS.Elevator0.set(0);
			break;
		case Top:
			ElevatorSS.Elevator0.set(.18);
			break;
		}
    }

    protected void interrupted() {
    		ElevatorSS.Elevator0.set(0);
    }
}
