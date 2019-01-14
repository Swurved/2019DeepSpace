package frc.robot.drive;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class DriveCom extends Command {

    // speed values taken in from controllers
    private double leftSpeed, rightSpeed;
    // deadzone of controller joystick
    private double deadzone = 0.04;

    private double v, h;
    private boolean quickturn;

    public DriveCom() {
        requires(Robot.driveTrain);
        System.out.println("Command constructor");
    }

    @Override
    protected void initialize() {
        leftSpeed = 0;
        rightSpeed = 0;
        v = 0;
        h = 0;
        System.out.println("Command initialize");
        quickturn = false;
    }

    @Override
    protected void execute() {
        // gets speeds from joysticks
        leftSpeed = -Robot.oi.driver.getY(Hand.kLeft);
        rightSpeed = -Robot.oi.driver.getY(Hand.kRight);
        // calls tankdrive method in drive subsystem with given speeds
        Robot.driveTrain.tankDrive(
            configSpeed(leftSpeed),
            configSpeed(rightSpeed)
        );

        // v = Robot.oi.driver.getY(Hand.kLeft);
        // h = Robot.oi.driver.getX(Hand.kLeft);
        // Robot.driveTrain.curveDrive(
        //     configSpeed(v),
        //     configSpeed(h)
        // );

        System.out.println("Left: " + Robot.driveTrain.getLeftEncoder() +
            "\tRight: " + Robot.driveTrain.getRightEncoder());
    }

    public double configSpeed(double s) {
        // applies a deadzone to the raw controller speeds
        if (Math.abs(s) <= deadzone)
            s = 0;
        // cubes the speeds for sensitivity control
        s = Math.pow(s, 3);
        s *= RobotMap.topSpeed;
        return s;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
    }

}