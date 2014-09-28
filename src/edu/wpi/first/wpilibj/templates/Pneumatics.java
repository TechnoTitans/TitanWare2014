/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SolenoidBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  Used for Air Control
 * @author Rohan Doshi, Priunsh Nagru 2013
 */
public class Pneumatics {
    // DEFINING THE COMPRESSOR
    private volatile Compressor comp;
    
    private final double LOW_MAX_VOLTAGE = 4.5; //in volts
    private final double LOW_MIN_VOLTAGE = 0.5; //in volts
    private final double LOW_MAX_PRESSURE = 200; //in psi
    private final double HIGH_MAX_VOLTAGE = 4.5; //in volts
    private final double HIGH_MIN_VOLTAGE = 0.5; //in volts
    private final double HIGH_MAX_PRESSURE = 200; //in psi
    private final int AVERAGE_BITS = 10;
    private final double ANALOG_VOLT_VARIANCE = 0.5;
    private final double ANALOG_VOLT_GOAL = 5.0;
    //private final double PRESSURE_ISSUE = 0.0;
    
    private double lowVoltRange = LOW_MAX_VOLTAGE-LOW_MIN_VOLTAGE;
    private double lowPressureSlope = LOW_MAX_PRESSURE/lowVoltRange;
    private double highVoltRange = HIGH_MAX_VOLTAGE-HIGH_MIN_VOLTAGE;
    private double highPressureSlope = HIGH_MAX_PRESSURE/highVoltRange;
    private double averageVoltage;
    private double pressure;
    private double analog5;
        
    private AnalogChannel lowPressureSensor;
    private AnalogChannel highPressureSensor;
    private AnalogChannel AnalogChannel5;
    
    boolean[] solenoid12 = {false,false,false,false,false,false,false,false};
    boolean[] solenoid24 = {false,false,false,false,false,false,false,false};
    
    public boolean isThingDone = false;
    
    public Pneumatics(){
        // SOLENOID DECLARATIONS
        comp = new Compressor(HWR.COMPRESSOR_PRESSURE_SWITCH, HWR.COMPRESSOR_RELAY);
        lowPressureSensor = new AnalogChannel(HWR.LOW_PRESSURE_SENSOR);
        lowPressureSensor.setAverageBits(AVERAGE_BITS);
        highPressureSensor = new AnalogChannel(HWR.HIGH_PRESSURE_SENSOR);
        highPressureSensor.setAverageBits(AVERAGE_BITS);
        AnalogChannel5 = new AnalogChannel(HWR.ANALOG_CHANNEL_5);
        
        
    }    
   /**
    * The code never actually uses it
    * It, however, must be in the code so the dashboard can see it
    * In other words, don't worry about this method
    * @return 
    */
    public boolean getPressureSwitch() {
        return comp.getPressureSwitchValue();
    }
           
    /**
     * Start the Compressor
     */
    public void startCompressor() {
        comp.start();
        System.out.println("Compressor Started");
    }
    
    /**
     * Calculates the pressure of the low pressure tubing
     * @return pressure in the low pressure tubing
     */
    public double getLowPressure() {
        averageVoltage = lowPressureSensor.getAverageVoltage();
        if (averageVoltage < LOW_MIN_VOLTAGE)
        {
            SmartDashboard.putBoolean("LowPressureTransducerError", true);
            pressure = 0;
            
        }
        else if (averageVoltage > LOW_MAX_VOLTAGE)
        {
            SmartDashboard.putBoolean("LowPressureTransducerError", true);
            pressure = LOW_MAX_PRESSURE;
        }
        else
        {
            SmartDashboard.putBoolean("LowPressureTransducerError", false);
            pressure = lowPressureSlope*(averageVoltage-LOW_MIN_VOLTAGE);
        }
        return pressure;
    }
    /**
     * Calculates the pressure of the high pressure tubing
     * @return pressure in the high pressure tubing
     */
    public double getHighPressure() {
        averageVoltage = highPressureSensor.getAverageVoltage();
        if (averageVoltage < HIGH_MIN_VOLTAGE)
        {
            SmartDashboard.putBoolean("HighPressureTransducerError", true);
            pressure = 0;
            
        }
        else if (averageVoltage > HIGH_MAX_VOLTAGE)
        {
            SmartDashboard.putBoolean("HighPressureTransducerError", true);
            pressure = HIGH_MAX_PRESSURE;
        }
        else
        {
            SmartDashboard.putBoolean("HighPressureTransducerError", false);
            pressure = highPressureSlope*(averageVoltage-HIGH_MIN_VOLTAGE);
        }
        return pressure;
    }
    
    public double getAnalog5() {
        //return AnalogChannel5.getValue();
        analog5 = AnalogChannel5.getAverageVoltage();
        if (analog5>ANALOG_VOLT_GOAL+ANALOG_VOLT_VARIANCE || analog5<ANALOG_VOLT_GOAL-ANALOG_VOLT_VARIANCE)
        {
            SmartDashboard.putBoolean("AnalogError", true);
        }
        else if (analog5<ANALOG_VOLT_GOAL+ANALOG_VOLT_VARIANCE && analog5>ANALOG_VOLT_GOAL-ANALOG_VOLT_VARIANCE)
        {
            SmartDashboard.putBoolean("AnalogError", false);
        }
        return analog5;
    }
    /**
     * Reads and determines the state of all solenoids on all modules and prints to the SmartDashboard
     * Remember to change the slots you are looking for and name of the solenoids from the past year
     * @author Sarang
     */
    public void checkSolenoids () {
        int Module_12Volt = SolenoidBase.getAllFromModule(HWR.SOLENOID_SLOT_12_VOLT);
        int Module_24Volt = SolenoidBase.getAllFromModule(HWR.SOLENOID_SLOT_24_VOLT);
        
        for(int i=0; i< 8 ; i++) {
            //The & is a bitwise operator
            //12 Volt
            if ((Module_12Volt & HWR.bitmaskArray[i]) > 0) { //the value of that solenoid is true
                solenoid12[i] = true;
            } else {
                solenoid12[i] = false;
            }
            
            //24 Volt
            if ((Module_24Volt & HWR.bitmaskArray[i]) > 0) { //the value of that solenoid is true
                solenoid24[i] = true;
            } else {
                solenoid24[i] = false;
            }
        }
        
        if (solenoid12[4] && solenoid12[3] && !solenoid12[0] && !solenoid12[1]) { //checks if shooter is trying to lower 
            isThingDone = false;
        } else if (!solenoid12[4] && !solenoid12[3]&& !solenoid12[0] && !solenoid12[1]) { //shooter is ready
            isThingDone = true;
        }
        
        SmartDashboard.putBoolean("Ready to Shoot", isThingDone);
        
        SmartDashboard.putBoolean("Top Left Piston", solenoid12[0]);
        SmartDashboard.putBoolean("Top Right Piston", solenoid12[1]);
        SmartDashboard.putBoolean("Bottom Left Piston", solenoid12[4]);
        SmartDashboard.putBoolean("Bottom Right Piston", solenoid12[3]);
        SmartDashboard.putBoolean("Latch Piston", solenoid12[2]);
        
        SmartDashboard.putBoolean("Back BAD Piston", solenoid24[0]);
        SmartDashboard.putBoolean("Top BAD Piston", solenoid24[1]);
    }
    
}
    
   


