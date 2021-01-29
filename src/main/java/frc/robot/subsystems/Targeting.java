// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Targeting extends Subsystem 
{
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    private final int LIMELIGHT_LED_ON = 3;
    private final int LIMELIGHT_LED_OFF = 1;
    private final int LEFT_MOTOR_IND = 0;
    private final int RIGHT_MOTOR_IND = 1;
    private final double TARGET_ACQUIRED = 1.0;
    private final double TARGET_NO_TARGET = 0.0;
    private final double INTEGRAL_WEIGHT = .2;
    private final double CONFIRMED_THRESHOLD = 1.0;
    private final double CONFIRMED_TIME = .25;        // Amount of seconds before it considers a target confirmed
    private final double INTEGRAL_LIMIT = 1000000000; // TODO Check math and get an actual number
    private final double LIMELIGHT_ERROR_MAX = 29.5;
    private final double PERCENT_OUTPUT_LIMIT = .5;
    private final double TIMER_NOT_STARTED_VALUE = 0.0;
    private final double DEFAULT_LAUNCHER_RPM = 1200.0;
    private final double ERROR_INTEGRAL_DEFAULT = 0.0;
    private final double LAST_ERROR_DEFAULT = 0.0;
    
    private double vP = 0.5;
    private double vI = 0.0;
    private double vD = 4.0;

    private NetworkTable limeData;          //Data from limelight
    private NetworkTableEntry tAcquired;    // t stands for target
    private NetworkTableEntry targetX;      // x value of the target
    private NetworkTableEntry targetY;      // y value of the target

    private double errorIntegral = 0.0;
    private double lastError = 0.0;

    private Timer confirmTargetTime;

    private boolean isReadyToFire = false;

    public Targeting() 
    {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        // Get limelight data from network table
        limeData = NetworkTableInstance.getDefault().getTable("limelight");
        tAcquired = limeData.getEntry("tv");
        targetX = limeData.getEntry("tx");
        targetY = limeData.getEntry("ty");
        
        // Set default values for shuffleboard
        limeData.getEntry("camMode").setNumber(0);
        SmartDashboard.putNumber("P Gain", vP);
        SmartDashboard.putNumber("I Gain", vI);
        SmartDashboard.putNumber("D Gain", vD);
    }

    @Override
    public void initDefaultCommand() 
    {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
    }

    @Override
    public void periodic() 
    {
        // Put code here to be run every loop
        // Get updated values from shuffleboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);

        // Set PID if changed through shuffleboard
        if(p != vP) {
            vP = p;
        }
        
        if(i != vI) {
            vI = i;
        }
        
        if(d != vD) {
            vD = d; 
        }
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean getIsReadyToFire()
    {
        return this.isReadyToFire;
    }

    // Turns the LED on or off
    public void controlLight(boolean enabled)
    {
        if(enabled){
            limeData.getEntry("ledMode").setNumber(LIMELIGHT_LED_ON); 
        }
        else{
            limeData.getEntry("ledMode").setNumber(LIMELIGHT_LED_OFF);
        }
    }

    public double[] navToTarget()
    {
        double[] velCmds = {0.0,0.0}; //Left motor velocity, Right motor velocity (returns -1 to 1), At the target for a period of time
        double timerValue = 0.0;

        //Turn the LED on
        controlLight(true);

        //Do we have a target?
        if (tAcquired.getDouble(TARGET_NO_TARGET) == TARGET_ACQUIRED)
        {
            double limeError = targetX.getDouble(0.0); //Get the error of the target X
            double headingError = limeError / LIMELIGHT_ERROR_MAX; // 29.5 is the range of the limelight which goes from -29.5 to 29.5 
            double change = headingError - lastError;

            if(Math.abs(errorIntegral) < INTEGRAL_LIMIT) 
            {
                //Accumulate the error into the integral
                errorIntegral += headingError * INTEGRAL_WEIGHT;
            }
            
            //Calculate percent output to feed to velocity drive
            double percentOutput = (vP * headingError) + (vI * errorIntegral) + (vD * change);
            if(percentOutput > PERCENT_OUTPUT_LIMIT) 
            {
                percentOutput = PERCENT_OUTPUT_LIMIT;
            }
            else if(percentOutput < -PERCENT_OUTPUT_LIMIT) 
            {
                percentOutput = -PERCENT_OUTPUT_LIMIT;
            }
            
            //Set the velocity output
            velCmds[LEFT_MOTOR_IND] = -percentOutput; //Left Motor
            velCmds[RIGHT_MOTOR_IND] = percentOutput; //Right Motor

            //Update shuffleboard
            SmartDashboard.putNumber("LeftOutput",velCmds[LEFT_MOTOR_IND]);
            SmartDashboard.putNumber("RightOutput",velCmds[RIGHT_MOTOR_IND]);

            //Save last error
            lastError = headingError;

            // Checking if target has been in range for a certain amount of time
            if(Math.abs(limeError) < CONFIRMED_THRESHOLD) 
            {
                //Target is within threshold
                //Get current timer value
                timerValue = confirmTargetTime.get();
                
                if(TIMER_NOT_STARTED_VALUE == timerValue )
                {
                    //Start timer
                    confirmTargetTime.start();
                }
                else if( CONFIRMED_TIME == timerValue )
                {
                    //Ready to fire!
                    isReadyToFire = true;
                }
            }
            else
            {
                //Target is NOT within threshold
                isReadyToFire = false;
                confirmTargetTime.stop();
                confirmTargetTime.reset();
            }
        }
        
        return velCmds;
    }

    public double calcShooterRPM()
    {
        double retval = 0.0;
        if(tAcquired.getDouble(0.0) == TARGET_ACQUIRED)
        {
            //This is where we would calculate the distance
            retval = DEFAULT_LAUNCHER_RPM;
        }

        return retval;
    }

    public void resetPID()
    {
        errorIntegral = ERROR_INTEGRAL_DEFAULT;
        lastError = LAST_ERROR_DEFAULT;
    }
}
