
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.*;
import edu.wpi.first.wpilibj.Timer;


/**
 * Will be used for drive 
 * 
 * @author Michael C., Sarang M.
 */
public class GearTrain extends RobotDrive {
    //Joystick leftStick, rightStick;
    private final double MAX_DRIVE_VALUE = 1; // desired Max Value of speed/joystick while driving
    private final int INVERSION = -1;
    
    double filterLSpeed;
    double filterRSpeed;
    
    double driveAlpha;
    
    DriveEncoder left;
    DriveEncoder right;
    
    TitanGyro gyro;
    public boolean atAngle = false;
    double correction=  0.2;//positive value b/t 0.0 and 1.0
    double gAlpha; //driveAlpha while using gyro, 0.1 works well for now
    
    private final double ROBOT_DIAMETER_FT = 2.268;
    private final double ROBOT_RADIUS_FT = ROBOT_DIAMETER_FT/2;
    
    Timer time;
    
    double trim = 0.853;
    private double stepLength = 0.0;
    private double stepSpeed = 0.0;
    private double stepAngle = 0.0;
    double angleThreshold;
    
    
    /**
     * Constructor, utilizes RobotDrive
     * @param l left encoder
     * @param r right encoder
     */
    public GearTrain (DriveEncoder l, DriveEncoder r, TitanGyro g){
        super(HWR.LEFT_MOTOR, HWR.RIGHT_MOTOR);
        left = l;
        right = r;
        gyro = g;
        time = new Timer();
        //leftStick = passL;
        //rightStick = passR;
    }
    
    /**
     * Sets the speed of both sides using input from Driver Station, used for driving
     * This code is all for tank drive. No arcade drive so far. 
     */
    public void driveMode(){ 
        //driveAlpha = DriverStation.getDouble("DriveAlpha");
        //double lSpeed = leftStick.getY();
        //double rSpeed = rightStick.getY();
        double lSpeed = DriverStation.leftStick.getRawAxis(DriverStation.kYAxis);
        double rSpeed = DriverStation.rightStick.getRawAxis(DriverStation.kYAxis);

        lSpeed = quadScale(lSpeed)*INVERSION;
        rSpeed = quadScale(rSpeed)*INVERSION;
        
        driveAlpha = 0.5;
        
        filterLSpeed = (1-driveAlpha) * lSpeed + driveAlpha*filterLSpeed;
        filterRSpeed = (1-driveAlpha) * rSpeed + driveAlpha*filterRSpeed;

        
        setMotorSpeed(filterLSpeed, filterRSpeed);
        
        SmartDashboard.putNumber("Left Speed", lSpeed);
        SmartDashboard.putNumber("Right Speed", rSpeed);
    }
    
    /**
     * Sets the speed of both sides
     * @param lSpeed speed of the left side
     * @param rSpeed speed of the right side
     */
    public void setMotorSpeed(double lSpeed, double rSpeed){
        setLeftRightMotorOutputs(-lSpeed, rSpeed);
    }
    
    
    /**
     * Sets the speed of both sides using input from Driver Station, not scaled.
     */
    public void setMotorSpeed(){
        double lSpeed = DriverStation.leftStick.getRawAxis(DriverStation.kYAxis);
        double rSpeed = DriverStation.rightStick.getRawAxis(DriverStation.kYAxis);
        setMotorSpeed(lSpeed, rSpeed);
    }
    
    /**
     * Based off of 2012 quadScale method, scales joystick input to be easier to use.
     * @param toScale the value to be scaled
     * @return the scaled value
     */
    private double quadScale(double toScale){
        toScale = toScale*MAX_DRIVE_VALUE;
        
        if (toScale < 0) {
            toScale *= -toScale;
        } else {
            toScale *= toScale;
        }
        return toScale;
    }
     /**
     * Turns the robot to a desired angle using the encoders
     * @param angle the desired angle to turn to (degrees) clockwise is negative, don't go over 180 or ur dum
     * @param speed the desired speed at which the motors will rotate; don't be negative or ur dum
     */
    public void encoderTurn(double angle, double speed){
        double leftSpeed, rightSpeed, distance;
        double radians = Math.toRadians(angle);
        if (radians > 0){
            leftSpeed = -speed;
            rightSpeed = speed;
        } else if (radians < 0){
            leftSpeed = speed;
            rightSpeed = -speed;
        } else {
            leftSpeed = 0;
            rightSpeed = 0;
            System.out.println("ur dum");
        }
        distance = radians*ROBOT_RADIUS_FT;
        left.start();
        right.start();
        left.reset();
        right.reset();
        while (Math.abs(left.getDistanceFt())< distance ||  Math.abs(right.getDistanceFt())< distance){
            setMotorSpeed(leftSpeed, rightSpeed);
            System.out.println("Turning");
        } 
        setMotorSpeed(0,0);
        System.out.println("Finished");

    }
    /**
     * Drives forward a certain number of counts 
     * @param counts number of counts you want to drive
     */
    public void encoderDrive (int counts) {
        left.start();
        right.start();
        left.reset();
        right.reset();
        
        System.out.println("Counts:"+ counts);
        while (Math.abs(left.get()) < counts && Math.abs(right.get())<counts) {
            setMotorSpeed(0.4,0.4);
            System.out.println("Driving");
            SmartDashboard.putNumber("Trim", trim);
        } 
        setMotorSpeed(0,0);
        System.out.println("Finished");
 
    }
    /**
     * puts the number of counts in the smart dashboard
     */
    public void getCounts () {
        SmartDashboard.putNumber("Left Counts", left.get());
        SmartDashboard.putNumber("Right Counts", right.get());
    }
    
