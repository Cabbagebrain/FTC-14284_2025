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
  private DcMotor ArmMotor;

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
    Drivetrain = new Drivetrain(FrontLeft, FrontRight, BackLeft, BackRight, gamepad1)
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        Drivetrain.drive()
        telemetry.update();
      }
    }
  }
}
