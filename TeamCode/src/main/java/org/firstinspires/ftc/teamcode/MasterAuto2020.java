package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_SKY_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.TFOD_MODEL_ASSET;

@Autonomous(name = "master", group = "Autonomous")
public class MasterAuto2020 extends LinearOpMode {

    public static final String VUFORIA_KEY = "AXDMU6L/////AAABmYsje6g+d0FouarmMJSceCUvoPLsXYHB38V7+MVCV//rzuYmaMR0aeKY+X1gyKROXD2HP/yqTdMoGKjNifE0TLgN3fUlxqF8CAejftyRLJXX7t1xBrivJKRDgDbQrX6I+6xe2ZcfInF2KnfQHOrlMh/i7M4RU6vzkIwKIzCwkV/SaMxAyYWpEngCIK+3ZelwN2uVIc0nXFNEXI2qVTaiAb7ffvbqzCBcxXrxCzbahSso5A/fD9f6FGsyMvVTQUzRaybT473gX+RJ1nPHyqjTscffYVyBGl0sAQ259VwLGwM+FE+ymehKO1shL9s1ITfaZaRdSWxzxvdS/e5xaavoXEw3ylD16GUnclpvw1s/ts7y";
    //endregion

    static final double TICKS_PER_ROTATION = 1120.0 * 0.75;
    static final double WHEEL_DIAMETER = 4;  // bad units

    static final double TICKS_PER_INCH = (TICKS_PER_ROTATION) / (WHEEL_DIAMETER * Math.PI);
    static final double TICKS_PER_TILE = TICKS_PER_INCH * 24;
    static final double WHEEL_SPAN = 10.86614; // bad units

    WebcamName webcamName;

    BNO055IMU gyro;
    // variables.

    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;

    public ElapsedTime runtime = new ElapsedTime();

    public DcMotor lf = null;
    public DcMotor rf = null;
    public DcMotor lb = null;
    public DcMotor rb = null;

    //arm
    private DcMotor extend = null;
    private Servo grab = null;
    private DcMotor rotateL = null;
    private DcMotor rotateR = null;

    //movePlatform
    private Servo movePlat1 = null;
    private Servo movePlat2 = null;

    public void runOpMode() {
    }

