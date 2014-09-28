/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
//import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class TechnoTitan extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Shooter shooter;
    Pneumatics air;
    GearTrain drive;
    PickerUpper pickerUpper;
    DriveEncoder leftEncoder;
    DriveEncoder rightEncoder;
    TitanGyro gyro;
    Timer time;
    Timer time2;
    Timer gyroDriveTime;
    Timer time3;
    Timer time4;
    Vision vision;
//    NetworkTable table;
    
     //autonomous mode
    final int ONE_BALL = 1;
    final int TWO_BALLS = 2;
    int AUTONOMOUS_MODE;
    //low or high goal
    final int LOW_GOAL = 0;
    final int HIGH_GOAL = 1;
    int LOW_OR_HIGH;
    //autonomous cases
    int presentState;
    int nextState;
    final int MOVE_FORWARD=1;
    final int WAIT_FOR_HOT_GOAL=2;
    final int SHOOT=3;
    final int DONE=4;
    final int STOP=5;
    final int BACK_UP = 6;
    final int PICK_UP = 7; //overwritten - functions moved to BACK_UP
    final int MOVE_FORWARD2 = 8;
    final int WAIT_PICK_UP = 9;
    final int STOP_PICK_UP = 10;
    final int SHOOT2 = 11;
    final int LOW_SHOOT = 12;
    final int QUICK_PAUSE = 13;
    final int QUICK_PAUSE_2 = 14;
    //autonomous variables
    double autoDriveSpeed = 0.4;// need to finalize the drive speed 
    double autoDriveDistance = 36;//inches from the tape
    double autoDriveBackDistance;
    double lowGoalDistance;
    double highGoalDistance;
    double backDrive; //distance behind starting point the ball is placed
    final double HOT_WAIT_TIME= 6.0;//seconds
    final double DRIVE_WAIT_TIME= 1.25;
    final double PICK_UP_SPEED = 0.666;
    private final int AUTONOMOUS = 0;
    private final int TELEOP = 1;
    private double pickUpTime;
    private double shooterCooldown;
    private double lowCooldown;
    private boolean enableShooting = false;
    
    private boolean firstLowShoot = true;
    private boolean shootTime = false;
    private boolean BADFirstTime = true;

    // variables for debugging
    int temp;
    String[] cases={"MOVE_FORWARD","WAIT_FOR_HOT_GOAL","SHOOT","DONE","STOP","BACK_UP","PICK_UP","MOVE_FORWARD2","WAIT_PICK_UP","STOP_PICK_UP","SHOOT2", "LOW_SHOOT", "QUICK_PAUSE"};
    //Encoder Turning
    double angle;
    double speed;
    double driveDistance;
    int driveCount;
    
    boolean first = true;
    
    public void robotInit() {

        air = new Pneumatics();
//        table = NetworkTable.getTable(""); //For vision
        

        pickerUpper = new PickerUpper();
        shooter = new Shooter(pickerUpper);
        
        leftEncoder = new DriveEncoder(HWR.LEFT_CHANNEL_A, HWR.LEFT_CHANNEL_B, true);
        rightEncoder = new DriveEncoder(HWR.RIGHT_CHANNEL_A, HWR.RIGHT_CHANNEL_B, true);
        gyro=new TitanGyro();
        drive = new GearTrain(leftEncoder, rightEncoder, gyro);
        vision = new Vision ();
        
        shooter.closeLatch();
        
//        DriverStation.prefDouble("gDriveSpeed", 0.7);
//        DriverStation.prefDouble("gDriveAlpha", 0.1);
//        DriverStation.prefDouble("gDriveCorrection", 0.4);
//        DriverStation.prefDouble("gDriveAngle", 45.0);
//        DriverStation.prefDouble("aThreshold", 1.5);
        
        vision.sendTrackingConstants();
        
//        DriverStation.prefDouble("shootDelay", 0.50);
//        DriverStation.prefDouble("pickerUpperSpeed", .66);
//        DriverStation.prefInt("AutonomousBallNumber",ONE_BALL);
//        DriverStation.prefInt("LowOrHighGoal", HIGH_GOAL);
//        DriverStation.prefDouble("AutoSpeed", 0.3);
//        DriverStation.prefDouble("LowGoalDistance",5.0);
//        DriverStation.prefDouble("HighGoalDistance",24.0);
//        DriverStation.prefDouble("BackUpDistance",3.0);
//        DriverStation.prefDouble("PickUpTime",0.5);
//        DriverStation.prefDouble("ShooterCooldown",2.5);
//        DriverStation.prefDouble("LowCooldown",1.0);
//    
//        DriverStation.prefDouble("reverseChargeDelay", 1.0);
//        DriverStation.prefDouble("ventDelay",1.0);
//        DriverStation.prefDouble("shootingDelay", 1.0);
//        DriverStation.prefDouble("shooterReload", 3.0);
//        DriverStation.prefBoolean("autoTesting", true);
//        DriverStation.prefDouble("testDelay",20.0);
//        DriverStation.prefDouble("latchDelay",0.5);
//        DriverStation.prefDouble("BADDelay", 0.5);
    }
