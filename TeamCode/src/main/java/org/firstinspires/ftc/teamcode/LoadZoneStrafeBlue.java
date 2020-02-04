package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@Autonomous(name="LoadZoneStrafeBlue", group="Autonomous")
public class LoadZoneStrafeBlue extends MasterAuto2020 {



    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        //reset();

        waitForStart();

        strafeTime(.5, 1000);

        driveTime(-.5,2300);


        //drive(-1.7,.5);

        movePlat();

        sleep(2000);

        //drive(1.8,.7);

        driveTime(.5,2500);

        strafeTime(.5,2000);

        releasePlat();

        sleep(2000);

        //strafeTime(.7,5000);

        //rf.setPower(.7);
       //rb.setPower(-.7);
        //lf.setPower(-.7);
        //lb.setPower(.7);

        //sleep(4500);

        halt();


    }
}