package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="StonePark", group="Autonomous")
public class StonePark extends MasterAuto2020 {



    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        reset();

        waitForStart();



        rotateArmForward();

        drive(1.5,.45);

        grabBlock();

        sleep(2000);


        rotateArmBack();

        drive(-.6,.7);

        strafe(4.5,.5);

        pivot(-45,.5);

        drive(.3,.3);

        rotateArmForward();

        releaseBlock();

        sleep(1000);


        drive(-.3,.5);

        //pivot(430,.7);

        //drive(-.3,.3);

        //movePlat();

        //sleep(2000);

        //drive(2,.7);

        //releasePlat();


        strafe(-3,.7);

        halt();
    }
}