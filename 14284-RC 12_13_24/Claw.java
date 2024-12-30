package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;


public class Claw {
    private Servo claw;
    private Servo rotateClaw;
    private Gamepad gamepad1;
    
    public Claw(Servo claw, Servo rotateClaw){
        this.claw = claw;
        this.rotateClaw = rotateClaw;
    }
    public void rotateClaw(Gamepad gamepad1){
        while (gamepad1.circle) {
            rotateClaw.setPosition(Constants.Claw.HIGH_BASKET_DROP);
        }
        /* gamepad1.x does not refer to the X button on the PS controller
        it's square */
        while (gamepad1.square) {
            rotateClaw.setPosition(Constants.Claw.SAMPLE);
        } 
        while (gamepad1.dpad_left) {
            rotateClaw.setPosition(Constants.Claw.TAKE_PIECE_FROM_INTAKE);
        }
        while (gamepad1.dpad_right) {
            rotateClaw.setPosition(Constants.Claw.LOW_BASKET_DROP);
        }
    }
    public void grab(Gamepad gamepad1){
        while (gamepad1.right_bumper) {
            claw.setPosition(Constants.Claw.OPEN_CLAW);
        }
        while (gamepad1.left_bumper) {
            claw.setPosition(Constants.Claw.CLOSE_CLAW);
        }
        
    }
    public void moveClawWithDegrees(double rotation){
        rotateClaw.setPosition(rotation);
    }
    
    public void grabWithDegrees(double degrees){
        claw.setPosition(degrees);
    }
}