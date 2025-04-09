// THE BELLOW CODE CONTAINS FIELD CENTRIC DRIVING AS THE MAIN DRIVING TECHNIQUE 

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "Old Drive")

public class FCDrive extends LinearOpMode {
    @Override
    
    public void runOpMode(){
        DcMotor backLeft = hardwareMap.dcMotor.get("Rear Left");
        DcMotor backRight = hardwareMap.dcMotor.get("Rear Right");
        DcMotor frontLeft = hardwareMap.dcMotor.get("Front Left");
        DcMotor frontRight = hardwareMap.dcMotor.get("Front Right");
        DcMotor MainArm = hardwareMap.dcMotor.get("MainArm");
        /*CRServo intake = hardwareMap.get(CRServo.class, "intake");
        CRServo pumper = hardwareMap.get(CRServo.class, "Pumper");*/
        Servo claw = hardwareMap.get(Servo.class, "claw");
        Servo wrist = hardwareMap.get(Servo.class, "wrist");
        Servo intakeWrist = hardwareMap.get(Servo.class,"intakeWrist");
        CRServo intake = hardwareMap.get(CRServo.class,"initialIntake");
        IMU imu = hardwareMap.get(IMU.class,"imu"); 

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
            imu.initialize(parameters);
        
        
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
            
            double speed = 1.75;
            double y =  - gamepad1.right_stick_y/speed;
            double x =  gamepad1.right_stick_x/speed;
            double rx =  gamepad1.left_stick_x/speed;
            double up = gamepad1.left_trigger;
            double down = gamepad1.right_trigger;
            boolean clawClose = gamepad1.a;
            boolean wristStraight = gamepad1.dpad_up;
            boolean wristLeft = gamepad1.dpad_left;
            boolean wristRight = gamepad1.dpad_right;
            boolean Im = gamepad1.left_bumper;
            boolean Wm = gamepad1.right_bumper;
            
            if(gamepad1.x){
            intake.setPower(0);
            }else if(gamepad1.left_bumper){
                intake.setPower(1);
            }else{
                intake.setPower(-1);
            }
            
            /*if(Im){
                intake.setPower(1);
            }else if (gamepad1.x){
                intake.setPower(0);
            }else{
                intake.setPower(-1);
            } */
            
            if (Wm){
                intakeWrist.setPosition(0.29);
            }else {
                intakeWrist.setPosition(1);
            }
            
           
            
            // open and close the  main claw
            if (clawClose){
                claw.setPosition(0.17);
            }else {
                claw.setPosition(0);
            }
            
        
            if (wristStraight)
            {
                wrist.setPosition(0.62);
            } 
            else if (wristLeft)
            {
                wrist.setPosition(1);
            }
            else if (wristRight)
            {
                wrist.setPosition(0.285);
            }
            

              
            
              MainArm.setPower(up-down); //control Main Arm 
              
              //below this is field centric drive
            if(gamepad1.b) {
                imu.resetYaw();
            }
           double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX *1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower =(rotY + rotX + rx) / denominator;
            double backLeftPower =(rotY - rotX + rx) / denominator;
            double frontRightPower =(rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);
            telemetry.update();
            telemetry.addData("Front Left Power", frontLeft.getPower());
            telemetry.addData("Front Right Power", frontRight.getPower());
            telemetry.addData("Back Left Power", backLeft.getPower());
            telemetry.addData("Back Right Power", backRight.getPower());
            

            

           
            


/*
            
             //initial intake move back and forth
            if (Wm){
                intakeWrist.setPosition(0.1);
            }else {
                intakeWrist.setPosition(0.76);
            } 
            
            if (wristStraight){
                wrist.setPosition(0.53);
            }else if (wristLeft){
                wrist.setPosition(0.2);
                }else {
              wrist.setPosition(0.855);  
            }
            
if (gamepad1.dpad_up){
                intake.setPower(1);
            }
            else {
                intake.setPower(-1);
            }
            
if (gamepad1.left_bumper) {
                pumper.setPower(1);
            }else if (gamepad1.right_bumper){
                pumper.setPower(-1);
            }else{
                pumper.setPower(0);
            }
            
            
 if (gamepad1.dpad_right){
                wrist.setPosition(0.13);
            }else if (gamepad1.dpad_left){
                wrist.setPosition(0.82);
            }else{
                wrist.setPosition(0.475);
            }

if (gamepad1.a){
                forward =- gamepad1.right_stick_y/2;
                strafe = gamepad1.right_stick_x/2;
                turn = gamepad1.left_stick_x/2;
            }
            */

            /*
            if(forward == 0 && strafe == 0 && turn == 0 ){
                frontRight.setPower(-0.1);
                backRight.setPower(0.1);
                frontLeft.setPower(-0.1);
                backLeft.setPower(0.1);
            }
            */


            // the bellow code is mechanum drive without field centric driving
            /* 
            frontRight.setPower(forward - strafe + turn);
            frontLeft.setPower(forward + strafe - turn);
            backLeft.setPower(forward - strafe - turn);
            backRight.setPower(forward + strafe + turn);
            */
            }
        }
}
