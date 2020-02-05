package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

@TeleOp()
@Disabled
public class ColorSensorOpMode extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2 {

    private Robot robot = new Robot();
    private CameraBridgeViewBase mOpenCvCameraView;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        telemetry.addData("getGreen", robot.getGreen());
        telemetry.addData("Red", robot.getRed());
        telemetry.addData("blue", robot.getBlue());
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        return;
    }

    @Override
    public void onCameraViewStopped() {
        return;
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}
