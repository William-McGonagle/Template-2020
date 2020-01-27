package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@Autonomous(name="LoadZone", group="Autonomous")
public class LoadZone extends MasterAuto2020 {



    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        reset();

        waitForStart();

        drive(-.2,.7);

        pivot(-180,.7);


        drive(-1.6,.5);

        pivot(180,.7);

        drive(-1.6,.5);



        movePlat();

        sleep(2000);

        drive(1.6,.7);

        releasePlat();
        sleep(2000);



        pivot(-180,.7);

        drive(-2,.7);





        halt();
    }
}