    /**
     * Drives a certain number of feet
     * @param feet how many feet you want it to drive
     */
    public void encoderDriveFeet(double feet) {
        left.start();
        right.start();
        left.reset();
        right.reset();
        System.out.println("Feet:"+ feet);
        while (Math.abs(left.getDistanceFt()) < feet && Math.abs(right.getDistanceFt())<feet) {
            setMotorSpeed(0.4,0.4);
            System.out.println("Driving");
        } 
        setMotorSpeed(0,0);
        System.out.println("Finished");
 
    }
    /**
     * turns the robot to a certain angle (based on gyro)
     * @param angle
     * @param turnSpeed 
     */
    public void turnAngle(double angle, double turnSpeed)
    {
        
        gyro.reset();
        if(angle<0)
        while(gyro.angle()<angle)
        {
           this.setMotorSpeed(-turnSpeed,turnSpeed);
        }
        else if(angle>0)
         while(gyro.angle()>angle)
        {
           this.setMotorSpeed(turnSpeed,-turnSpeed);
        }
        else
        {
            this.setMotorSpeed(0.0, 0.0);
        }
        this.setMotorSpeed(0.0, 0.0);
    }
    /**
     * Drives with DA GYRO, also UNTESTED, DON'T ASSUME IT WORKS
     */
    public void gyroDrive(){
        double lSpeed = DriverStation.leftStick.getRawAxis(DriverStation.kYAxis);
        double rSpeed = DriverStation.rightStick.getRawAxis(DriverStation.kYAxis);
                
        lSpeed = quadScale(lSpeed)*INVERSION;
        rSpeed = quadScale(rSpeed)*INVERSION;
        
        driveAlpha = 0.5;
        
        filterLSpeed = (1-driveAlpha) * lSpeed + driveAlpha*filterLSpeed;
        filterRSpeed = (1-driveAlpha) * rSpeed + driveAlpha*filterRSpeed;

        if (DriverStation.antiBounce(HWR.LEFT_JOYSTICK, HWR.GYRO_DRIVE_ACTIVATE)){
            gyro.reset();
            while (DriverStation.leftStick.getRawButton(HWR.GYRO_DRIVE_ACTIVATE)){
                filterRSpeed = filterLSpeed;
                if (gyro.angle() > 0 && gyro.angle() <= 90.0){
                    filterRSpeed *= (90.0-gyro.angle())/90.0; //Assumes the robot will never be more than 90 degrees off position
                } else if (gyro.angle() < 0 && gyro.angle() >= -90.0){
                    filterLSpeed *= (90.0+gyro.angle())/90.0; //Assumes the robot will never be more than 90 degrees off position
                } else {
                    //do nothing
                }
                setMotorSpeed(filterLSpeed, filterRSpeed);
            }
        } else {
            setMotorSpeed(filterLSpeed, filterRSpeed);
        }
    }
    
    /**
     * Gyro Drive that doesn't use joysticks, make sure to reset gyro before calling this method. Drives straight
     * @param speed desired speed from 0.0 to 1.0
     */
    public void gyroDrive(double speed){
        
        driveAlpha = gAlpha; //may change on actual robot
        
        filterLSpeed = (1-driveAlpha) * speed + driveAlpha*filterLSpeed;
        filterRSpeed = (1-driveAlpha) * speed + driveAlpha*filterRSpeed;
        
        double lSpeedAdd = 0.0;
        double rSpeedAdd = 0.0;
        double lSpeed = 0.0;
        double rSpeed = 0.0;
        
        //Clockwise = positive
        if (gyro.angle() > angleThreshold){
            rSpeedAdd = 0.0;//may change on actual robot 
            lSpeedAdd = -1 * correction;//0.4 works on mule
        } else if (gyro.angle() < -1*angleThreshold){
            lSpeedAdd = 0.0;
            rSpeedAdd = -1 * correction;
        }
        
        SmartDashboard.putNumber("lSpeedAdd", lSpeedAdd);
        SmartDashboard.putNumber("rSpeedAdd", rSpeedAdd);
        
        lSpeed = filterLSpeed + lSpeedAdd;
        rSpeed = filterRSpeed + rSpeedAdd;

        SmartDashboard.putNumber("Left Speed", lSpeed);
        SmartDashboard.putNumber("Right Speed", rSpeed);
        
        setMotorSpeed(lSpeed,rSpeed);
    }
    
