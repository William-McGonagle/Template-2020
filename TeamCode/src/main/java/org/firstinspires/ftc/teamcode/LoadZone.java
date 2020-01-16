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



        drive(-1.6,.7);

        strafe(-1,.7);


        movePlat(); 

        sleep(2000);



        drive(2,.7);

        releasePlat();

        strafe(2,.7);



        halt();
    }
}