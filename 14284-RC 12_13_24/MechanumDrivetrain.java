package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class MechanumDrivetrain{
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;

    private Gamepad gamepad1;
    private boolean pressed;

    private double denominator;
    private double frontLeftPower;
    private double frontRightPower;
    private double backLeftPower;
    private double backRightPower;
    
    private double rotX;
    private double rotY;
    private double botHeading;

    public MechanumDrivetrain(DcMotor frontLeftMotor, DcMotor backLeftMotor, DcMotor frontRightMotor, DcMotor backRightMotor){
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;
        
        pressed = false;
    }

    public void setPower(Gamepad gamepad1, IMU imu, double x, double y, double rx, boolean pressed){
        botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing
        
        denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        frontLeftPower = (rotY + rotX + rx) / denominator;
        backLeftPower = (rotY - rotX + rx) / denominator;
        frontRightPower = (rotY - rotX - rx) / denominator;
        backRightPower = (rotY + rotX - rx) / denominator;
        /*if(pressed){
            frontLeftPower /= 2;
            frontRightPower /=2;
            backLeftPower /= 2;
            backRightPower /= 2;
        }*/

    }
    
    public void drive(){
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }
    
    public void driveWithSetPower(double nX, double nY, double nrx)
    {
        // Rotate the movement direction counter to the bot's rotation
        nX = nX * 1.1;  // Counteract imperfect strafing

        denominator = Math.max(Math.abs(nY) + Math.abs(nX) + Math.abs(nrx), 1);
        frontLeftPower = (nY + nX + nrx) / denominator;
        backLeftPower = (nY - nX + nrx) / denominator;
        frontRightPower = (nY - nX - nrx) / denominator;
        backRightPower = (nY + nX - nrx) / denominator;
        
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }
}