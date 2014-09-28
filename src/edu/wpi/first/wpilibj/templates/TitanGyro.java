/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Kiran
 */
public class TitanGyro extends Gyro{
    // variables for balance tracking
    public Gyro gyro;
    private final double SENSITIVITY=0.007;//Need to find acutal sensitivity and change
    private double angle;
    private double prevAngle;
    private boolean isBalanced;
    
    /**
     * Constructor that sets sensitivity and uses constructor from WPI gyro class
     */
    public TitanGyro()
    {
        super(HWR.GYRO);
        setSensitivity(SENSITIVITY);
        reset();
    }        
    /**
     * Gets the current reading from the gyro
     * @return angle
     */
    public double angle()
    {
        return getAngle();
    }        
            
   /**
     * Prints data out to SmartDashboard 
     * and auxStick, button 8, resets gyro
     */
    public void run(){
        SmartDashboard.putNumber("Gyro data", gyro.getAngle());
        if(DriverStation.auxStick.getRawButton(HWR.GYRO_RESET)){
            reset();
        }
    
    }
}

