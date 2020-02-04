package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="FinalAutoRed", group="Autonomous")

public class FinalAutoRed extends MasterAuto2020{

    @Override
    public void runOpMode() {

        //region initialization
        initialize();


        waitForStart();

        rotateArmForward();

        driveTime(.4,2000);

        grabBlock();

        rotateArmBack();

        driveTime(-.6,200);

        pivotTime(-.7, 750);

        driveTime(.7,2700);

        pivotTime(.7, 800);

        driveTime(.3,900);

        rotateArmForward();

        releaseBlock();

        driveTime(-.4,800);

        pivotTime(.7,1500);

        driveTime(-.4,1000);

        movePlat();

        sleep(2000);

        driveTime(.5, 2000);
        //drive(1.8,.7);
        pivotTime(-.5, 2500);
        //driveTime(.5,2500);

        driveTime(-.5,1000);

        releasePlat();


        driveTime(1,1000);


        halt();
    }

}
