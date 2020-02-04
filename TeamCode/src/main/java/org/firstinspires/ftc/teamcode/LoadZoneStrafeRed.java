package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@Autonomous(name="LoadZoneStrafeRed", group="Autonomous")
public class LoadZoneStrafeRed extends MasterAuto2020 {



    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        //reset();

        waitForStart();

        strafeTime(-.5, 1000);

        driveTime(-.5,2400);


        //drive(-1.7,.5);

        movePlat();

        sleep(2000);

        driveTime(.5, 2000);
        //drive(1.8,.7);
        pivotTime(-.5, 2500);
        //driveTime(.5,2500);

        driveTime(-.5,1000);

        releasePlat();

        sleep(2000);

        driveTime(.5,2500);





        halt();


    }
}