    /**
     * Gyro Drive that turns to a set angle while driving
     * @param speed desired speed from 0.0 to 1.0
     * @param angle desired turning angle
     */
    public void gyroDriveTurn(double speed, double angle){
        
        driveAlpha = gAlpha; //may change on actual robot
        
        filterLSpeed = (1-driveAlpha) * speed + driveAlpha*filterLSpeed;
        filterRSpeed = (1-driveAlpha) * speed + driveAlpha*filterRSpeed;
        
        double lSpeedAdd = 0.0;
        double rSpeedAdd = 0.0;
        double lSpeed = 0.0;
        double rSpeed = 0.0;
        
        
        //Clockwise = positive
        if (angle >= 0 && gyro.angle() < angle){
            lSpeedAdd = 0.0;//may change on actual robot 
            rSpeedAdd = -1 * correction;//0.4 works pretty well for mule bot
            atAngle = false;
        } else if (angle <= 0 && gyro.angle() > angle){
            rSpeedAdd = 0.0;
            lSpeedAdd = -1 * correction;
            atAngle = false;
        } else {
            rSpeedAdd = 0.0;
            lSpeedAdd = 0.0;
            atAngle = true;
        }
        
        lSpeed = filterLSpeed + lSpeedAdd;
        rSpeed = filterRSpeed + rSpeedAdd;
        
        setMotorSpeed(lSpeed,rSpeed);
    }
    
    /**
     * turning using the gyro
     * @param angle desired turning angle
     */
    public void gyroTurn(double angle){
        gyro.reset();
        double speed = 0.8;
        double lSpeed, rSpeed;
        while (Math.abs(gyro.angle())<= Math.abs(angle)){
            if (angle > 0){
                lSpeed = speed;
                rSpeed = -speed;
            } else {
                lSpeed = -speed;
                rSpeed = speed;
            }
            setMotorSpeed(lSpeed,rSpeed);
            SmartDashboard.putNumber("Angle", gyro.angle());
        }
        setMotorSpeed(0.0,0.0);
    }
    
    /**
     * Method for entering a series of commands, note that this one is strictly time-based
     * @param c a list of commands containing time(seconds), speed(-1.0 to 1.0), and angle(degrees)
     */
    public void commandList(double[][] c){
        boolean keepGoing;
        for (int step = 0; step < c.length; step++){
            if (c[step].length != 3){
                System.out.println("Command Entry ERROR, possible 1D10T involvement");
                return;
            }
            gyro.reset();
            atAngle = false;
            keepGoing = true;
            stepLength = c[step][0];
            stepSpeed = c[step][1];
            stepAngle = c[step][2];
            System.out.println("Executing Step " + step + "; Time = " + stepLength + ", Speed = " + stepSpeed + ", Angle = " + stepAngle);
            Timer.delay(0.2);
            time.reset();
            time.start();
            while( (keepGoing || (stepLength == 0.0)) && (!atAngle || (stepAngle == 0)) ){
                gyroDriveTurn(stepSpeed, stepAngle);
                keepGoing = time.get() <= stepLength;
            }
            time.stop();
        }
        this.setMotorSpeed(0.0, 0.0);
            
    }
    
    /**
     * Method for entering a series of commands, note that this one can be either time-based or encoder based
     * @param c a list of commands containing time(seconds)/distance(feet), speed(-1.0 to 1.0), and angle(degrees)
     * @param encoderMode whether or not the user wishes to use the encoder rather than the timer
     */
    public void commandList(double[][] c, boolean encoderMode){
        boolean keepGoing;
        for (int step = 0; step < c.length; step++){
            if (c[step].length != 3){
                System.out.println("Command Entry ERROR, possible 1D10T involvement");
                return;
            }
            gyro.reset();
            atAngle = false;
            keepGoing = true;
            stepLength = c[step][0];
            stepSpeed = c[step][1];
            stepAngle = c[step][2];
            System.out.println("Executing Step " + step + "; Time = " + stepLength + ", Speed = " + stepSpeed + ", Angle = " + stepAngle);
            Timer.delay(0.2);
            time.reset();
            time.start();
            while((keepGoing || stepLength == 0.0) && (!atAngle || stepAngle == 0)){
                gyroDriveTurn(stepSpeed, stepAngle);
                if (encoderMode){
                    keepGoing = (Math.abs(left.getDistanceFt()+right.getDistanceFt())/2 <= stepLength);
                } else {
                    keepGoing = time.get() <= stepLength;
                }
            }
            time.stop();
        }
        this.setMotorSpeed(0.0, 0.0);
            
    }
        
}

    

