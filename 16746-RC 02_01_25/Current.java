/*
Copyright 2024 FIRST Tech Challenge Team 16746

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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


@TeleOp(name = "Current Drive")

public class Current extends LinearOpMode {

    boolean slow;
    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;
    
    @Override
    public void runOpMode() throws InterruptedException{
        DcMotor backLeft = hardwareMap.dcMotor.get("Rear Left");
        DcMotor backRight = hardwareMap.dcMotor.get("Rear Right");
        DcMotor frontLeft = hardwareMap.dcMotor.get("Front Left");
        DcMotor frontRight = hardwareMap.dcMotor.get("Front Right");
        DcMotor MainArm = hardwareMap.dcMotor.get("MainArm");
        DcMotor Viper = hardwareMap.dcMotor.get("Viper");
        Servo claw = hardwareMap.get(Servo.class, "claw");
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        slow = false;
        
        IMU imu = hardwareMap.get(IMU.class,"imu"); 

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        imu.initialize(parameters);
        
        TargetLineup targetTrack = new TargetLineup(frontLeft, frontRight, backLeft, backRight, limelight);
        
        imu.resetYaw();
        
        MainArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Viper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        MainArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Viper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        limelight.pipelineSwitch(0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        limelight.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
            double forward = -gamepad1.left_stick_y*0.9;
            double strafe = gamepad1.left_stick_x*-0.9;
            double turn = gamepad1.right_stick_x*0.9;
            double extend = gamepad1.left_trigger;
            double retract = gamepad1.right_trigger;
            boolean  up = gamepad1.dpad_up;
            boolean  fullDown = gamepad1.dpad_down;
            boolean  down = gamepad1.dpad_left;
            boolean farSide = gamepad1.dpad_right;
            boolean reset = gamepad1.y;
            boolean clawClose = gamepad1.a;
            //boolean reset = gamepad1.x;
            //telemetry.addData("retract", retract);
            
        Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        /*if (Viper.getCurrentPosition() <= -1900)
        {
            Viper.setPower(extend - retract);
        }
        else if(Viper.getCurrentPosition() >= 3000){
            Viper.setPower(0.0007 + (extend - retract));
        } else
        {
             Viper.setPower(0.0007 - retract);
        }*/
        
        Viper.setPower(extend - retract);
        
        if(down)
        {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-1600);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
        
        //MainArm.setPower(0);
        }
        
        if (farSide)
        {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-1700);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
        }
        
        if (fullDown)
        {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-1150);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
            
            //MainArm.setPower(0);
        } 
        /*else if (MainArm.getCurrentPosition() <=-3000 && Viper.getCurrentPosition() >= 1100){
            MainArm.setPower(1);
        }*/
        if (up)
        {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(-5150);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
        }
        
        if (reset)
        {
            MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MainArm.setTargetPosition(0);
            MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MainArm.setPower(1);
        }
            
             
        if(gamepad1.b) 
        {
            imu.resetYaw();
        }    
              
        if(gamepad1.x)
        {
            if(slow)
                slow = false;
            else
                slow = true;
        }
    
           // open and close the  main claw
        if (clawClose){
            claw.setPosition(0.4);
        }
        else 
        {
            claw.setPosition(0.61);
        }
        
        while(gamepad1.left_bumper)
        {
            double[] speed = targetTrack.lineUp(); 
            telemetry.addData("Speed[0]", speed[0]);
            telemetry.addData("Speed[1]", speed[1]);
           
            frontRight.setPower(speed[0] - speed[1] + speed[2]);
            frontLeft.setPower(speed[0] + speed[1] - speed[2]);
            backLeft.setPower(speed[0] - speed[1] - speed[2]);
            backRight.setPower(speed[0] + speed[1] + speed[2]);
            
            /*if(speed[0] < 0.01 && speed[1] < 0.01 && speed[2] < 0.01)
            {
                Thread.sleep(100);
                frontRight.setPower(0 - 0.5 + 0);
                frontLeft.setPower(0 + 0.5 - 0);
                backLeft.setPower(0 - 0.5 - 0);
                backRight.setPower(0 + 0.5 + 0);
            }
            */
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - Math.PI / 2;
        double rotX = forward * Math.cos(-botHeading) - strafe * Math.sin(-botHeading);
        double rotY = forward * Math.sin(-botHeading) + strafe * Math.cos(-botHeading);

        rotX = rotX *1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);
        //if(slow)
        /*{
            double frontLeftPower =((rotY + rotX + turn) / denominator)/2;
            double backLeftPower =((rotY - rotX + turn) / denominator)/2;
            double frontRightPower =((rotY - rotX - turn) / denominator)/2;
            double backRightPower = ((rotY + rotX - turn) / denominator)/2;
        }
        else
        {*/
        double frontLeftPower =(rotY + rotX + turn) / denominator;
        double backLeftPower =(rotY - rotX + turn) / denominator;
        double frontRightPower =(rotY - rotX - turn) / denominator;
        double backRightPower = (rotY + rotX - turn) / denominator; 
        //}

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
            
        telemetry.update();
        telemetry.addData("Position Arm", MainArm.getCurrentPosition());
        telemetry.addData("Position Viper", Viper.getCurrentPosition());
        telemetry.addData("Front Left Power", frontLeft.getPower());
        telemetry.addData("Front Right Power", frontRight.getPower());
        telemetry.addData("Back Left Power", backLeft.getPower());
        telemetry.addData("Back Right Power", backRight.getPower());
        telemetry.addData("Heading", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        }
    }
}
    

