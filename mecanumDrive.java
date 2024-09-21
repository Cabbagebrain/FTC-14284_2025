package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "MecanumDriveTrain (Blocks to Java)", group = "")
public class MecanumDriveTrain extends LinearOpMode {

  private DcMotor FrontRight;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  private DcMotor BackRight;
  private DcMotor Arm;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    double vertical;
    float horizontal;
    float pivot;

    FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
    FrontRight = hardwareMap.dcMotor.get("FrontRight");
    BackLeft = hardwareMap.dcMotor.get("BackLeft");
    BackRight = hardwareMap.dcMotor.get("BackRight");
    Arm = hardwareMap.dcMotor.get("Arm");

    // Put initialization blocks here.
    FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    Arm.setDirection(DcMotorSimple.Direction.FORWARD);
    BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    BackRight.setDirection(DcMotorSimple.Direction.FORWARD);
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        vertical = -gamepad1.right_stick_y;
        horizontal = gamepad1.right_stick_y;
        pivot = gamepad1.left_stick_x;
        FrontLeft.setPower(-pivot + (vertical - horizontal));
        FrontRight.setPower(-pivot + vertical + horizontal);
        BackLeft.setPower(pivot + vertical + horizontal);
        BackRight.setPower(pivot + (vertical - horizontal));
        Arm.setPower(gamepad1.right_trigger);
        telemetry.update();
      }
    }
  }
}