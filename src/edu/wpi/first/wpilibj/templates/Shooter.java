/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Valerie and Seung-Seok
 */
public class Shooter {
    
    Timer timer;
    Timer theThingTimer;
    Timer latchTimer;
    Timer timer4;
    private final boolean OPEN = true;
    private final boolean CLOSE = false;
    private final boolean PRESSURIZE = true;
    private final boolean VENT = false;
    boolean BADFirstTime = true;
    
    private boolean isBack = false;
    
    private Solenoid rightPistonTop;
    private Solenoid rightPistonBottom;
    private Solenoid leftPistonTop;
    private Solenoid leftPistonBottom;
    private Solenoid latchPiston;
    
    private DigitalInput latch;
    private DigitalInput retract;
    
    public final double REVERSE_CHARGE_TIME = 0.6;
    PickerUpper BAD;
    //private DigitalInput magneticSensor;
   
    //cases
    final int INIT_CASE = 0;
    final int BRING_SHOOTER_DOWN = 1;
    final int VENT_LARGE_PISTONS = 2;
    final int SHOOT = 3;
    final int WAIT_FOR_THROW = 4;
    final int PRESSURIZE_LARGE_PISTONS = 5;
    final int WAIT_TO_SHOOT = 6;
    final int AUTO_PRESSURIZE = 7;
    final int RELATCH = 8;
    final int CLOSE_LATCH= 9;
    
    int lastCase = INIT_CASE;
    int presentCase=INIT_CASE;
    int nextCase=5; //change this to make more sense
    int storedCase = 6;
    //delays
    double delay;
    double reverseChargeDelay;
    double ventDelay;
    double shooterDelay;
    double shooterReload;
    double latchDelay;
    double BADDelay;
    
//    double delay = 0.6;
//    double reverseChargeDelay = 0.6;
//    double ventDelay = 0.7;
//    double shooterDelay = 1.0;
//    double shooterReload = 1.0;
    
    boolean testMode;
    double testDelay;
    String [] shooterCases={"INIT_CASE", "BRING_SHOOTER_DOWN","VENT_LARGE_PISTONS","SHOOT","WAIT_FOR_THROW","PRESSURIZE_LARGE_PISTONS", "WAIT_TO_SHOOT", "AUTO_PRESSURIZE","CLOSE_LATCH"};
    int temp=-1;
            
    boolean shooterFirstTime;
    boolean firstTime;
    boolean doTheThing;
    boolean firstLatch;
    boolean enableShooting;
    boolean latched;
    boolean retracted;
    public Shooter(PickerUpper pUp) {
        rightPistonTop = new Solenoid (HWR.SOLENOID_SLOT_12_VOLT, HWR.RIGHT_PISTON_TOP);
        rightPistonBottom = new Solenoid (HWR.SOLENOID_SLOT_12_VOLT, HWR.RIGHT_PISTON_BOTTOM);
        leftPistonTop = new Solenoid (HWR.SOLENOID_SLOT_12_VOLT, HWR.LEFT_PISTON_TOP);
        leftPistonBottom = new Solenoid (HWR.SOLENOID_SLOT_12_VOLT, HWR.LEFT_PISTON_BOTTOM);
        latchPiston = new Solenoid (HWR.SOLENOID_SLOT_12_VOLT, HWR.LATCH_PISTON);
        
        latch = new DigitalInput(HWR.LATCH_SWITCH);
        retract = new DigitalInput(HWR.RETRACT_SENSOR);
    
        BAD = pUp;
        //magneticSensor = new DigitalInput(HWR.MAGNETIC_PISTON_SENSOR);
        timer = new Timer();  
        theThingTimer = new Timer();
        latchTimer = new Timer();
        timer4 = new Timer(); //BAD delay timer
    }
    /**
     * checks if the shooter is ready to latch
     * latch.get() currently returns false when the shooter is ready to latch
     * @return latched - triggers latch to close if it's true
     */
    public boolean isLatched(){
        latched = !latch.get();
        return latched;
    }
    /**
     * checks if the shooting piston is pressurized (retracted) and ready to vent
     * retract.get() currently returns false when the pistons are retracted
     * @return retracted - triggers the pistons to vent if it's true
     */
    public boolean isRetracted(){
        retracted = !retract.get();
        return retracted;
    }
    /**
     * sets latch piston to false. 
     * if it is set up as a single action piston, pressurizes to closed. 
     * if it is a double action solenoid with only one side hooked up, then leaves the piston free to move.
     */
    public void closeLatch(){
        latchPiston.set(CLOSE);
        //rightPistonTop.set(VENT);
        System.out.println("Closing latch");
    }
    
