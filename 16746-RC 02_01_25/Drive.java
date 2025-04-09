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


@TeleOp(name = " Non FC Drive")

public class Drive extends LinearOpMode {


    @Override
    public void runOpMode() {
        DcMotor backLeft = hardwareMap.dcMotor.get("Rear Left");
        DcMotor backRight = hardwareMap.dcMotor.get("Rear Right");
        DcMotor frontLeft = hardwareMap.dcMotor.get("Front Left");
        DcMotor frontRight = hardwareMap.dcMotor.get("Front Right");
        DcMotor MainArm = hardwareMap.dcMotor.get("MainArm");
        DcMotor Viper = hardwareMap.dcMotor.get("Viper");
        Servo claw = hardwareMap.get(Servo.class, "claw");
        
        IMU imu = hardwareMap.get(IMU.class,"imu"); 

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
            imu.initialize(parameters);
        
        
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        MainArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Viper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
            double forward = - gamepad1.left_stick_y/1.75;
            double strafe = gamepad1.left_stick_x/1.75;
            double turn = gamepad1.right_stick_x/1.75;
            double extend = gamepad1.left_trigger;
            double retract = gamepad1.right_trigger;
            boolean  up = gamepad1.dpad_up;
            boolean  fullDown = gamepad1.dpad_down;
            boolean  down = gamepad1.dpad_left;
            boolean reset = gamepad1.y;
            boolean clawClose = gamepad1.a;
            
            
          Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        if (MainArm.getCurrentPosition()>=-1900 && Viper.getCurrentPosition() >= 1800){
            Viper.setPower(0 - retract);
        }else if(Viper.getCurrentPosition()>= 3000){
            Viper.setPower(0.0007 + (extend - retract));
        } else if(Viper.getCurrentPosition() >= 6500){
             Viper.setPower(0.0007 - retract);
        }else if (Viper.getCurrentPosition() >= 5900){
            gamepad1.rumble(1,1,500);
        }else{
            Viper.setPower(extend - retract); //control Viper Slide
        }

        if(down){MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MainArm.setTargetPosition(-1650);
        MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MainArm.setPower(1);
        while(MainArm.isBusy()) {
        }
        MainArm.setPower(0);
        }
        
        if (fullDown){MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MainArm.setTargetPosition(-1200);
        MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MainArm.setPower(1);
        while (MainArm.isBusy()) {
        }
        MainArm.setPower(0);
        } else if (MainArm.getCurrentPosition() <=-3000 && Viper.getCurrentPosition() >= 1100){
            MainArm.setPower(1);
        }
            if (up){MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MainArm.setTargetPosition(-5200);
        MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MainArm.setPower(1);
        while (MainArm.isBusy()) {
        }
        MainArm.setPower(0);
        }
        
        if (reset){MainArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MainArm.setTargetPosition(0);
        MainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MainArm.setPower(1);
        while (MainArm.isBusy()) {
        }
        MainArm.setPower(0);
        }
            
             
            
              
            if(gamepad1.b){
            forward =- gamepad1.right_stick_y/2;
             strafe = gamepad1.right_stick_x/2;
             turn = gamepad1.left_stick_x/2;
           }
    
           // open and close the  main claw
            if (clawClose){
                claw.setPosition(0.4);
            }else {
                claw.setPosition(0.61);
            }
            
            
            frontRight.setPower(forward - strafe + turn);
            frontLeft.setPower(forward + strafe - turn);
            backLeft.setPower(forward - strafe - turn);
            backRight.setPower(forward + strafe + turn);
            
            telemetry.update();
            telemetry.addData("Position Arm", MainArm.getCurrentPosition());
            telemetry.addData("Position Viper", Viper.getCurrentPosition());
            telemetry.addData("Front Left Power", frontLeft.getPower());
            telemetry.addData("Front Right Power", frontRight.getPower());
            telemetry.addData("Back Left Power", backLeft.getPower());
            telemetry.addData("Back Right Power", backRight.getPower());
            
            }
        }
}
    

