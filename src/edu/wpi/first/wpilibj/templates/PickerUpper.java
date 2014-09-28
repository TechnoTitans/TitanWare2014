/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * Controls the BAD (ball acquisition device)
 * @author Val 
 */


public class PickerUpper {
   private final boolean EXTEND = true;
   private final boolean RETRACT = false;
   static boolean isPickerUpperExtended;
   Talon lPickerUpper;
   private Solenoid backPickerUpperDeploy;
   private Solenoid frontPickerUpperDeploy;
   double speed;// = 0.7;
   
   public PickerUpper(){
        
        lPickerUpper = new Talon(HWR.LEFT_PICKERUPPER_MOTOR);

        backPickerUpperDeploy = new Solenoid(HWR.SOLENOID_SLOT_24_VOLT,HWR.BACK_PICKER_UPPER_PISTON);
        frontPickerUpperDeploy = new Solenoid(HWR.SOLENOID_SLOT_24_VOLT,HWR.FRONT_PICKER_UPPER_PISTON);

   }
   
   /**
    * sets picker upper to values set by the aux joystick y-axis, 
    * and sets pistons to raise up/down if the corresponding joystick buttons are pressed.
    * also allows (possibly only for testing purposes) to control running the roller in the 
    * intake direction, the expel direction, and stopping through buttons with a set speed.
    */
   public void pickerUpperMode()
   {
       speed = DriverStation.getDouble("pickerUpperSpeed");
       //sets pickerUpper to speed of aux joystick. for testing purposes.
       speed = (1+DriverStation.auxStick.getRawAxis(DriverStation.kZAxis))/2; //0.0 to 1
       speed = (speed + 1)/2.5; //0.4 to 0.8
       SmartDashboard.putNumber("BAD Speed", speed);
       if (DriverStation.auxStick.getRawAxis(DriverStation.kYAxis) > 0.2) {
           lPickerUpper.set(speed);
       } else if (DriverStation.auxStick.getRawAxis(DriverStation.kYAxis) < -0.2) {
           lPickerUpper.set(-speed);
       } else {
           lPickerUpper.set(0);
       }

       if (DriverStation.antiBounce(HWR.LEFT_JOYSTICK, 1))  { //joystick
           if (isPickerUpperExtended) {
               raiseUp();
           } else if (!isPickerUpperExtended) {
               lowerDown();
           }
       }
               
               

       
   }
   /**
    * Raises the pickerUpper by pressurizing the front solenoid and venting the back solenoid, retracting the whole piston.
    */
   public void raiseUp()
   {
       //pickerUpperDeploy.set(RETRACT);
       frontPickerUpperDeploy.set(EXTEND);
       backPickerUpperDeploy.set(RETRACT);
       isPickerUpperExtended = false;
   }
   /**
    * Lowers the pickerUpper by venting the front solenoid and pressurizing the back solenoid, extending the whole piston.
    */
   public void lowerDown()
   {
       //pickerUpperDeploy.set(EXTEND);
       frontPickerUpperDeploy.set(RETRACT);
       backPickerUpperDeploy.set(EXTEND);
       isPickerUpperExtended = true;

   }
   /**
    * checks if the intake ball button (to roll roller forwards) is pressed (uses antibounce)
    * @return whether button is pressed or not
    */
   public boolean isIntake()
   {
       return DriverStation.antiBounce(HWR.LEFT_JOYSTICK,HWR.INTAKE_BALL);
      
   }
   
   /**
    * checks if the release ball button (to roll roller backwards) is pressed (uses antibounce)
    * @return whether button is pressed or not
    */
   public boolean isRelease()
   {
       return DriverStation.antiBounce(HWR.LEFT_JOYSTICK,HWR.RELEASE_BALL);
   }
   
   /**
    * checks if the stop pickerUpper button is pressed (uses antibounce)
    * @return whether button is pressed or not
    */
   public boolean isStopPickerUpper()
   {
       return DriverStation.antiBounce(HWR.LEFT_JOYSTICK,HWR.STOP_PICKERUPPER);
   }

}