    void initialize() {
        rf = hardwareMap.get(DcMotor.class, "rf");
        rb = hardwareMap.get(DcMotor.class, "rb");
        lf = hardwareMap.get(DcMotor.class, "lf");
        lb = hardwareMap.get(DcMotor.class, "lb");      // get drivetrain motors

        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);
        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);                 // set drivetrain directions

        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);   // set drivetrain ZPB

        // Tell the driver that initialization is complete.
        extend = hardwareMap.get(DcMotor.class, "extend");
        rotateL = hardwareMap.get(DcMotor.class, "rotateL");
        rotateR = hardwareMap.get(DcMotor.class, "rotateR");

        grab = hardwareMap.get(Servo.class, "grab");
        movePlat1 = hardwareMap.get(Servo.class, "movePlat1");
        movePlat2 = hardwareMap.get(Servo.class, "movePlat2");

        gyro = hardwareMap.get(BNO055IMU.class, "imu");


        //initVuforia();
        /*if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            //initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }*/ // frick no we ain't doin no gosh diddly darn view

        sleep(1000);                                 // chill out a sec while it sets up
    }

    private void initVuforia() {

        webcamName = hardwareMap.get(WebcamName.class, "logi");
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraName = webcamName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }


    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKY_STONE);
    }

    void reset() {
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);         // reset encoder positions

        rf.setTargetPosition(0);
        rb.setTargetPosition(0);
        lf.setTargetPosition(0);
        lb.setTargetPosition(0);


        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);                // turn encoders back on
    }

    void halt() {
        rf.setPower(0);
        rb.setPower(0);
        lf.setPower(0);
        lb.setPower(0); // stop all motors
    }


    void driveTime(double pwr, int time){

        rf.setPower(pwr);
        rb.setPower(pwr);
        lf.setPower(pwr);
        lb.setPower(pwr);

        sleep(time);

        halt();

    }


    void drive(double distance, double pwr) {
        reset();
        int target = (int) (distance * TICKS_PER_TILE);

        pwr = Math.abs(pwr);

        rf.setTargetPosition(target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(target);
        lb.setTargetPosition(target);


        rf.setPower(pwr);
        rb.setPower(pwr);
        lf.setPower(pwr);
        lb.setPower(pwr);

        while (opModeIsActive() && rf.isBusy() && rb.isBusy() && lf.isBusy() && lb.isBusy()) {



            telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("rfpow", rf.getPower());
            telemetry.addData("rbpow", rb.getPower());
            telemetry.addData("lfpow", lf.getPower());
            telemetry.addData("lbpow", lb.getPower());

            telemetry.addData("complete", 1);
            telemetry.update();
        }
        halt();
        reset();
    }

    /**
     * @param distance to move forward in tiles
     * @param pwr      to give the motors, from 0.0 to 1.0
     * @param ramp     the distance in inches to accelerate linearly through
     */
    void drive(double distance, double pwr, int ramp) {
        reset();
        int target = (int) (distance * TICKS_PER_TILE);

        double fpwr = Math.abs(pwr);

        rf.setTargetPosition(target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(target);
        lb.setTargetPosition(target);

        while (opModeIsActive() && rf.isBusy()) {

            if (rf.getCurrentPosition() >= ramp * TICKS_PER_TILE / 24) {
                pwr = fpwr;
            } else {
                pwr = fpwr * (rf.getCurrentPosition() + 50) / (ramp * TICKS_PER_TILE / 24);
            }

            rf.setPower(pwr);
            rb.setPower(pwr);
            lf.setPower(pwr);
            lb.setPower(pwr);

            telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("rfpow", rf.getPower());
            telemetry.addData("rbpow", rb.getPower());
            telemetry.addData("lfpow", lf.getPower());
            telemetry.addData("lbpow", lb.getPower());
            telemetry.update();
        }
        reset();
        halt();
    }


    void swivelpivot(double angle, double pwr) {
        reset();
        double distance = 0.8 * Math.PI * (WHEEL_SPAN) * angle / 255; // remember learning s = r*theta? it's back to haunt you.
        int target = (int) (distance * TICKS_PER_INCH);
        pwr = Math.abs(pwr);

        rf.setTargetPosition(target);
        rb.setTargetPosition((int) (target * .5547));
        lf.setTargetPosition(-target);
        lb.setTargetPosition(-(int) (target * .5547));
        rf.setPower(pwr);
        rb.setPower(pwr * .5547);
        lf.setPower(pwr);
        lb.setPower(pwr * .5547);

        while (opModeIsActive() && rf.isBusy()) {
            telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("rfpow", rf.getPower());
            telemetry.addData("rbpow", rb.getPower());
            telemetry.addData("lfpow", lf.getPower());
            telemetry.addData("lbpow", lb.getPower());
            telemetry.update();
        }
        halt();
    }

    void pivot(double angle, double pwr) {
        reset();
        double distance = Math.PI * (WHEEL_SPAN / 2) * angle / 180; // remember learning s = r*theta? it's back to haunt you.
        int target = (int) (distance * TICKS_PER_INCH);
        pwr = Math.abs(pwr);

        rf.setTargetPosition(target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(-target);
        lb.setTargetPosition(-target);

        rf.setPower(pwr);
        rb.setPower(pwr);
        lf.setPower(pwr);
        lb.setPower(pwr);

        while (opModeIsActive() && rf.isBusy() && rb.isBusy() && lf.isBusy() && lb.isBusy()) {
            telemetry.addData("rfD", rf.getCurrentPosition());
            telemetry.addData("rbD", rb.getCurrentPosition());
            telemetry.addData("lfD", lf.getCurrentPosition());
            telemetry.addData("lbD", lb.getCurrentPosition());

            telemetry.addData("rfP", rf.getPower());
            telemetry.addData("rbP", rb.getPower());
            telemetry.addData("lfP", lf.getPower());
            telemetry.addData("lbP", lb.getPower());
            telemetry.update();
        }
        halt();
        reset();
    }

    void movePlat() {
        telemetry.addData("complete", 3);
        telemetry.update();
        movePlat1.setPosition(1);
        movePlat2.setPosition(.5);




    }

    void releasePlat() {
        movePlat1.setPosition(0);
        movePlat2.setPosition(1);
    sleep(2000);

    }


    void strafeAC(double distance, double pwr) {
        reset();
        int target = (int) (distance * TICKS_PER_TILE);

        float urgency = 0.15f; // remember that we may end up more than 10 degrees off course.

        float initial = gg();
        float current;

        rf.setTargetPosition(-target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(target);
        lb.setTargetPosition(-target);

        while (opModeIsActive() && rf.isBusy()) {
            current = gg();
            float d = initial - current;

            rf.setPower(pwr - d * urgency * pwr);
            rb.setPower(pwr + d * urgency * pwr);
            lf.setPower(pwr - d * urgency * pwr);
            lb.setPower(pwr + d * urgency * pwr);

            //region telemetry
            /*telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());*/

            telemetry.addData("R f pwr", rf.getPower());
            telemetry.addData("R b pwr", rb.getPower());
            telemetry.addData("L f pwr", lf.getPower());
            telemetry.addData("L b pwr", lb.getPower());

            telemetry.addData("d", d);
            telemetry.addData("p", d * urgency);

            telemetry.update();
            //endregion
        }
        halt();
        reset();
    }





    void strafe(double distance, double pwr) {
        reset();
        int target = (int) (distance * TICKS_PER_TILE);

        pwr = Math.abs(pwr);

        rf.setTargetPosition(-target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(target);
        lb.setTargetPosition(-target);

        rf.setPower(pwr);
        rb.setPower(pwr);
        lf.setPower(pwr);
        lb.setPower(pwr);

        while (opModeIsActive() && rf.isBusy() && rb.isBusy() && lf.isBusy() && lb.isBusy()) {





            telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("rfpow", rf.getPower());
            telemetry.addData("rbpow", rb.getPower());
            telemetry.addData("lfpow", lf.getPower());
            telemetry.addData("lbpow", lb.getPower());

            telemetry.addData("complete", 1);
            telemetry.update();

        }





        //movePlat1.setPosition(0);
        //movePlat2.setPosition(1.0);

        //sleep(2000);
        halt();
        reset();

    }

    void strafeTime(double pwr, int time) {



        rf.setPower(-pwr);
        rb.setPower(pwr);
        lf.setPower(pwr);
        lb.setPower(-pwr);

        sleep(time);

        halt();


    }

    /**
     * @param distance distance. may be positive or negative
     * @param pwr POSITIVE power
     */
    void driveAC(double distance, double pwr) {
        reset();
        int target = (int) (distance * TICKS_PER_TILE);

        float urgency = 0.05f; // remember that we may end up more than 10 degrees off course.

        pwr = Math.abs(pwr);

        float initial = gg();
        float current;

        rf.setTargetPosition(target);
        rb.setTargetPosition(target);
        lf.setTargetPosition(target);
        lb.setTargetPosition(target);

        while (opModeIsActive() && rf.isBusy() && lf.isBusy() && rb.isBusy() && lb.isBusy()) {
            current = gg();
            float d = initial - current;

            rf.setPower(pwr + d * urgency * pwr);
            rb.setPower(pwr + d * urgency * pwr);
            lf.setPower(pwr - d * urgency * pwr);
            lb.setPower(pwr - d * urgency * pwr);

            //region telemetry
            /*telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());*/

            telemetry.addData("R pwr", rf.getPower());
            telemetry.addData("L pwr", lf.getPower());

            telemetry.addData("d", d);
            telemetry.addData("p", d * urgency);

            telemetry.addData("tgt p", target);
            telemetry.addData("max p", Math.max(Math.max(rf.getCurrentPosition(), lf.getCurrentPosition()), Math.max(rb.getCurrentPosition(), lb.getCurrentPosition())));

            telemetry.update();
            //endregion
        }
        halt();
        reset();
    }

    void turnAC(int angle, double pwr) {
        reset();

        if (angle == 0) {
            return;
        }

        pwr = Math.abs(pwr);

        int tolerance = 15;

        int target = gg() + angle;

        if (target > 180)
            target = target - 360;
        if (target < -180)
            target = target + 360;

        int current = gg();

        sleep(500);

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int sign = angle / Math.abs(angle);

        rf.setPower(sign * pwr);
        rb.setPower(sign * pwr);
        lf.setPower(-sign * pwr);
        lb.setPower(-sign * pwr);

        while (opModeIsActive() && Math.abs(target - current) > tolerance * pwr + 5) {
            current = gg();

            //region telemetry

            telemetry.addData("target", target);
            telemetry.addData("current", current);
            telemetry.addData("sign", sign);
            telemetry.addData("delta", Math.abs(target - current));
            telemetry.addData("tolerance", tolerance);


            telemetry.update();
            //endregion
        }
        halt();

        sleep(300);

        current = gg();

        rf.setPower(-sign * pwr * 0.2);
        rb.setPower(-sign * pwr * 0.2);
        lf.setPower(sign * pwr * 0.2);
        lb.setPower(sign * pwr * 0.2);
        while (opModeIsActive() && Math.abs(target - current) > 5) {
            current = gg();

            //region telemetry

            telemetry.addData("target", target);
            telemetry.addData("current", current);
            telemetry.addData("sign", sign);
            telemetry.addData("delta", Math.abs(target - current));
            telemetry.addData("tolerance", tolerance);


            telemetry.update();
            //endregion
        }
        halt();
    }

    void turnAC_debug(int angle, double pwr) {
        reset();

        if (angle == 0) {
            return;
        }

        pwr = Math.abs(pwr);

        int tolerance = 15;

        int target = gg() + angle;

        if (target > 180)
            target = target - 360;
        if (target < -180)
            target = target + 360;

        int current = gg();

        sleep(500);

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int sign = angle / Math.abs(angle);

        rf.setPower(sign * pwr);
        rb.setPower(sign * pwr);
        lf.setPower(-sign * pwr);
        lb.setPower(-sign * pwr);

        while (opModeIsActive() && Math.abs(target - current) > tolerance * pwr + 5) {
            current = gg();

            //region telemetry

            telemetry.addData("target", target);
            telemetry.addData("current", current);
            telemetry.addData("sign", sign);
            telemetry.addData("delta", Math.abs(target - current));
            telemetry.addData("tolerance", tolerance);


            telemetry.update();
            //endregion
        }

        halt();

        while (opModeIsActive()) {
            current = gg();

            //region telemetry

            telemetry.addData("target", target);
            telemetry.addData("current", current);
            telemetry.addData("sign", sign);
            telemetry.addData("delta", Math.abs(target - current));
            telemetry.addData("tolerance", tolerance);


            telemetry.update();
            //endregion
        }
    }

    void rotateArmForward(){
        rotateL.setPower(.5);
        rotateR.setPower(-.5);
        sleep(1000);
        rotateL.setPower(0);
        rotateR.setPower(0);

    }

    void rotateArmBack(){
        rotateL.setPower(-.4);
        rotateR.setPower(.4);
        sleep(1000);
        rotateL.setPower(0);
        rotateR.setPower(0);
    }

    void grabBlock(){

        grab.setPosition(1);
    }

    void releaseBlock(){

        grab.setPosition(0);
    }

    void turnACB(int angle, double pwr) {
        reset();

        if (angle == 0) {
            return;
        }

        pwr = Math.abs(pwr);

        int tolerance = 15;

        int target = gg() + angle;

        if (target > 180)
            target = target - 360;
        if (target < -180)
            target = target + 360;

        int current = gg();

        sleep(500);

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int sign = angle / Math.abs(angle);

        rf.setPower(sign * pwr);
        rb.setPower(sign * pwr);
        lf.setPower(-sign * pwr);
        lb.setPower(-sign * pwr);

        while (opModeIsActive() && Math.abs(target - current) > tolerance * pwr + 5) {
            current = gg();

            //region telemetry

            telemetry.addData("target", target);
            telemetry.addData("current", current);
            telemetry.addData("sign", sign);
            telemetry.addData("delta", Math.abs(target - current));
            telemetry.addData("tolerance", tolerance);


            telemetry.update();
            //endregion
        }
        halt();
    }

    /**
     * @return the current gyro reading as an int from 0 to 360 degrees
     */
    int gg() {

        int gyroAngle = (int) gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        return gyroAngle;
    }

    /**
     * @param timeout is how long before tfod gives up and returns center
     * @return the position of gold, 0 left, 1 center, 2 right
     */
    int tfod(int timeout) {
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. **/
            if (tfod != null) {
                tfod.activate();
            }

            runtime.reset();

            int stone1x = -1;

            final int WIDTH = 1; // SET THIS LATER
            final int TOLERANCE = 10; // the accuracy. lower is better, but takes longer (or forever)
            final double POWERMULT = 0.001; // speed of the robot. probably needs to be super low.

            while (opModeIsActive() && runtime.seconds() < timeout) {

                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_SKY_STONE)) {
                                stone1x = (int) recognition.getLeft();
                                break;
                            }
                        }
                    }
                }

                int dist = Math.abs(WIDTH/2 - stone1x);

                if (dist > TOLERANCE) {
                    rf.setPower(dist * POWERMULT);
                    lf.setPower(dist * POWERMULT);
                    rb.setPower(-dist * POWERMULT);
                    lb.setPower(-dist * POWERMULT);
                } else {
                    halt();
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
        return 1;
    }

    void getSkyStone () {

    }

    /*void alignWall(int timeout) {
        reset();
        runtime.reset();

        double pwr = -0.7;

        float urgency = 0.15f; // remember that we may end up more than 10 degrees off course.

        float initial = gg();
        float current;

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive() && !atouch.isPressed() && runtime.seconds() < timeout) {
            current = gg();
            float d = initial - current;

            rf.setPower(pwr - d * urgency * pwr);
            rb.setPower(pwr + d * urgency * pwr);
            lf.setPower(pwr - d * urgency * pwr);
            lb.setPower(pwr + d * urgency * pwr);

            //region telemetry
            /*telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("R f pwr", rf.getPower());
            telemetry.addData("R b pwr", rb.getPower());
            telemetry.addData("L f pwr", lf.getPower());
            telemetry.addData("L b pwr", lb.getPower());

            telemetry.addData("d", d);
            telemetry.addData("p", d * urgency);

            telemetry.update();
            //endregion
        }
        halt();
        reset();
    }*/

    /*void alignWallAcc(int timeout) {
        reset();
        runtime.reset();

        double pwr = -0.7;

        float urgency = 0.15f; // remember that we may end up more than 10 degrees off course.

        float initial = gg();
        float current;

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Acceleration a = gyro.getAcceleration();
        float p = (float)Math.sqrt(Math.pow(a.xAccel,2) + Math.pow(a.yAccel,2) + Math.pow(a.zAccel,2));

        while (opModeIsActive() && ((p < 10) || runtime.seconds() < 0.5) && runtime.seconds() < timeout) {
            a = gyro.getAcceleration();
            p = (float)Math.sqrt(Math.pow(a.xAccel,2) + Math.pow(a.yAccel,2) + Math.pow(a.zAccel,2));

            current = gg();
            float d = initial - current;

            rf.setPower(pwr - d * urgency * pwr);
            rb.setPower(pwr + d * urgency * pwr);
            lf.setPower(pwr - d * urgency * pwr);
            lb.setPower(pwr + d * urgency * pwr);

            //region telemetry
            /*telemetry.addData("rfpos", rf.getCurrentPosition());
            telemetry.addData("rbpos", rb.getCurrentPosition());
            telemetry.addData("lfpos", lf.getCurrentPosition());
            telemetry.addData("lbpos", lb.getCurrentPosition());

            telemetry.addData("R f pwr", rf.getPower());
            telemetry.addData("R b pwr", rb.getPower());
            telemetry.addData("L f pwr", lf.getPower());
            telemetry.addData("L b pwr", lb.getPower());

            telemetry.addData("d", d);
            telemetry.addData("p", d * urgency);

            telemetry.update();
            //endregion
        }
        halt();
        reset();
    }*/
}