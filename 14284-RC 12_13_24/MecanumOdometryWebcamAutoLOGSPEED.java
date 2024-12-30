package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.digitalchickenlabs.CachingOctoQuad;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "MecanumOdometryAuto (Blocks to Java)")
public class MecanumOdometryWebcamAutoLOGSPEED extends LinearOpMode {

  private DcMotor frontRight;
  private DcMotor backRight;
  private DcMotor frontLeft;
  private DcMotor backLeft;
  
  double Strafe;
  double Drive;
  double Rotate;
  
  double posStrafe;
  double posDrive;
  double posRotate;
  
  double errorX;
  double errorY;
  double errorR;
  
  double integx;
  double integy;
  double integr;
  
  double timeX;
  double timeY;
  double timeR;
  
  double porpx;
  double porpy;
  double porpr;
  
  double derivx;
  double derivy;
  double derivr;
  
  double currentTime;

  @Override
  public void runOpMode() throws InterruptedException {
    double AutoSpeed;
    int targetx;
    int targety;
    int targetr;
    
    double kpx;
    double kix;
    double kdx;
    
    double kpy;
    double kiy;
    double kdy;
    
    double kpr;
    double kir;
    double kdr;
    
    double prevErrorX;
    double prevErrorY;
    double prevErrorR;
    
    int stableX;
    int stableY;
    int stableR;
    
    double deltaTimeX;
    double deltaTimeY;
    double deltaTimeR;
    
    GoBildaPinpointDriver odo;
    // Called from GoBildaPinPointDriver
    
    odo = hardwareMap.get(GoBildaPinpointDriver.class, "OdoComp");
    
    odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
    
    odo.setOffsets(24, 0);
   
    odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
    
    odo.resetPosAndIMU();

    frontRight = hardwareMap.get(DcMotor.class, "FrontRight");
    backRight = hardwareMap.get(DcMotor.class, "BackRight");
    frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
    backLeft = hardwareMap.get(DcMotor.class, "BackLeft");

   
    waitForStart();
    // Configure the telemetry for optimal display of data.
    telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
    telemetry.setMsTransmissionInterval(50);
    // Set all the encoder inputs to zero.
    telemetry.update();
    frontRight.setDirection(DcMotor.Direction.REVERSE);
    backRight.setDirection(DcMotor.Direction.REVERSE);
    frontLeft.setDirection(DcMotor.Direction.FORWARD);
    backLeft.setDirection(DcMotor.Direction.FORWARD);
    
    targetx = Constants.MecanumOdometryWebcamAutoLOGSPEED.targetx; 
    targety = Constants.MecanumOdometryWebcamAutoLOGSPEED.targety;
    targetr = Constants.MecanumOdometryWebcamAutoLOGSPEED.targetr;
    
    errorX = 0;
    errorY = 0;
    errorR = 0;
    
    prevErrorX = 0;
    prevErrorY = 0;
    prevErrorR = 0;
    
    kpx = 0.015;
    kix = 0;
    kdx = 0.1;
    
    kpy = 0.006;
    kiy = 0;
    kdy = 0.15;
    
    kpr = 0.03;
    kir = 0;
    kdr = 200;
    
    Drive = 0;
    Rotate = 0;
    Strafe = 0;
    
    porpx = 0;
    porpy = 0;
    porpr = 0;
    
    integx = 0;
    integy = 0;
    integr = 0;
    
    derivx = 0;
    derivy = 0;
    derivr = 0;
    
    stableX = 0;
    stableY = 0;
    stableR = 0;

    // Get the current time in milliseconds. The value returned represents
    // the number of milliseconds since midnight, January 1, 1970 UTC.
    timeX = currentTime;
    timeY = currentTime;
    timeR = currentTime;
    
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        
        odo.update();
        // Returns x position the unit of your choice
        posStrafe = odo.getPosition().getY(DistanceUnit.MM);
        telemetry.addData("Pos x", posStrafe);
        // Returns y position the unit of your choice
        posDrive = -odo.getPosition().getX(DistanceUnit.MM);
        telemetry.addData("Pos y", posDrive);
        // Returns the direction your robot is facing the unit of your choice
        posRotate = odo.getPosition().getHeading(AngleUnit.DEGREES);
        telemetry.addData("rotation", posRotate);
        
        telemetry.addData("power x", Strafe);
        telemetry.addData("power y", Drive);
        telemetry.addData("power r", Rotate);
        telemetry.addData("error x:", errorX);
        telemetry.addData("error y:", errorY);
        
        currentTime = System.currentTimeMillis();
        
        deltaTimeX = currentTime - timeX;
        deltaTimeY = currentTime - timeY;
        deltaTimeR = currentTime - timeR;
        
        // set x speed then y speed
        errorX = targetx - posStrafe;
        errorY = targety - posDrive;
        errorR = targetr - posRotate;
        
        porpx = errorX * kpx;
        porpy = errorY * kpy;
        porpr = -errorR * kpr;
        // Get the current time in milliseconds. The value returned represents
        // the number of milliseconds since midnight, January 1, 1970 UTC.
        integx += errorX * (deltaTimeX);
        integy += errorY * (deltaTimeY);
        integr += -errorR * (deltaTimeR);
        // Get the current time in milliseconds. The value returned represents
        // the number of milliseconds since midnight, January 1, 1970 UTC.
        Thread.sleep(1);
        derivx = ((errorX - prevErrorX) / (deltaTimeX));
        derivy = ((errorY - prevErrorY) / (deltaTimeY));
        derivr = ((errorR - prevErrorR) / (deltaTimeR));
        
        Strafe = (porpx + (kix * integx) + (kdx * derivx));
        Drive =  (porpy + (kiy * integy) + (kdy * derivy));
        Rotate = (porpr + (kir * integr) + (kdr * derivr));
        
        frontRight.setPower(Drive + Rotate + Strafe);
        backRight.setPower(Drive + (Rotate - Strafe));
        frontLeft.setPower(Drive - (Rotate + Strafe));
        backLeft.setPower(Drive - (Rotate - Strafe));
        
        prevErrorX = errorX;
        prevErrorY = errorY;
        prevErrorR = errorR;
        
        telemetry.update();
        // Share the CPU.
        sleep(20);
      }
    }
  }
}
