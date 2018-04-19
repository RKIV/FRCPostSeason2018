package org.usfirst.frc.team5530.robot.commands;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.subsystems.IntakeSS;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Intake extends Command {
	boolean 	turnIntakeOn;
	/**
	 * Turn intake on or off
	 * @param intakeOn Turn intake on(true) or off(false)?
	 */
    public Intake(boolean intakeOn) {
        requires(Robot.intakeSS);
        turnIntakeOn = intakeOn;
    }

    protected void initialize() {
    		if (turnIntakeOn) IntakeSS.Intake0.set(1);
    		else IntakeSS.Intake0.set(0);
    		IntakeSS.intakeOn = turnIntakeOn;
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return turnIntakeOn;
    }

    protected void end() {
    		IntakeSS.Intake0.set(0);
    		IntakeSS.intakeOn = false;
    }

    protected void interrupted() {
    		IntakeSS.Intake0.set(0);
    		IntakeSS.intakeOn = false;
    }
}
