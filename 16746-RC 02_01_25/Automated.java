package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class Automated extends LinearOpMode {
    
    public final double WAIT_TIME = 25;
    public final double DRIVE_TIME = 3;
    public final double DRIVE_SPEED = .5f;
    
    public void runOpMode() {
        DcMotor backLeft = hardwareMap.dcMotor.get("Rear Left");
        DcMotor backRight = hardwareMap.dcMotor.get("Rear Right");
        DcMotor frontLeft = hardwareMap.dcMotor.get("Front Left");
        DcMotor frontRight = hardwareMap.dcMotor.get("Front Right");
        
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        ElapsedTime elapsedTime = new ElapsedTime();
        
        while (opModeIsActive()) {
            if (elapsedTime.seconds() < WAIT_TIME) {
                // Wait for "WAIT_TIME" seconds
            } else if (elapsedTime.seconds() < WAIT_TIME + DRIVE_TIME) {
                // Drive forward for "DRIVE_TIME" seconds
                backLeft.setPower(DRIVE_SPEED);
                backRight.setPower(DRIVE_SPEED);
                frontLeft.setPower(DRIVE_SPEED);
                frontRight.setPower(DRIVE_SPEED);
            } else {
                // Stop driving forward after "DRIVE_TIME" seconds
                backLeft.setPower(0);
                backRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
            }
        }
    }
    
}