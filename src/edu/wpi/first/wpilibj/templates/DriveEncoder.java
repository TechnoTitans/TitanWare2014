/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Encoder;
/**
 * Class to handle the receiving and interpreting data from the encoders
 * @author Kiran
 */
public class DriveEncoder extends Encoder{
    //private final double TURNING_WHEEL_DISTANCE_PER_PULSE = 0.01840659135;
    private final double WHEEL_DISTANCE_PER_PULSE=47.0/700.0;//inches/pusle
    //Above value has been confirmed from past revisions as the constant for the robot not the mule
    //may require retuning on 2nd day of Competition.
    /**
     * Constructor
     * @param aChannel
     * @param bChannel
     * @param reverseDirection 
     */
    public DriveEncoder(int aChannel, int bChannel, boolean reverseDirection){
            super(aChannel, bChannel, reverseDirection);
    }
    /**
     * Gets distance from encoder in terms of inches
     * @return distance in inches
     */
    public double getDistance () {
        
        this.setDistancePerPulse(WHEEL_DISTANCE_PER_PULSE); // Need to figure out DISTANCE_PER_PULSE
        return Math.abs(super.getDistance()); 
    }
    /**
     * Gets distance from encoder in terms of feet
     * @return distance in feet
     */
     public double getDistanceFt(){
        return getDistance()/12;
    }
     /**
      * Gets distance from encoder in terms of meters
      * @return distance in meters
      */
    // converts distance from feet to meters. 
    public double getDistanceMeters(){
        return getDistanceFt()*0.3048;
}
}