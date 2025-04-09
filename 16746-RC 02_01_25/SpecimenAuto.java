package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "SpecimenAuto", group = "Autonomous")
public class SpecimenAuto extends LinearOpMode {
    
        private DcMotor backLeft;
        private DcMotor backRight;
        private DcMotor frontLeft;
        private DcMotor frontRight;
        
        private DcMotor MainArm;
        private DcMotor Viper;
        private Servo claw;
        
        private Limelight3A limelight;
        private TargetLineup targetTrack;

    @Override
    public void runOpMode() throws InterruptedException{
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        
        backLeft = hardwareMap.dcMotor.get("Rear Left");
        backRight = hardwareMap.dcMotor.get("Rear Right");
        frontLeft = hardwareMap.dcMotor.get("Front Left");
        frontRight = hardwareMap.dcMotor.get("Front Right");
        MainArm = hardwareMap.dcMotor.get("MainArm");
        Viper = hardwareMap.dcMotor.get("Viper");
        claw = hardwareMap.get(Servo.class, "claw");
        
        targetTrack = new TargetLineup(frontLeft, frontRight, backLeft, backRight, limelight);
        
        MainArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Viper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MainArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Viper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        waitForStart();
        
        if (opModeIsActive()) {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-5050);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
            Thread.sleep(3000);
            
            drive(0.5, 0.2, 0);
            Thread.sleep(400);
            drive(0, 0, 0.3);
            Thread.sleep(100);
            drive(0, 0, 0);
            
            Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Viper.setTargetPosition(-2800);
            Viper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Viper.setPower(1);
            Thread.sleep(2500);
            
            claw.setPosition(0.4);
            Thread.sleep(300);
            
            Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Viper.setTargetPosition(-60);
            Viper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Viper.setPower(1);
            Thread.sleep(2500);
            
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-1200);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
            Thread.sleep(3000);
            
            drive(0, 0, -0.5);
            Thread.sleep(1100);
            drive(0.5, 0, 0);
            Thread.sleep(350);
            drive(0, 0, 0);
            
            claw.setPosition(0.61);
            Thread.sleep(100);
            
            Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Viper.setTargetPosition(-350);
            Viper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Viper.setPower(1);
            Thread.sleep(1200);
            
            /*claw.setPosition(0.4);
            Thread.sleep(100);*/
        }
    }
    
    private void drive(double forward, double strafe, double turn)
    {
        frontRight.setPower(forward - strafe + turn);
        frontLeft.setPower(forward + strafe - turn);
        backLeft.setPower(forward - strafe - turn);
        backRight.setPower(forward + strafe + turn);
    }
}
