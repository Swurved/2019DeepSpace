package frc.robot.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;

public class DriveTrainSubsystem extends Subsystem {

    private WPI_VictorSPX leftMaster = new WPI_VictorSPX(RobotMap.leftMasterId);
    private VictorSP leftSlave = new VictorSP(RobotMap.leftSlaveId);
    private WPI_VictorSPX rightMaster = new WPI_VictorSPX(RobotMap.rightMasterId);
    private VictorSP rightSlave = new VictorSP(RobotMap.rightSlaveId);

    private SpeedControllerGroup rightSide = new SpeedControllerGroup(rightMaster, rightSlave);
    private SpeedControllerGroup leftSide = new SpeedControllerGroup(leftMaster, leftSlave);

    private DifferentialDrive driveTrain = new DifferentialDrive(leftSide, rightSide);

    private Encoder leftEnc, rightEnc;
    private double kWheelDiameter = 8;
    private double kDistancePerRevolution = kWheelDiameter * Math.PI;
    private double kPulsesPerRevolution = 360;
    private double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution;

    
    public DriveTrainSubsystem() {
        // leftSlave.follow(leftMaster);
        // rightSlave.follow(rightMaster);

        // rightMaster.setInverted(true);
        // rightSlave.setInverted(true);

        leftEnc = new Encoder(RobotMap.LEFT_ENC_A, RobotMap.LEFT_ENC_B, true, EncodingType.k4X);
        leftEnc.setDistancePerPulse(kDistancePerPulse);
        leftEnc.setMaxPeriod(0.1);

        rightEnc = new Encoder(RobotMap.RIGHT_ENC_A, RobotMap.RIGHT_ENC_B, false, EncodingType.k4X);
        rightEnc.setDistancePerPulse(kDistancePerPulse);
        rightEnc.setMaxPeriod(0.1);

        driveTrain.setSafetyEnabled(false);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCom());
    }

    public void arcadeDrive(double speed, double rotation) {
        driveTrain.arcadeDrive(speed, rotation);
    }

    public void tankDrive(double left, double right) {
        leftMaster.set(ControlMode.PercentOutput, left);
        leftSlave.set(left);
        rightMaster.set(ControlMode.PercentOutput, right);
        rightSlave.set(right);
    }

    public void curvyDrive(double speed, double rotation, boolean quickTurn) {
        driveTrain.curvatureDrive(speed, rotation, quickTurn);
        System.out.println("Speed: " + speed + "  Rotation: " + rotation + "  Quick: " + quickTurn);
    }

    public void reverseMotors(boolean state) {
        leftMaster.setInverted(state);
        leftSlave.setInverted(state);
        rightMaster.setInverted(state);
        rightSlave.setInverted(state);
	}

    public int getLeftEncoder() {
        return leftEnc.get();
    }
    public double getLeftEncoderPos() {
        return leftEnc.getDistance();
    }

    public int getRightEncoder() {
        return rightEnc.get();
    }
    public double getRightEncoderPos() {
        return rightEnc.getDistance();
    }

    public int getAvgEncoder() {
        return (leftEnc.get() + rightEnc.get()) / 2;
    }
    public double getAvgEncoderPos() {
        return (rightEnc.getDistance() + leftEnc.getDistance()) / 2;
    }
    
    public void resetEncoders() {
        leftEnc.reset();
        rightEnc.reset();
    }

    public void logEncoders() {
        System.out.println("Left: " + getLeftEncoder() + "\tRight: " + getRightEncoder());
    }

}
