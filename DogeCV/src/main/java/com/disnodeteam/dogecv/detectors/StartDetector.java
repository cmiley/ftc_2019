package com.disnodeteam.dogecv.detectors;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class StartDetector extends DogeCVDetector {

    // Defining Mats to be used.
    private Mat displayMat = new Mat(); // Display debug info to the screen (this is what is returned)
    private Mat workingMat = new Mat(); // Used for preprocessing and working with (blurring as an example)

    public int start_color;
    public boolean color_detected;
    private int frameNumber;

    /**
     * Simple constructor
     */
    public StartDetector() {
        super();
        detectorName = "Start Detector"; // Set the detector name
        start_color = 0;
        color_detected = false;
        frameNumber = 0;
    }


    @Override
    public Mat process(Mat input) {
        Log.e("Debug", "hi, I'm a debug message!");
        Log.e("Debug", "shape: " + input.width() + " " + input.height());

        // Copy the input mat to our working mats, then release it for memory
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        frameNumber++;
        Log.e("Debug", "frame no: " + frameNumber);
        if(!color_detected && frameNumber > 0) {
            Mat hsl = new Mat();
            Imgproc.cvtColor(input, hsl, Imgproc.COLOR_BGR2HSV);
            Log.e("Debug", "color converted");

            Mat mask_red = new Mat();
            Mat mask_blue = new Mat();
            Core.inRange(hsl, new Scalar(80,120,100), new Scalar(120,255,255), mask_blue);
            Core.inRange(hsl, new Scalar(0,120,100), new Scalar(45,255,255), mask_red);
            hsl.release();
            Log.e("Debug", "thresholding done");

            Rect cropbox = new Rect(0,2500,4048,536);
            Mat cropped_red = new Mat(mask_red, cropbox);
            Mat cropped_blue = new Mat(mask_blue, cropbox);
            mask_red.release();
            mask_blue.release();
            Log.e("Debug", "cropping done");

            double red_count = Core.sumElems(cropped_red).val[0]/255;
            double blue_count = Core.sumElems(cropped_blue).val[0]/255;
            cropped_red.release();
            cropped_blue.release();
            Log.e("Debug", "counting done");

            if ((red_count > 0) || (blue_count > 0))
                color_detected = true;
            if (red_count > blue_count)
                start_color = 1;
            else
                start_color = 2;
            Log.e("Debug", "color selected");
        }
        return displayMat;
    }

    @Override
    public void useDefaults() {

    }

}