public void autonomousInit () {

    AUTONOMOUS_MODE = DriverStation.getInt("AutonomousBallNumber");
    LOW_OR_HIGH = DriverStation.getInt("LowOrHighGoal");

    vision.sendTrackingConstants();
    
//    AUTONOMOUS_MODE = TWO_BALLS; //testing purposes only
//    LOW_OR_HIGH = LOW_GOAL; //testing purposes only
    air.startCompressor();
    
    leftEncoder.start();
    rightEncoder.start();
    leftEncoder.reset();
    rightEncoder.reset();
    
//    autoDriveSpeed=DriverStation.getDouble("AutoSpeed");
//    lowGoalDistance=DriverStation.getDouble("LowGoalDistance");
//    highGoalDistance=DriverStation.getDouble("HighGoalDistance");
//    backDrive=DriverStation.getDouble("BackUpDistance");
//    pickUpTime=DriverStation.getDouble("PickUpTime");
//    shooterCooldown=DriverStation.getDouble("ShooterCooldown");
//    lowCooldown=DriverStation.getDouble("LowCooldown");
    
    autoDriveSpeed = 0.3;
    lowGoalDistance = 5.0;
    highGoalDistance =174.0;
    backDrive = 9.0;
    pickUpTime = 1.0;
    //shooterCooldown = 1.0; //undetermined
    lowCooldown = 1.0;

    shooter.delay = DriverStation.getDouble("shooterDelay");
    //shooter.reverseChargeDelay = DriverStation.getDouble("reverseChargeDelay"); //should be finalized
    shooter.ventDelay = DriverStation.getDouble("ventDelay");
    shooter.shooterDelay = DriverStation.getDouble("shootingDelay");
    shooter.shooterReload = DriverStation.getDouble("shooterReload");  
    //drive.correction = DriverStation.getDouble("gDriveCorrection");//positive value b/t 0.0 and 1.0
    drive.gAlpha = DriverStation.getDouble("gDriveAlpha");
    drive.angleThreshold = DriverStation.getDouble("aThreshold");
    
    shooter.testMode = DriverStation.getBoolean("autoTesting");
    shooter.latchDelay = DriverStation.getDouble("latchDelay");
    shooter.firstTime = true;
    shooter.shooterFirstTime = true;
    shooter.doTheThing = false;
    if (shooter.testMode)
            //testDelay = 20.0;\
            shooter.testDelay = DriverStation.getDouble("testDelay");
    else if (!shooter.testMode)
            shooter.testDelay = 0;
    
    if (LOW_OR_HIGH==LOW_GOAL)
        autoDriveDistance = lowGoalDistance;
    else
        autoDriveDistance = highGoalDistance;
    
    autoDriveBackDistance = autoDriveDistance + backDrive;
    presentState=MOVE_FORWARD; 
    shooter.resetPresentCase();
    pickerUpper.lowerDown();
    time = new Timer();
    time2 = new Timer();
    time3 = new Timer();
    time4 = new Timer(); //delay between lowering bad and shooting
    time.start();
    time.reset();
    shootTime = false;
    temp=0;// used for debugging and is used to print the state only if it changed.
    gyro.reset();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    //need an if statement to only print if the state has changed
    //need to also print the time when the state changes  
//        if(presentState!=temp){
//         System.out.println("the present state is "+cases[presentState-1] );//for debugging purposes
//         System.out.println(cases[presentState-1]+" started at "+time.get()+" seconds");
//         temp=presentState;
//        }
       SmartDashboard.putBoolean("Retract State", shooter.isRetracted());
       SmartDashboard.putBoolean("Latch State", shooter.isLatched());
       SmartDashboard.putNumber("State", presentState);
       SmartDashboard.putNumber("High Pressure",air.getHighPressure());
//       System.out.println("Case:" + presentState);
       SmartDashboard.putNumber("ShooterState", shooter.presentCase);
       air.checkSolenoids();
        if (shootTime)
            shooter.shootMode(AUTONOMOUS);
     switch(presentState){
         /**
          * Drives forward a distance of autoDriveDistance or until DRIVE_WAIT_TIME seconds have passed.
          */ 
        case MOVE_FORWARD :
        {
            if(((leftEncoder.getDistance()+rightEncoder.getDistance())/2)<highGoalDistance)
            //if (time.get()<DRIVE_WAIT_TIME)
            {
                drive.gyroDrive(autoDriveSpeed);
                nextState=MOVE_FORWARD;
            }
            else
            {
                nextState=STOP;
                leftEncoder.reset();
                rightEncoder.reset();
            }
              
            break;
        }
        /**
         * Stops the robot and changes paths based on the autonomous modes (one/two ball/s and low/high goal).
         */
        case STOP:
        {
            //pickerUpper.lowerDown();
            drive.setMotorSpeed(0.0,0.0);
            if (AUTONOMOUS_MODE == ONE_BALL) { //checks for mode (one ball or two) to choose the next state
                nextState=QUICK_PAUSE;
                time3.start();
                time3.reset();
            }
            else if (LOW_OR_HIGH == LOW_GOAL) {
                nextState = LOW_SHOOT;
            }
            else if (AUTONOMOUS_MODE == TWO_BALLS && LOW_OR_HIGH == HIGH_GOAL) {
                nextState=QUICK_PAUSE;
                time3.start();
                time3.reset();
            }
            break;
        }

        /**
         * A brief pause to allow the ball to settle before any shooting occurs
         */
        case QUICK_PAUSE:
        {
            if (time3.get() > 0.5) {
                if (AUTONOMOUS_MODE == ONE_BALL) {
                    nextState=WAIT_FOR_HOT_GOAL;
                } else if (AUTONOMOUS_MODE == TWO_BALLS){
                    nextState = SHOOT;
                }
                
            } else {
                nextState = QUICK_PAUSE;
            }
            break;
        }
        /**
         * Waits for the goal to turn hot, or the time limit to expire
         */
        case WAIT_FOR_HOT_GOAL:
        {
            drive.setMotorSpeed(0.0,0.0); 
            //hotOrNot() for reflective tape; hotOrNot2() for LED's
            if (time.get()>=HOT_WAIT_TIME || vision.hotOrNot2())
            {
                if (LOW_OR_HIGH == HIGH_GOAL)
                    nextState=SHOOT2;
                else
                    nextState=LOW_SHOOT;
            }
            else
                nextState=WAIT_FOR_HOT_GOAL;
            break;
        }    
        /**
         * Shoots ball and starts running the shootMode program in the background.
         */
        case SHOOT://Used only in 2 Balls - named SHOOT because it happens 1st in 2 Balls
        {
           // accuate the trigger
            shootTime = true; //starts running shooter code in background
            shooter.resetPresentCase();
            time2.reset();
            time2.start();
            while (time2.get()<shooterCooldown) //runs shooter code until shooting is finished
            {
                shooter.shootMode(AUTONOMOUS);
                drive.setMotorSpeed(0.0,0.0);
            }
            time2.stop();
            time2.reset();
            nextState=BACK_UP;
            time.reset();
            break;
        }
        /**
         * Shoots ball and starts running shooter code in background if only one ball is used.
         */
        case SHOOT2: //Used in both 1 Ball and 2 Balls - Called SHOOT2 because it's used 2nd in 2 Balls)
        {
            if (AUTONOMOUS_MODE == TWO_BALLS)
            {
                drive.setMotorSpeed(0.0,0.0);
                shooter.resetPresentCase();
            }
            else
                shootTime = true; //starts running shooter code in background to have shooter prepare for teleop
            shooter.resetPresentCase();
            time2.start();
            while (time2.get()<shooterCooldown) //keeps it in shooting mode until shooter has completed shooting
            {
                shooter.shootMode(AUTONOMOUS);
                drive.setMotorSpeed(0.0,0.0);
            }
            time2.stop();
            nextState = DONE;
            break;
        }
        /**
         * Autonomous is finished - robot stays in this state until autonomous ends.
         */
        case DONE:
        {
            //do nothing since shooter reloads automatically
            drive.setMotorSpeed(0.0,0.0);
            //shooter.retractShooter();
            nextState=DONE;
            leftEncoder.reset();
            rightEncoder.reset();
            gyro.reset();
            break;
        }  
        /**
         * Backs up while running the Ball Acquisition Device (pickerUpper).
         */
        case BACK_UP: //Only used in 2 Balls - backs up to 2nd Ball
        {
            pickerUpper.lowerDown();
            pickerUpper.lPickerUpper.set(-PICK_UP_SPEED);
            if(((Math.abs(leftEncoder.getDistance())+Math.abs(rightEncoder.getDistance()))/2)<autoDriveBackDistance)
            //if (time.get()<DRIVE_WAIT_TIME*2)
            {
                drive.gyroDrive(-autoDriveSpeed);
                nextState=BACK_UP;
            }
            else
            {
                nextState=WAIT_PICK_UP;
                drive.setMotorSpeed(0.0,0.0);
                leftEncoder.reset();
                rightEncoder.reset();
                time.reset();
            }
            break;
        }

        /**
         * Runs the pickerUpper until pickUpTime is complete - pickUpTime should be very small.
         */
        case WAIT_PICK_UP:
        {
            drive.setMotorSpeed(0.0,0.0);
            if (time.get()>=pickUpTime)
                nextState = STOP_PICK_UP;
            else
                nextState = WAIT_PICK_UP;
            break;
        }
        /**
         * Stops the Ball Acquisition Device and raises the pickerUpper.
         */
        case STOP_PICK_UP:
        {
            pickerUpper.lPickerUpper.set(0);
            //pickerUpper.raiseUp();
            leftEncoder.reset();
            rightEncoder.reset();
            //pickerUpper.rPickerUpper.set(0);
            nextState = MOVE_FORWARD2;
            time.reset();
            gyro.reset();
            break;
        }
        /**
         * Moves forward after picking up the ball - Distinguishes between high and low goal to go to proper case.
         */
        case MOVE_FORWARD2: //Only used in 2 balls - 2nd time moving forward
        {
            //if (time.get()<DRIVE_WAIT_TIME)
            if(((Math.abs(leftEncoder.getDistance())+Math.abs(rightEncoder.getDistance()))/2)<36)
            //if (time.get()<DRIVE_WAIT_TIME*2)
            {
                drive.gyroDrive(autoDriveSpeed);
                nextState=MOVE_FORWARD2;
            }
            else 
            {
                if (LOW_OR_HIGH == HIGH_GOAL) {
                    nextState=QUICK_PAUSE_2;
                    time3.reset();
            }
                else
                    nextState = LOW_SHOOT;
            }
            break;
        }
        /**
         * Quick pause for second shot (again to allow ball to settle)
         */
        case QUICK_PAUSE_2:
        {
            if (time3.get() > 1.5) {
                nextState = SHOOT2;
            }
        break;
        }
        /**
         * Shoots the ball to the low goal by rolling the pickerUpper/Ball Acquisition Device backwards.
         * UNTESTED - PREFERRED NOT TO USE.
         */
        case LOW_SHOOT: //Shooting sequence for low goal
        {
            pickerUpper.lowerDown();
            time.reset();
            while (time.get()<lowCooldown) //disables everything else on the robot
            {
                pickerUpper.lPickerUpper.set(-PICK_UP_SPEED);
                drive.setMotorSpeed(0.0,0.0);
            }
            System.out.println("Low Shooting Time: "+time.get());
            pickerUpper.lPickerUpper.set(0.0);
            pickerUpper.raiseUp();
            time.reset();
            if (AUTONOMOUS_MODE==ONE_BALL||!firstLowShoot)
            {
                nextState = DONE;
                //shootTime = true; //used if shooter doesn't work in autonomous but works in teleop
            }
            else
                nextState = BACK_UP;
            firstLowShoot = false;
            break;
        }
        /**
         * Case that occurs if the autonomous state machine is broken.
         */
        default :
        {
            drive.setMotorSpeed(0.0, 0.0);
            System.out.println("Error in autonomous State machine");    
            nextState=DONE;
        }
    }
    presentState=nextState;
    }
    /**
     * Runs at the beginning of Disabled, simply as a safety measure for the latch.
     */
    public void disabledInit () {
        shooter.closeLatch();
        vision.sendTrackingConstants();
    }

    public void teleopInit() {
         vision.sendNormalConstants();
         air.startCompressor();
         angle = DriverStation.getDouble("angleToTurn");
         speed = DriverStation.getDouble("TurnSpeed");
         driveCount = DriverStation.getInt("Count");
         driveDistance = DriverStation.getDouble("Distance");
         shooter.firstTime = true;
         shooter.reverseChargeDelay = DriverStation.getDouble("reverseChargeDelay");
         shooter.closeLatch();
         shooter.BADDelay = DriverStation.getDouble("BADDelay");
         
    }
    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        drive.driveMode(); //geartrain
        //shooter.shootMode(TELEOP);
        pickerUpper.pickerUpperMode();
        SmartDashboard.putBoolean("Retract State", shooter.isRetracted());
        SmartDashboard.putBoolean("Latch State", shooter.isLatched());
        SmartDashboard.putBoolean("Pressure Switch", air.getPressureSwitch());
        SmartDashboard.putNumber("Low Pressure",air.getLowPressure());
        SmartDashboard.putNumber("High Pressure",air.getHighPressure());
        SmartDashboard.putNumber("Analog Channel 5", air.getAnalog5());
        
        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.OPEN_LATCH))
            shooter.openLatch();
        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.CLOSE_LATCH))
            shooter.closeLatch();
        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.RETRACT_SHOOTER))
            shooter.retractShooter();
        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.SHOOT))
            shooter.shoot();
        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.VENT))
            shooter.vent();

        shooter.theThing(shooter.theThingMode(), shooter.getThingLength());
        shooter.teleopShootMode(shooter.enableTeleopShooter());
        air.checkSolenoids();
        
        
        

    }
    /**
     * This function is called once at the beginning of test mode.
     */
    public void testInit(){
        LiveWindow.setEnabled(false);
        air.startCompressor();
        leftEncoder.start();
        rightEncoder.start();
        leftEncoder.reset();
        rightEncoder.reset();
        gyroDriveTime = new Timer();
        gyro.reset();
        
        
    }
    
    /**
     * This function is called periodically during test mode.
     */
    public void testPeriodic() {
        if (DriverStation.leftStick.getRawButton(2)) {
            vision.sendNormalConstants();
        } else {
            vision.sendTrackingConstants();
        }
        
//        //pickerUpper.pickerUpperMode(); //able-izes pickerUpper
//        double gAngle = DriverStation.getDouble("gDriveAngle");
//        double gSpeed = DriverStation.getDouble("gDriveSpeed");
        //drive.correction = DriverStation.getDouble("gDriveCorrection");//positive value b/t 0.0 and 1.0
        drive.gAlpha = DriverStation.getDouble("gDriveAlpha");
        drive.angleThreshold = DriverStation.getDouble("aThreshold");
////        drive.encoderTurn(angle, speed);
        
        //Encoder Tests
       if (DriverStation.antiBounce(HWR.LEFT_JOYSTICK, 10)) {
           while (Math.abs(leftEncoder.get()) < 500 && Math.abs(rightEncoder.getDistanceFt()) < 500) {
               drive.setMotorSpeed(0.4, 0.4);
               SmartDashboard.putNumber("Right Encoder Counts", Math.abs(rightEncoder.get()));
               SmartDashboard.putNumber("Left Encoder Counts", Math.abs(leftEncoder.get()));
           }
           drive.setMotorSpeed(0.0,0.0);
       }
           
       if (DriverStation.antiBounce(HWR.LEFT_JOYSTICK, 11)) {
           while (Math.abs(leftEncoder.getDistanceFt()) < 3 && Math.abs(rightEncoder.getDistanceFt()) < 3) {
               drive.setMotorSpeed(0.4, 0.4-0.05);
               SmartDashboard.putNumber("Left Encoder Distance", Math.abs(leftEncoder.getDistanceFt()));
               SmartDashboard.putNumber("Left Encoder Counts", Math.abs(leftEncoder.get()));
               SmartDashboard.putNumber("Right Encoder Distance", Math.abs(rightEncoder.getDistanceFt()));
               SmartDashboard.putNumber("Right Encoder Counts", Math.abs(rightEncoder.get()));
           }
           drive.setMotorSpeed(0.0,0.0);
       }
       
       
       //Vision Tests
       SmartDashboard.putBoolean("Hot or Not", vision.hotOrNot());
       
       //Gyro Tests
       if (DriverStation.leftStick.getRawButton(9)) {
           drive.gyroDrive(0.5);
       } else {
           drive.setMotorSpeed(0,0);
       }
       SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
       

//       
//       //Drive straight for 5 seconds
//       if (DriverStation.antiBounce(HWR.LEFT_JOYSTICK, 9)) {
//           gyro.reset();
//           gyroDriveTime.reset();
//           gyroDriveTime.start();
//           while (gyroDriveTime.get() <= 5){
//                drive.gyroDrive(0.7);
//                SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           drive.setMotorSpeed(0.0, 0.0);
//           gyroDriveTime.stop();
//       }
//       
//       //Drive straight, then turn to a certain degree while driving, drive straight, stop
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 8)){
//           gyro.reset();
//           gyroDriveTime.reset();
//           gyroDriveTime.start();
//           while (gyroDriveTime.get()<= 2){
//               drive.gyroDrive(gSpeed);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           while (!drive.atAngle){
//               drive.gyroDriveTurn(gSpeed, gAngle);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           gyroDriveTime.stop();
//           gyroDriveTime.reset();
//           gyroDriveTime.start();
//           gyro.reset();
//           while (gyroDriveTime.get()<= 2){
//               drive.gyroDrive(gSpeed);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           drive.setMotorSpeed(0.0, 0.0);
//           gyroDriveTime.stop();
//       }
//       
//       //Drive straight, turn, drive straight, turn back, stop
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 9)){
//           gyro.reset();
//           gyroDriveTime.reset();
//           gyroDriveTime.start();
//           System.out.println("DRIVE FORWARD");
//           while (gyroDriveTime.get()<= 2){
//               drive.gyroDriveTurn(gSpeed, 0);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           System.out.println("TURN");
//           while (!drive.atAngle){
//               drive.gyroDriveTurn(gSpeed, gAngle);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           System.out.println("TURN BACK");
//           gyro.reset();
//           drive.atAngle = false;
//           while (!drive.atAngle){
//               drive.gyroDriveTurn(gSpeed, -gAngle);
//               SmartDashboard.putNumber("DA GYRO ANGEL", gyro.angle());
//           }
//           System.out.println("STAHP");
//           drive.gyroDriveTurn(0.0, 0.0);
//           gyroDriveTime.stop();
//       }
//       
//       //Same as above using different method, each command must be input in format {x, y, z} where x = time in seconds, y = speed, and z = angle
//       //If x = 0, the command will only be fulfilled once the desired angle has been reached, conversely, if z = 0, the command will only be fulfulled once the desired time has passed
//       double[][] straight = {{3.0, gSpeed, 0.0}};
//       double[][] oneTurnR = {{2.0, gSpeed, 0.0}, {0.0, gSpeed, gAngle}};
//       double[][] oneTurnL = {{2.0, gSpeed, 0.0}, {0.0, gSpeed, -gAngle}};
//       double[][] zDrive = {{2.0, gSpeed, 0.0}, {0.0, gSpeed, gAngle},{0.0, gSpeed, -gAngle}};
//       double[][] cwSquare = {{2.0, gSpeed, 0.0}, {0.0, gSpeed, 90.0},{2.0, gSpeed, 0.0},{0.0, gSpeed, 90.0},{2.0, gSpeed, 0.0}, {0.0, gSpeed, 90.0},{2.0, gSpeed, 0.0},{0.0, gSpeed, 90.0}};
//       double[][] ccwSquare = {{0.0, gSpeed, -90.0}, {0.0, gSpeed, -90},{0.0, gSpeed, -90},{0.0, gSpeed, -90.0}};
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 10)){
//           drive.commandList(zDrive);
//       }
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 6)){
//           drive.commandList(cwSquare);
//       }
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 7)){
//           drive.commandList(ccwSquare);
//       }
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 5)){
//           drive.commandList(straight);
//       }
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 4)){
//           drive.commandList(oneTurnR);
//       }
//       if (DriverStation.antiBounce(HWR.RIGHT_JOYSTICK, 3)){
//           drive.commandList(oneTurnL);
//       }
//       
//         //Debugging Tools
//       LiveWindow.setEnabled(false);
//        air.checkSolenoids();
//        SmartDashboard.putBoolean("Pressure Switch", air.getPressureSwitch());
//        SmartDashboard.putNumber("High Pressure",air.getHighPressure());
//        SmartDashboard.putNumber("Low Pressure", air.getLowPressure());
//        SmartDashboard.putNumber("Analog Channel 5", air.getAnalog5());
//        SmartDashboard.putBoolean("Latch State", shooter.isLatched());
//        SmartDashboard.putBoolean("Retract State",shooter.isRetracted());
//        //drive.getCounts();
    }
//        
//        drive.driveMode();
//        pickerUpper.pickerUpperMode();
//        //shooter.shootMode(TELEOP);
//        
//        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.OPEN_LATCH))
//            shooter.openLatch();
//        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.CLOSE_LATCH))
//            shooter.closeLatch();
//        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.RETRACT_SHOOTER))
//            shooter.retractShooter();
//        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.SHOOT))
//            shooter.shoot();
//        if(DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWR.VENT))
//            shooter.vent();
   }

