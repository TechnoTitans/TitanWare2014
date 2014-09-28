/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 * Storage of all physical variables that connects to HWP.java
 * @author Sarang
 */
public class HWR {
    
    //Motors
    //Reset the PWM Values to whatever you need
   public static final int LEFT_MOTOR               = HWP.PWM_1;
   public static final int RIGHT_MOTOR              = HWP.PWM_2;
   public static final int LEFT_PICKERUPPER_MOTOR   = HWP.PWM_3;
   public static final int RIGHT_PICKERUPPER_MOTOR  = HWP.PWM_4;
    
    //Joystick
    public static final int LEFT_JOYSTICK               = HWP.JOY_1;
    public static final int RIGHT_JOYSTICK              = HWP.JOY_2;
    public static final int AUX_JOYSTICK                = HWP.JOY_3;
    

    
    //Buttons
    public static final int LONGER_THING                  = HWP.BUTTON_9;
    public static final int LOWER_DOWN                  = HWP.BUTTON_11;
    public static final int STOP_PICKERUPPER            = HWP.BUTTON_9;
    public static final int OPEN_LATCH                  = HWP.BUTTON_2;
    public static final int CLOSE_LATCH                 = HWP.BUTTON_3;
    public static final int RETRACT_SHOOTER             = HWP.BUTTON_4;
    public static final int SHOOT                       = HWP.BUTTON_7;
    public static final int THE_THING                   = HWP.BUTTON_8;
    public static final int VENT                        = HWP.BUTTON_5;
    public static final int GYRO_RESET                  = HWP.BUTTON_8;
    public static final int INTAKE_BALL                 = HWP.BUTTON_4;
    public static final int RELEASE_BALL                = HWP.BUTTON_5;
    public static final int GYRO_DRIVE_ACTIVATE         = HWP.BUTTON_3;
    public static final int BAD_SHOOT                   = HWP.BUTTON_11;
    public static final int KEEP_BAD_LOWERED            = HWP.BUTTON_3;
  
    
    //Solenoid module 1 (12v)
    public static final int SOLENOID_SLOT_12_VOLT           = HWP.SOLENOID_MODULE_2;
    public static final int SOLENOID_INDEX                  = 31;
    public static final int LATCH_PISTON                    = HWP.SOLENOID_3;
    public static final int RIGHT_PISTON_TOP                = HWP.SOLENOID_2;
    public static final int LEFT_PISTON_TOP                 = HWP.SOLENOID_1;
    public static final int RIGHT_PISTON_BOTTOM             = HWP.SOLENOID_4;
    public static final int LEFT_PISTON_BOTTOM              = HWP.SOLENOID_5;
    
    //Solenoid module 2 (24v)
    public static final int SOLENOID_SLOT_24_VOLT           = HWP.SOLENOID_MODULE_1; 
    public static final int SOLENOID_24_INDEX               = 3;
    public static final int BACK_PICKER_UPPER_PISTON        = HWP.SOLENOID_1;
    public static final int FRONT_PICKER_UPPER_PISTON       = HWP.SOLENOID_2;
    
    
    //Compressors
    public static final int  COMPRESSOR_PRESSURE_SWITCH = HWP.GPIO_1;
    public static final int  COMPRESSOR_RELAY           = HWP.RELAY_8;
    
    //Sensors
    public static final int  BATTERY_VOLTAGE            = HWP.ANALOG_1;
    public static final int  GYRO                       = HWP.ANALOG_2;
    public static final int  LOW_PRESSURE_SENSOR        = HWP.ANALOG_3;
    public static final int  HIGH_PRESSURE_SENSOR       = HWP.ANALOG_4;
    public static final int  ANALOG_CHANNEL_5           = HWP.ANALOG_5;
    public static final int  LEFT_CHANNEL_A             = HWP.GPIO_2;//encoder
    public static final int  LEFT_CHANNEL_B             = HWP.GPIO_3;//encoder
    public static final int  RIGHT_CHANNEL_A            = HWP.GPIO_4;//encoder
    public static final int  RIGHT_CHANNEL_B            = HWP.GPIO_5;//encoder
    public static final int  MAGNETIC_PISTON_SENSOR     = HWP.GPIO_6;
    public static final int  LATCH_SWITCH               = HWP.GPIO_7; 
    public static final int  RETRACT_SENSOR             = HWP.GPIO_8;
    //Servos
    
    //Photogate
    
    //Utility
    public static int[] bitmaskArray                     = {HWP.FLAG_1,HWP.FLAG_2,HWP.FLAG_3, HWP.FLAG_4, 
                                                            HWP.FLAG_5, HWP.FLAG_6, HWP.FLAG_7, 
                                                            HWP.FLAG_8};
}