    /**
     * opens latch piston to allow firing.
     * opens it regardless of single or double action solenoid.
     */
    public void openLatch(){
          latchPiston.set(OPEN);
          System.out.println("Opening latch");
        
    }
    
    /**
     * brings shooter down by extending the pistons on both sides of the shooter.
     */
    public void retractShooter(){
        rightPistonTop.set(VENT);
        rightPistonBottom.set(PRESSURIZE);
        leftPistonTop.set(VENT);
        leftPistonBottom.set((PRESSURIZE));
        System.out.println("retracting shooter");

    }
    
    /**
     * sets all shooter solenoids to false so the shooter is free to move.
     */
    public void vent(){
        rightPistonTop.set(VENT);
        rightPistonBottom.set(VENT);
        leftPistonTop.set(VENT);
        leftPistonBottom.set(VENT);
    }
    
    /**
     * a different name for the shoot method to be less confusing 
     * about retracting the shooter pistons before the latch is released.
     */
    public void pressurizeLargePistons(){
        shoot();
    }
    
    /**
     * retracts both shooter pistons to fire the shooter.
     */
    public void shoot(){
            rightPistonBottom.set(VENT);
            rightPistonTop.set(PRESSURIZE);

            leftPistonTop.set(PRESSURIZE);
            leftPistonBottom.set((VENT));
            System.out.println("Shooting shooter");
    }
    /**
     * resets presentCase to INIT_CASE.
     */
    public void resetPresentCase () {
        presentCase = INIT_CASE;
    }
    /**
     * switches state of variable doTheThing to trigger the method theThing()
     * @return doTheThing - triggers method theThing() if true
     */
    public boolean theThingMode()
    {
         if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.THE_THING) || DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.LONGER_THING))
        {
            if (doTheThing == false)
                doTheThing = true;
            else if(doTheThing == true) 
                doTheThing = false;
            theThingTimer.start();
        }
         return doTheThing;
    }
    /**
     * Checks which length of thing has to happen
     * @return 1 is long, 0 if normal (shorter)
     */
    public int getThingLength () {

        if (DriverStation.auxStick.getRawButton(HWR.THE_THING)) {
            return  0;
        }
        else if (DriverStation.auxStick.getRawButton(HWR.LONGER_THING)) {
            return 1;
        } else {
            return 0;
            //in case something messses up, defaults to short bad
        }
    }
    /**
     * pressurizes the shooter pistons then vents them in preparation for shooting
     * @param run - parameter is fed from theThingMode()
     */
    public void theThing(boolean run, int length){
        
        if (length == 0) {
            reverseChargeDelay = 0.9;
        }
        else if (length == 1) {
            reverseChargeDelay = 1.3; 
        }
        else {
            reverseChargeDelay = 0.9;
        }
        
        SmartDashboard.putNumber("Reverse Charge Delay", reverseChargeDelay);
        if (run)
        {
            //if (theThingTimer.get()<REVERSE_CHARGE_TIME&&firstTime)
            if (firstTime&&(!isRetracted()&&theThingTimer.get()<reverseChargeDelay))
            {
                
                shoot();
                firstTime = false;
                
            }
            //else if (theThingTimer.get()>=REVERSE_CHARGE_TIME)
            else if (isRetracted()||theThingTimer.get()>=reverseChargeDelay)
            {
                vent();
                doTheThing = false;
                theThingTimer.stop();
                theThingTimer.reset();
                firstTime = true;
            }
            
        }
    }
    /**
     * opens and closes the latch after a delay to ensure easier latching
     * CURRENTLY NOT IN USE - latch fixed on the robot so obsolete
     * @param run - value fed from LATCH case
     */
    public void resetLatch(boolean run){
        openLatch();
        if (firstLatch)
        {
            latchTimer.start();
            firstLatch = false;
        }
        if (latchTimer.get()>=latchDelay)
        {
            closeLatch();
            latchTimer.stop();
            latchTimer.reset();
            firstLatch = true;
        }
    }
    /**
     * starts shooter sequence in shootMode for teleop after auto
     * @return true if nothing went wrong
     */
    public boolean enableTeleopShooter(){
        if (DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.BAD_SHOOT))
        {
            if (enableShooting == false) {
                enableShooting = true;
            }
            else if (enableShooting == true) { //this shouldn't happen
                enableShooting = false;
            }
        }
        return enableShooting;
    }
    /**
     * one button shooting (brings down the BAD and then shoots)
     * @param run should be true when activating method
     */
    public void teleopShootMode(boolean run){
        if (run){
            if (BADFirstTime)
            {
                BAD.lowerDown();
                timer4.start();
                BADFirstTime = false;
            }
            else if (!BADFirstTime)
            {
                if (timer4.get()>=BADDelay)
                {
                    openLatch();
                    timer4.stop();
                    timer4.reset();
                    BADFirstTime = true;
                    enableShooting = false;
                }
            }
        }   
    }
    /**
     * state machine to fire the shooter and bring it back down, along with latch piston usage.
     * @param mode - used to determine whether autonomous shooting(0) or teleop shooting(anything else)
     */
    public void shootMode(int mode){
        delay = DriverStation.getDouble("shootDelay");
        //System.out.println("delay for shooterrr" + delay);
        if(presentCase!=temp){
         //System.out.println("the present state in the shooter is "+shooterCases[presentCase] );//for debugging purposes
         //System.out.println(shooterCases[presentCase-1]+" started at "+timer.get()+" seconds");
         temp=presentCase;
        }
        lastCase = presentCase;
        switch(presentCase){
            /**
             * starting case, starts the timer, makes sure the latch allows for the shooter to be locked, 
             * and figures out which case to go to next, based on the mode.
             */
            case INIT_CASE:
                System.out.println("shooterFirstTime at INIT_CASE is: " + shooterFirstTime);
                timer.start();
                closeLatch();
//                if (mode==0&&shooterFirstTime) //0 means autonomous
//                    nextCase = SHOOT;
//                else if (mode!=0)
//                    nextCase = PRESSURIZE_LARGE_PISTONS; //not used at the moment
//                else if (!shooterFirstTime)
//                    nextCase = SHOOT;
                nextCase = SHOOT;
                timer.reset();
                break;
            /**
             * after the delay, pulls the shooter down and resets the timer (shooter will latch with passive latch)
             * @nextCase PRESSURIZE_LARGE_PISTONS
             */    
            case BRING_SHOOTER_DOWN:
                //presentCase = DELAY;
                if(timer.get()>= shooterDelay) //waits for the shooter to finish shooting
                {
                    //closeLatch();
                    retractShooter();
                    nextCase = CLOSE_LATCH;
                    timer.reset();
                }
                break;
                
//            case LATCH:
//                if(timer.get()>= delay)
//                {
//                    closeLatch();
//                
//                    //allows it to go to shooting if it's caught up in this state rather than waiting to vent the piston
//                    if (DriverStation.auxStick.getRawButton(HWR.SHOOT)){ 
//                        nextCase = SHOOT;                       
//                    } 
//                    else{
//                        nextCase = PRESSURIZE_LARGE_PISTONS;
//                    }
//                    timer.reset();
//                }
//                break;
            /**
             * after 2*delay to allow for the pistons fighting the bungee 
             * (and this should not occur in a time sensitive period), 
             * the pistons are pressurized in the firing direction so they are already semi charged 
             * when the gate latch is released. timer is also reset.
             * METHOD OVERWRITTEN - functions moved to AUTO_PRESSURIZE.
             * @nextCase WAIT_TO_SHOOT
             */
            case PRESSURIZE_LARGE_PISTONS: //not in use at the moment
                if(timer.get()>= delay/*||magneticSensor.get()*/)
                {
                    pressurizeLargePistons();
                    nextCase = WAIT_TO_SHOOT;  
                    timer.reset();
                }
                break;
            
            /**
             * waits until the fire button is pressed to proceed to the next case. resets timer before proceeding
             * Autonomous mode stays in this case once the shooting preparations are finished.
             * Autonomous mode uses resetPresentCase() to go back to INIT_CASE to get out of this case.
             * @nextCase SHOOT
             */    
            case WAIT_TO_SHOOT:
                if (DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.SHOOT))
                {
                    nextCase = SHOOT;
                }
                else {
                    nextCase = WAIT_TO_SHOOT;
                }
                timer.reset();
                break;
            /**
             * vents large pistons to get them out of way of shooter. resets timer before proceeding
             * @nextCase WAIT_TO_SHOOT
             */    
            case VENT_LARGE_PISTONS:
                if(isRetracted() || timer.get()>=reverseChargeDelay)
                {
                    vent();
//                    if (shooterFirstTime)
//                        shooterFirstTime = false;
                    nextCase = WAIT_TO_SHOOT;
                    
                    timer.reset();
                    System.out.println("Finishing VENT_LARGE_PISTONS");
                }
                break;
            /**
             * pressurizes large pistons to get them out of way of shooter. resets timer before proceeding
             * @nextCase VENT_LARGE_PISTONS
             */
            case AUTO_PRESSURIZE: //basically the thing (reverseCharge)
                {
                    
                    if (timer.get()>=latchDelay + testDelay)//&&LatchSensor.latched) //waits for shooter to finish reloading and latching
                    { 
                        shoot();
                        nextCase = VENT_LARGE_PISTONS;
                        timer.reset();
                        System.out.println("Current State: AUTO_PRESSURIZE");
                    }
                    else if (timer.get()<latchDelay + testDelay)
                        nextCase = AUTO_PRESSURIZE;
                }
                break;
                /**
             * Opens the latch to fire the shooter. resets the timer before proceeding
             * @nextCase BRING_SHOOTER_DOWN
             */    
            case SHOOT:
                //if(timer.get()>= ventDelay) //want to remove delay if possible
                {
                    //shoot();
                    openLatch();
                    nextCase = BRING_SHOOTER_DOWN;
                    timer.reset();
                }
                break;
            
            /**
             * goes through delay and then allows for a possibility for a sensor checking if the ball has left the robot yet.
             * NOT CURRENTLY USED - function and delay put in BRING_SHOOTER_DOWN
             * @nextCase BRING_SHOOTER_DOWN
             */    
            case WAIT_FOR_THROW: //deprecated - function and delay put in BRING_SHOOTER_DOWN
                if(timer.get()>= delay)
                {
                    nextCase = BRING_SHOOTER_DOWN;
                    closeLatch();
                    timer.reset();
//                    if (true/*ball==released*/) {
//                            nextCase = BRING_SHOOTER_DOWN;
//                            closeLatch();
//                            timer.reset();
//                        }
//                        else {
//                            nextCase = WAIT_FOR_THROW;
//                        }
                }
                break;   
            /**
             * Opens and, after waiting for latch delay, closes latch to help shooter latch properly
             * NOT USED AT THE MOMENT - function split up into BRING_SHOOTER_DOWN and CLOSE_LATCH
             * @nextCase AUTO_PRESSURIZE
             */
            case RELATCH:
                resetLatch(true);
                nextCase = AUTO_PRESSURIZE;
                break;
            /**
             * Closes latch if isLatched() returns true (ready to latch) or after shooterReload has passed
             * @nextCase AUTO_PRESSURIZE
             */    
            case CLOSE_LATCH:
                if (isLatched()||timer.get()>=shooterReload)
                {
                    closeLatch();
                    nextCase = AUTO_PRESSURIZE;
                    timer.reset();
                }
                break;
        } //ends switch
        
        presentCase = nextCase;
    }
}

