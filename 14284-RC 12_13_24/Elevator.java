package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotContainer;

public class Elevator {
  public DcMotor elevatorR;
  public DcMotor elevatorL; 
  private Gamepad gamepad1;
  
  public Elevator(DcMotor elevatorR, DcMotor elevatorL){
    this.elevatorR = elevatorR;
    this.elevatorL = elevatorL;
  }

  public void runElevator(Gamepad gamepad1) {
    if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0) {
      //change this to PID control?
      elevatorR.setPower(-0.01); //prevents lift from falling down on itself
      elevatorL.setPower(-0.01);
    } 
    else {
        elevatorR.setPower(gamepad1.left_trigger);
        elevatorR.setPower(-gamepad1.right_trigger);
        elevatorL.setPower(gamepad1.left_trigger);
        elevatorL.setPower(-gamepad1.right_trigger);
    }
  }
    
  public void stopElevator() {
      elevatorR.setPower(-0.01);
      elevatorL.setPower(-0.01);
  }
  
  public void runElevatorUntilStop()
  {
    elevatorR.setPower(-0.8);
    elevatorL.setPower(-0.8);
  }
}