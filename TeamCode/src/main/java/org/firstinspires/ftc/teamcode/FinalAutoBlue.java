package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="FinalAutoBlue", group="Autonomous")

public class FinalAutoBlue extends MasterAuto2020{

    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        reset();

        waitForStart();

        rotateArmForward();

        driveTime(.45,2000);


        grabBlock();

        sleep(2000);

        rotateArmBack();

        driveTime(-.6,1000);

        pivotTime(.7, 1000);

        driveTime(.5,4000);

        pivotTime(-.7, 1000);

        driveTime(.3,1000);

        rotateArmForward();

        releaseBlock();

        sleep(1000);

        driveTime(.3,1000);

        pivotTime(-.7,2000);

        driveTime(-.3,1000);

        movePlat();

        sleep(2000);

        pivotTime(.5,2000);

        driveTime(.5,1000);

        releasePlat();

        sleep(1000);

        driveTime(-.7,3000);


        halt();
    }

}
