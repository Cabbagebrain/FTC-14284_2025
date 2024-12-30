package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.concurrent.TimeUnit;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

@TeleOp
public class RobotContainer extends LinearOpMode {
    private DcMotor elevatorR;
    private DcMotor elevatorL;
    
    private CRServo intake;
    private Servo rotateIntake;

    private Servo claw;
    private Servo rotateClaw;
    
    private IMU imu;
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;

    private double x;
    private double y;
    private double rx;
    private boolean pressed;

    @Override
    public void runOpMode() throws InterruptedException {

        pressed = false;
        frontLeftMotor = hardwareMap.dcMotor.get("FrontLeft");
        backLeftMotor = hardwareMap.dcMotor.get("BackLeft");
        frontRightMotor = hardwareMap.dcMotor.get("FrontRight");
        backRightMotor = hardwareMap.dcMotor.get("BackRight");
        
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        elevatorR = hardwareMap.dcMotor.get("elevatorR");
        elevatorL = hardwareMap.dcMotor.get("elevatorL");
        
        elevatorR.setDirection(DcMotorSimple.Direction.REVERSE);
        //Slot 1
        intake = hardwareMap.get(CRServo.class, "intake");
        //Slot 0
        rotateIntake = hardwareMap.get(Servo.class, "rotateIntake");
        //Slot 2
        claw = hardwareMap.get(Servo.class, "claw");
        
        //Slot 3
        rotateClaw = hardwareMap.get(Servo.class, "rotateClaw");
        //Claw will move over top but have to change values
        //rotateClaw.setDirection(Servo.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        MechanumDrivetrain drivetrain = new MechanumDrivetrain(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        Claw FullClaw = new Claw(claw, rotateClaw);
        Elevator elevators = new Elevator(elevatorR, elevatorL);
        Intake FullIntake = new Intake(intake, rotateIntake);
        
        waitForStart();
        
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            y = gamepad1.left_stick_y;
            x = -gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;

            if (gamepad1.left_stick_button){
                imu.resetYaw();
            }
            
            if(gamepad1.right_stick_button)
            {
                FullIntake.spinIntakeWithDegrees(-0.5);
            }
            
            if(gamepad1.share)
            {
                FullIntake.moveIntakeWithDegrees(Constants.Intake.PICKUP);
            }
            
            if(gamepad1.dpad_up){
                FullClaw.grabWithDegrees(Constants.Claw.OPEN_CLAW);
                Thread.sleep(200);
                /*FullIntake.moveIntakeWithDegrees(Constants.Intake.INTAKE_TO_CLAW);
                Thread.sleep(2500);*/
                FullClaw.moveClawWithDegrees(Constants.Claw.TAKE_PIECE_FROM_INTAKE);
                Thread.sleep(750);
                //FullIntake.spinIntakeWithDegrees(1);
                FullClaw.grabWithDegrees(Constants.Claw.CLOSE_CLAW);
                Thread.sleep(500);
                elevators.runElevatorUntilStop();
                Thread.sleep(1000);
                elevators.stopElevator();
                FullIntake.moveIntakeWithDegrees(Constants.Intake.PICKUP);
                Thread.sleep(300);
                FullClaw.moveClawWithDegrees(Constants.Claw.LOW_BASKET_DROP);
            }
            FullIntake.runIntake(gamepad1);
            FullIntake.moveIntake(gamepad1);
            
            FullClaw.rotateClaw(gamepad1);
            FullClaw.grab(gamepad1);
            
            elevators.runElevator(gamepad1);
            
            if(gamepad1.dpad_right){
                pressed = true;
            }
            
            drivetrain.setPower(gamepad1, imu, x, y, rx, pressed);
            drivetrain.drive();
        }
    }
}