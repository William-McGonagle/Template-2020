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

        //drive(1.6,.7);

        grabBlock();


        rotateArmBack();

        //strafe(-3,.7);



        halt();
    }
}