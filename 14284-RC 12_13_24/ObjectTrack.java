package org.firstinspires.ftc.teamcode;

import android.util.Size;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;

import java.util.List;

@TeleOp(name = "ObjectTracking")

public class ObjectTrack extends LinearOpMode
{
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    
    double Strafe; //also mentioned as x
    double Drive; //also mentioned as y
    double Rotate; //also mentioned as rx
    
    double posRotate;
    double errorR;
    double integr;
    double timeR;
    double porpr;
    double derivr;
    
    private boolean objectTrack;
    
    RotatedRect boxFit;

    public void runOpMode() throws InterruptedException 
    {
        double targetr;
        double prevErrorR;
        double deltaTimeR;
        double kpr;
        double kir;
        double kdr;
        double currentTime = System.currentTimeMillis();
        
        waitForStart();
    
        

        //frontLeftMotor = hardwareMap.dcMotor.get("FrontLeft");
        //backLeftMotor = hardwareMap.dcMotor.get("BackLeft");
        //frontRightMotor = hardwareMap.dcMotor.get("FrontRight");
        //backRightMotor = hardwareMap.dcMotor.get("BackRight");
        
        frontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        backRight = hardwareMap.get(DcMotor.class, "BackRight");
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);

         MechanumDrivetrain drivetrain = new MechanumDrivetrain(frontLeft, backLeft, frontRight, backRight);
                        
        ColorBlobLocatorProcessor colorLocator = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.BLUE)         // use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
            .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 1, 1, -1))  // search central 1/4 of camera view
            .setDrawContours(true)                        // Show contours on the Stream Preview
            .setBlurSize(5)                               // Smooth the transitions between different colors in image
            .build();
        
        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam"))
                .setLiveViewContainerId(0)
                .build();
                
        GoBildaPinpointDriver odo;
        // Called from GoBildaPinPointDriver
        
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "OdoComp");
        
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        
        odo.setOffsets(24, 0);
       
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        
        odo.resetPosAndIMU();
        
        targetr = Constants.MecanumOdometryWebcamAutoLOGSPEED.targetr;
        prevErrorR = 0;
        kpr = 0.03;
        kir = 0;
        kdr = 200;

        telemetry.setMsTransmissionInterval(50);   // Speed up telemetry updates, Just use for debugging.
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
        
        waitForStart();
        
        if (isStopRequested()) return;
    
        while (opModeIsActive() || opModeInInit())
        {
            //telemetry.addData("X Cords", boxFit.center.x);
            //telemetry.addData("Y Cords", boxFit.center.y);
            //telemetry.addData("Angle", );
            telemetry.addData("ODO Angle", odo.getPosition().getHeading(AngleUnit.DEGREES));
            telemetry.update();
            
            //telemetry.addData("preview on/off", "... Camera Stream\n");

            // Read the current list
            List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();

            ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);  // filter out very small blobs.
            
            /*
             * The list of Blobs can be sorted using the same Blob attributes as listed above.
             * No more than one sort call should be made.  Sorting can use ascending or descending order.
             *     ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs);      // Default
             *     ColorBlobLocatorProcessor.Util.sortByDensity(SortOrder.DESCENDING, blobs);
             *     ColorBlobLocatorProcessor.Util.sortByAspectRatio(SortOrder.DESCENDING, blobs);
             */
            /* 
            import java.lang.Math
                Initialize variables:
                    double cx = x input from IMU;
                    double ci = i input from IMU;
                If you just want the angle theta,
                    double thrad = Math.atan(cx / ci);
                    double thdeg = thrad * 180 / Math.PI;
                    
                If you also want the distance of the hypotenuse
                    double thrad = Math.asin(Math.sqrt(cx ** 2 + ci ** 2), ci); or
                    double thrad = Math.acos(Math.sqrt(cx ** 2 + ci ** 2), ci);

            */
            timeR = System.currentTimeMillis();
            // timeR = currentTime;
            deltaTimeR = currentTime - timeR;
            errorR = targetr - posRotate;
            porpr = -errorR * kpr;
            integr += -errorR * (deltaTimeR);
            derivr = ((errorR - prevErrorR) / (deltaTimeR));
            posRotate = odo.getPosition().getHeading(AngleUnit.DEGREES);
            ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs);
            Rotate = (porpr + (kir * integr) + (kdr * derivr));
            frontRight.setPower(Drive + Rotate + Strafe);
            backRight.setPower(Drive + (Rotate - Strafe));
            frontLeft.setPower(Drive - (Rotate + Strafe));
            backLeft.setPower(Drive - (Rotate - Strafe));
            
            //telemetry.addLine(" Area Density Aspect  Center");
            if(blobs.size() > 0)
            {
                ColorBlobLocatorProcessor.Blob blob = blobs.get(0);
                boxFit = blob.getBoxFit();
                
                
                double x1 = odo.getPosition().getX(DistanceUnit.MM);
                double y1 = odo.getPosition().getY(DistanceUnit.MM);
                
                double x2 = boxFit.center.x;
                double y2 = boxFit.center.y;
                
                double slope = (y2 - y1) / (x2 - x1);
                double dist = x2 - x1;
                double angle = Math.atan(slope);

                //while((odo.getPosition().getHeading(AngleUnit.DEGREES) > angle - 1 && odo.getPosition().getHeading(AngleUnit.DEGREES) < angle + 1) && (opModeIsActive() || opModeInInit()))
                    
                telemetry.addData("X Cords", boxFit.center.x);
                telemetry.addData("Y Cords", boxFit.center.y);
                telemetry.addData("Angle", angle);
                telemetry.addData("ODO Angle", odo.getPosition().getHeading(AngleUnit.DEGREES));
                if(odo.getPosition().getHeading(AngleUnit.DEGREES) > angle)
                    drivetrain.driveWithSetPower(0, 0, Rotate);
                else if(odo.getPosition().getHeading(AngleUnit.DEGREES) < angle)
                    drivetrain.driveWithSetPower(0, 0, -Rotate);
                else
                    drivetrain.driveWithSetPower(0, 0, 0);
                    
                odo.update();
                telemetry.update();
                
                drivetrain.driveWithSetPower(0, 0, 0);
            }
        }
        sleep(50);
    }
}