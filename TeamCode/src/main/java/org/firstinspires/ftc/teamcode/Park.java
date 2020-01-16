package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Park", group="Autonomous")
public class Park extends MasterAuto2020 {



    @Override
    public void runOpMode() {

        //region initialization
        initialize();

        reset();

        waitForStart();

        strafe(-1.25,.7);

        halt();
    }
}