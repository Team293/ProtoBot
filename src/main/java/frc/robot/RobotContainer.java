// RobotBuilder Version: 3.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot;

import frc.robot.commands.*;
import frc.robot.commands.AutonomousCommand.AutoStartPosition;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.classes.Kinematics;
import frc.robot.classes.Position2D;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer 
{
    private static RobotContainer m_robotContainer = new RobotContainer();

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    // The robot's subsystems
    public final Climb m_climb = new Climb();
    public final Targeting m_targeting = new Targeting();
    public final Feeder m_feeder = new Feeder();
    public final BallPickup m_ballPickup = new BallPickup(m_feeder);
    public final Launcher m_launcher = new Launcher();
    public final LauncherController m_launcherController = new LauncherController(m_launcher, m_targeting);
    public final Kinematics m_kinematics = new Kinematics(new Position2D(0.0,0.0,0.0));
    public final Drivetrain m_drivetrain = new Drivetrain(m_kinematics);

    // Joysticks/Controllers
    private final XboxController driveController = new XboxController(4);
    private final XboxController operatorController = new XboxController(5);
    /*private final Joystick operatorRightJoy = new Joystick(2);
    private final Joystick operatorLeftJoy = new Joystick(3);
    private final Joystick rightJoy = new Joystick(1);
    private final Joystick leftJoy = new Joystick(0);*/
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
  
    // A chooser for autonomous commands
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
    */
    private RobotContainer() 
    {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD
        // Smartdashboard Subsystems

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD
        // Configure the button bindings
        configureButtonBindings();
        // Configure default commands
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND

        // Configure autonomous sendable chooser
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        m_chooser.setDefaultOption("Autonomous Command", new AutonomousCommand(m_drivetrain, m_kinematics, AutoStartPosition.RIGHT, m_feeder, m_launcher, m_ballPickup));
        m_chooser.addOption("Right", new AutonomousCommand(m_drivetrain, m_kinematics, AutoStartPosition.RIGHT, m_feeder, m_launcher, m_ballPickup));
        m_chooser.addOption("Middle", new AutonomousCommand(m_drivetrain, m_kinematics, AutoStartPosition.MIDDLE, m_feeder, m_launcher, m_ballPickup));
        m_chooser.addOption("Left", new AutonomousCommand(m_drivetrain, m_kinematics, AutoStartPosition.LEFT, m_feeder, m_launcher, m_ballPickup));

        //Setting default command for drivetrain as VelocityDrive
        m_drivetrain.setDefaultCommand(new ArcadeDrive( m_drivetrain, driveController));

        SmartDashboard.putData("Auto Mode", m_chooser);
    }

    public static RobotContainer getInstance() 
    {
        return m_robotContainer;
    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() 
    {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
        // Create some buttons
        
        //Toggle Green Zone RPMs 
        /*final JoystickButton greenZoneBtn = new JoystickButton(driveController, XboxController.Button.kX.value);
        greenZoneBtn.whenPressed(new SetGreenZone(m_launcherController));*/

        //Toggle Yellow Zone RPMs 
        /*final JoystickButton yellowZoneBtn = new JoystickButton(driveController, XboxController.Button.kA.value);
        yellowZoneBtn.whenPressed(new SetYellowZone(m_launcherController));*/
        
        //Toggle Blue Zone RPMs 
        /*final JoystickButton blueZoneBtn = new JoystickButton(driveController, XboxController.Button.kB.value);
        blueZoneBtn.whenPressed(new SetBlueZone(m_launcherController));*/
        
        //Toggle Red Zone RPMs 
        /*final JoystickButton redZoneBtn = new JoystickButton(driveController, XboxController.Button.kY.value);
        redZoneBtn.whenPressed(new SetRedZone(m_launcherController));*/

        SmartDashboard.putData("Reset Kinematics", new ResetKinematics(m_drivetrain));

        // operatorController Button Mapping

        //Shoot Mapping + Auto Aim
        final JoystickButton xboxFireBtn = new JoystickButton(operatorController, XboxController.Button.kRightBumper.value); 
        
        xboxFireBtn.whileHeld(new Fire(m_feeder, m_launcher, m_launcherController));

        //final JoystickButton xboxTargetBtn = new JoystickButton(driveController, XboxController.Button.kBumperLeft.value);
        //xboxTargetBtn.whileHeld(new LocateTarget(m_drivetrain, m_targeting));
         
        //Wheel Spin Up Mapping
        final JoystickButton blueZoneBtn = new JoystickButton(operatorController, XboxController.Button.kY.value);
        blueZoneBtn.whenPressed(new SetBlueZone(m_launcherController));

        final JoystickButton greenZoneBtn = new JoystickButton(operatorController, XboxController.Button.kB.value);
        greenZoneBtn.whenPressed(new SetGreenZone(m_launcherController));

        
        //Pickup Feeder Mapping
         //Raise the launcher piston
         final POVButton dpadUpButton = new POVButton(operatorController, 0);
         dpadUpButton.whenPressed(new ExtendClimber(m_climb));
 
         //Lower the launcher piston
         final POVButton dpadDownButton = new POVButton(operatorController, 180);
         dpadDownButton.whenPressed(new RetractClimber(m_climb));

        //toggle BallPickup and Gecko
        final JoystickButton toggleGeckoBtn = new JoystickButton(operatorController, XboxController.Button.kA.value);        
        toggleGeckoBtn.whenPressed(new ToggleGecko( m_ballPickup), true);
        SmartDashboard.putData("toggleGeckoBtn",new ToggleGecko( m_ballPickup ) );

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

    public XboxController getxboxController() 
    {
      return driveController;
    }
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() 
    {
        // The selected command will be run in autonomous
        return m_chooser.getSelected();
    }

    public Command getTeleopCommand()
    {
        return new ArcadeDrive(m_drivetrain, driveController);
    }
}
