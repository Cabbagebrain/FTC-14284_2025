package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Drivetrain {
    private DcMotor FrontRight;
    private DcMotor FrontLeft;
    private DcMotor BackLeft;
    private DcMotor BackRight;

    private float drive;
    private float rotate;
    private float strafe;
    
    public Drivetrain(DcMotor FrontRight, DcMotor FrontLeft, DcMotor BackLeft, DcMotor BackRight, Gamepad gamepad1){
        FrontRight = FrontRight;
        FrontLeft = FrontLeft;
        BackLeft = BackLeft;
        BackRight = BackRight;
        gamepad1 = gamepad1; 

        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
    }
    public void drive(){
        drive = gamepad1.right_stick_y;
        rotate = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;
        FrontLeft.setPower(drive - rotate - strafe);
        FrontRight.setPower(drive + rotate + strafe);
        BackLeft.setPower(drive - rotate + strafe);
        BackRight.setPower(drive + rotate - strafe);
        ArmMotor.setPower(gamepad1.right_trigger);
        ArmMotor.setPower(-gamepad1.left_trigger);
    }
    public float getData(){
        return drive;
        return rotate;
        return strafe;
    }
