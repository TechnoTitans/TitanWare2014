/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 *
 * @author David
 */
public class Vision {
    NetworkTable table;
    public boolean hot;
    public String hotError;
    
    //hotOrNot() and test() variables
//    public final static boolean HOT = true;
//    public final static boolean COLD = false;
    private final static double HOT_RATIO = 5.875;
    private final static double TEST_RATIO = 2.6883116883116883116883116883117;
    private final static double THRESHOLD = 3;
    public double height;
    public double width;
    public double blob1;
    public double blob2;
    
    //hotOrNot2() variables
    // Dots = LED's
    public static final double DOT_THRESHOLD = 60; // May need adjusting. Goal is made of 90 LED's.
    public double dots;
    
    
    //CAMERA TRACKING CONSTANTS
    public static final double  LOW_EXPOSURE = 0;
    public static final double LOW_BRIGHTNESS= 30;
    public static final double HIGH_CONTRAST = 100;

    
    //NORMAL VIEWING CONSTANTS
    public static final double NORMAL_CONTRAST = 50;
    public static final double NORMAL_BRIGHTNESS = 50;
    public static final double NORMAL_EXPOSURE = 50;

    
    //    private final double CAMERA_FOV_HORIZONTAL_ANGLE = 67; //in degrees
    //    public final static boolean BLUEBALL = true;
    //    public final static boolean REDBALL = false;
    
    public Vision(){
        table = NetworkTable.getTable("TitanTable");

    }
    
    public  boolean hotOrNot(){
        // New code copied from test
        try {
            final NumberArray blobHeight = new NumberArray();
            final NumberArray blobWidth = new NumberArray();
            table.retrieveValue("HEIGHT",blobHeight);
            table.retrieveValue("WIDTH",blobWidth);
            
            if (blobHeight.size()>0 && blobWidth.size()>0){
                
//                System.out.println("Height: " + blobHeight.get(0) + " " + blobHeight.get(1));
//                System.out.println("Width: " + blobWidth.get(0) + " " + blobWidth.get(1));
                hot = (Math.abs(blobWidth.get(0)/blobHeight.get(0)-HOT_RATIO)<THRESHOLD) || (Math.abs(blobWidth.get(1)/blobHeight.get(1)-HOT_RATIO)<THRESHOLD);
                blob1 = blobWidth.get(0)/blobHeight.get(0);
                blob2 = blobWidth.get(1)/blobHeight.get(1);
//                System.out.println("Blob1 Ratio: " + blob1);
//                System.out.println("Blob2 Ratio: " + blob2);
//                System.out.println("HotOrNot: " + hot);
                SmartDashboard.putNumber("Blob1 Ratio",blob1);
                SmartDashboard.putNumber("Blob2 Ratio",blob2);
                SmartDashboard.putBoolean("Is Hot?",hot);
                return hot;
            }
            else
                return false;
        } catch (TableKeyNotDefinedException exp) {
            hotError = "TableKeyNotDefined";
            return false;
        } catch (ArrayIndexOutOfBoundsException exp){
            hotError = "ArrayIndexOutOfBounds";
            SmartDashboard.putString("HotError", hotError);
            return false;
        }
        // Copied over from test()
//         try {
//            height = table.getNumber("HEIGHT", 1);
//            width = table.getNumber("WIDTH", 1);
//            System.out.println("Height: " + height);
//            System.out.println("Width: " + width);
//            // TODO: adjust threshold
//            return Math.abs((width/height)-HOT_RATIO) < THRESHOLD; // Dynamic vision target is horizontal
//        } catch (TableKeyNotDefinedException exp){
//            System.out.println("Exception caught");
////            dimensions[0] = 0;
////            dimensions[1] = 0;
//            return false;
//        }
        
        // Old code, doesnt work
//        try {
//            if (table.getNumber("COG_X") > 0){ //Hopefully this works?
//                return HOT;
//            }
//        } catch (TableKeyNotDefinedException exp){
//            System.out.println("Exception caught");
//            return COLD;
//        }
//        return COLD;
        
    }
    
    public boolean hotOrNot2(){
        try {
            dots = table.getNumber("BLOB_COUNT");
            if (dots > DOT_THRESHOLD) {
                hot = true;
            }
            else {
                hot = false;
            }
        } catch (TableKeyNotDefinedException exp) {
            hotError = "TableKeyNotDefined";
            hot = false;
        } catch (ArrayIndexOutOfBoundsException exp){
            hotError = "ArrayIndexOutOfBounds";
            SmartDashboard.putString("HotError", hotError);
            hot = false;
        }
        return hot;
    }
    
    /**
     *
     * @return
     */
    public boolean test(){
        try {
            NumberArray blobHeight = new NumberArray();
            NumberArray blobWidth = new NumberArray();
            table.retrieveValue("HEIGHT",blobHeight);
            table.retrieveValue("WIDTH",blobWidth);
            
            if (blobHeight.size()>0 && blobWidth.size()>0){
                System.out.println("Height: " + blobHeight.get(0) + " " + blobHeight.get(1));
                System.out.println("Width: " + blobWidth.get(0) + " " + blobWidth.get(1));
                return (Math.abs(blobWidth.get(0)/blobHeight.get(0)-HOT_RATIO)<THRESHOLD) || (Math.abs(blobWidth.get(1)/blobHeight.get(1)-HOT_RATIO)<THRESHOLD);
            }
            else
                return false;
        } catch (TableKeyNotDefinedException exp) {
            System.out.println("Exception caught.");
            return false;
        }
        
//        try {
//            height = table.getNumber("HEIGHT", 0); //Hopefully this works?
//            width = table.getNumber("WIDTH", 0);
//            System.out.println("Height: " + height);
//            System.out.println("Width: " + width);
//            return Math.abs((height/width)-TEST_RATIO);
//        } catch (TableKeyNotDefinedException exp){
//            System.out.println("Exception caught");
////            dimensions[0] = 0;
////            dimensions[1] = 0;
//            return -1;
//        }
////        return 0;
        
        // old

//        return 0;

        
//        NumberArray values = new NumberArray();
//        try {
//            table.retrieveValue("COG_X", values);
//            return values.get(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//        Timer.delay(0.1);
    }
    
//    public double ballDistance()
//    {
//        
//    }
//    
//    public boolean ballColor()
//    {
//        
//    }
//    
//    public double[] offCenterness()
//    {
//        
//    }
    public void sendTrackingConstants () {
        table.putNumber("BRIGHTNESS", LOW_BRIGHTNESS);//30
        table.putNumber("EXPOSURE", LOW_EXPOSURE);//0
        table.putNumber("CONTRAST", HIGH_CONTRAST);//100

    }
    
    public void sendNormalConstants () {
        table.putNumber("BRIGHTNESS", NORMAL_BRIGHTNESS);//50
        table.putNumber("EXPOSURE", NORMAL_EXPOSURE);//50
        table.putNumber("CONTRAST", NORMAL_CONTRAST);//50

    }
    

    
}