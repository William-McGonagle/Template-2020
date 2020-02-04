package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Mecanum2020", group="TeleOp")
public class Mechybois2020 extends OpMode {

    // Global Variables
    static final double TICKS_PER_ROTATION = 1120.0 * 0.75;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    //drive
    private DcMotor lf = null;
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;

    //arm
    private DcMotor extend = null;
    private Servo grab = null;
    private DcMotor rotateL = null;
    private DcMotor rotateR = null;

        // Arm Safety System
        public boolean armRaised = false;
        public boolean firstTick = false;
        public float armRotationTolerance = 10;

    //movePlatform
    private Servo movePlat1 = null;
    private Servo movePlat2 = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        lf = hardwareMap.get(DcMotor.class, "lf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rf = hardwareMap.get(DcMotor.class, "rf");
        rb = hardwareMap.get(DcMotor.class, "rb");

        extend = hardwareMap.get(DcMotor.class, "extend");
        grab = hardwareMap.get(Servo.class, "grab");
        rotateL = hardwareMap.get(DcMotor.class, "rotateL");
        rotateR = hardwareMap.get(DcMotor.class, "rotateR");

        movePlat1 = hardwareMap.get(Servo.class, "movePlat1");
        movePlat2 = hardwareMap.get(Servo.class, "movePlat2");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Let's roll.");          // tell the driver we're all set
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }


    /* CONTROLS LIST


    DRIVE
    G1 left stick y             STRAIGHT
    G1 left stick x             STRAFE
    G1 right stick x            ROTATE
    G1 X                        SLOW


     */

    @Override
    public void loop() {

        //region drivetrain power
        float drive = scaleInput(-gamepad1.left_stick_y);
        float strafe = scaleInput(gamepad1.left_stick_x);
        float rotate = scaleInput(gamepad1.right_stick_x);

        telemetry.addData("drive", + drive);
        telemetry.addData("strafe", + strafe);
        telemetry.addData("rotate", + rotate);

        if(Math.abs(drive) < 0.05f) drive = 0.0f;
        if(Math.abs(strafe) < 0.05f) strafe = 0.0f;
        if(Math.abs(rotate) < 0.05f) rotate = 0.0f;

        if(!gamepad1.x) {
            lf.setPower(Range.clip(drive + strafe + rotate, -1.0, 1.0));
            lb.setPower(Range.clip(drive - strafe + rotate, -1.0, 1.0));
            rf.setPower(Range.clip(drive - strafe - rotate, -1.0, 1.0));
            rb.setPower(Range.clip(drive + strafe - rotate, -1.0, 1.0));
        }
        else{
            lf.setPower(.2*Range.clip(drive + strafe + rotate, -1.0, 1.0));
            lb.setPower(.2*Range.clip(drive - strafe + rotate, -1.0, 1.0));
            rf.setPower(.2*Range.clip(drive - strafe - rotate, -1.0, 1.0));
            rb.setPower(.2*Range.clip(drive + strafe - rotate, -1.0, 1.0));
        }
        //endregion

        //arm

        if((gamepad2.dpad_up || gamepad1.dpad_up)){         //up
            extend.setPower(1);
        } else if((gamepad2.dpad_down || gamepad1.dpad_down)){         //down
            extend.setPower(-1);
        } else {
            extend.setPower(0);
        }

       if(gamepad2.left_bumper) {
          grab.setPosition(0);
        }
       else if(gamepad2.right_bumper){
             grab.setPosition(1);

        }

        /*


        // See if the button is pressed to change if the arm is raised or not
        if(gamepad2.a){

            // Set the arm raised status to the opposite of what it originally was. I.E. a toggle button.
            armRaised = !armRaised;

            // Set the first tick variable to true. This makes it so that we can reset the encoders.
            firstTick = true;

        }

        // See if the button is pressed to change if the arm is raised or not
        if(gamepad2.b){

            // Set the arm raised status to the opposite of what it originally was. I.E. a toggle button.
            armRaised = !armRaised;

            // Set the first tick variable to true. This makes it so that we can reset the encoders.
            firstTick = true;

        }

        // Check if arm is raised or not
        if (armRaised) {

            rotateL.setPower(0.5f);
            rotateR.setPower(0.5f);

        } else {



            rotateL.setPower(-0.5f);
            rotateR.setPower(-0.5f);



        }

        telemetry.addData("rotateL", rotateR.getCurrentPosition() * TICKS_PER_ROTATION);
        telemetry.addData("rotateR", rotateL.getCurrentPosition() * TICKS_PER_ROTATION);

        if (firstTick) {

            // Reset all of the rotation motor encoders, that means that we can track the difference much easier.
            rotateL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rotateR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



            // rotateL.setTargetPosition(0);
            // rotateR.setTargetPosition(0);

            // rotateL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // rotateR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (armRaised) {



            } else {



            }

            // Set firstTick to false because afterwards, it is no longer the first tick
            firstTick = false;

        }

         */

        if (gamepad2.a) {

            rotateL.setPower(0.5);
            rotateR.setPower(-0.5);

        }else if (gamepad2.b){
            rotateL.setPower(-0.5);
            rotateR.setPower(0.5);
        }else{
            rotateL.setPower(0);
            rotateR.setPower(0);



        }

        if(gamepad1.right_bumper){
            movePlat1.setPosition(1);
            movePlat2.setPosition(.5);

        }else if(gamepad1.left_bumper){
            movePlat1.setPosition(0);
            movePlat2.setPosition(1);
        }


        //region telemetry
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("rb power", + rb.getPower());
        telemetry.addData("rf power", + rf.getPower());
        telemetry.addData("lf power", + lf.getPower());
        telemetry.addData("lb power", + lb.getPower());

        telemetry.addData("rb pos", + rb.getCurrentPosition());
        telemetry.addData("rf pos", + rf.getCurrentPosition());
        telemetry.addData("lf pos", + lf.getCurrentPosition());
        telemetry.addData("lb pos", + lb.getCurrentPosition());


        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.update();
        //endregion
    }



    @Override
    public void stop() {
    }
    float scaleInput(float in) {
        float out = in*in;
        if (in < 0)
            out = -out;
        return(out);
    }


}
