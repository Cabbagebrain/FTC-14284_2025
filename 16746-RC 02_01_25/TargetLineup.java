package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import java.util.List;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class TargetLineup {
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private Limelight3A limelight;
    private double forward, strafe, rotate, tx, ty, tz;
    private boolean targetFound;
    private double[] speed;
    
    public TargetLineup(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, Limelight3A limelight)
    {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.limelight = limelight;
        
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        
        limelight.pipelineSwitch(0);
    }
    
    public double[] lineUp()
    {
        double[] speed = new double[3];
        
        limelight.start();
        LLResult result = limelight.getLatestResult();
        
        tx = result.getTx();
        ty = result.getTy();
        //tz = result.getTargetXDegrees();
        targetFound = result.isValid();
        
        if (targetFound)
        {
            List<LLResultTypes.ColorResult> results = result.getColorResults();
            tz = results.get(0).getTargetXDegrees();
            //double offset = 10 * 0.02;
            if (ty < 5)
            {
                forward = 0.3;
            }
            else
            {
                forward = 0;
                
            }
            //forward = (ty < 5) ? 0.3 : 0;
            strafe = (-tx + 1) * 0.02;// - offset;
            rotate = (tz) * 0.01;
            
            speed[0] = forward;
            speed[1] = strafe;
            speed[2] = rotate;
        }
        
        else
        {
            speed[0] = 0;
            speed[1] = 0;
            speed[2] = 0;
        }
        
        return speed;
    }
}