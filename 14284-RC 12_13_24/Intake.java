package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;


public class Intake 
{
    private CRServo intake;
    private Servo rotateIntake;
    private boolean intakePressed;
    private boolean moveIntakePressed;
    
    public Intake(CRServo intake, Servo rotateIntake)
    {
        this.intake = intake;
        this.rotateIntake = rotateIntake;
        intakePressed = false;
        moveIntakePressed = false;
    }
    public void runIntake(Gamepad gamepad1)
    {
        /* gamepad1.y is not the Y found 
        on the PS controller; it is triangle 
        if (!intakePressed && gamepad1.y) {
            intake.setPower(0.4);
            intakePressed = true; 
        } */
        if(gamepad1.triangle) 
        {
            intake.setPower(0.7);
        } 
        else 
        {
            intake.setPower(0);
        }
    }   
    public void moveIntake(Gamepad gamepad1) 
    {
        if (!moveIntakePressed && gamepad1.dpad_down) 
        {
            rotateIntake.setPosition(Constants.Intake.PICKUP);
            moveIntakePressed = !moveIntakePressed;
        }    
        if (moveIntakePressed && gamepad1.dpad_down) 
        {
           rotateIntake.setPosition(Constants.Intake.INTAKE_TO_CLAW);
           moveIntakePressed = !moveIntakePressed;
        }
    }
    
    public void moveIntakeWithDegrees(double degrees)
    {
        rotateIntake.setPosition(degrees);
    }
    
    public void spinIntakeWithDegrees(double degrees)
    {
        intake.setPower(degrees);
    }
}