package com.disnodeteam.dogecv.detectors;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class RingDetector extends DogeCVDetector {

    // Defining Mats to be used.
    private Mat displayMat = new Mat(); // Display debug info to the screen (this is what is returned)
    private Mat workingMat = new Mat(); // Used for preprocessing and working with (blurring as an example)

    public int ring_count = -1;
    public boolean color_detected;
    private int frameNumber;
    public double orange_pixel_count;

    /**
     * Simple constructor
     */
    public RingDetector() {
        super();
        detectorName = "Ring Detector"; // Set the detector name
        color_detected = false;
        frameNumber = 0;
        orange_pixel_count = -1;
    }


    @Override
    public Mat process(Mat input) {
//        Log.d("StartDetector", "hi, I'm a debug message!");
//        Log.d("StartDetector", "shape: " + input.width() + " " + input.height());
        // Copy the input mat to our working mats, then release it for memory
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        Imgproc.rectangle(displayMat, new Point(340, 0), new Point(640, 480), new Scalar(0, 0, 255),5);

        frameNumber++;
//        Log.d("StartDetector", "Dump mat:" + workingMat.dump());
//        Log.d("StartDetector", "frame no: " + frameNumber);
        if(!color_detected && frameNumber > 0) {
            Mat hsv = new Mat();
            Imgproc.cvtColor(workingMat, hsv, Imgproc.COLOR_BGR2HSV);
            Log.d("RingDetector", "color converted");

            Mat mask = new Mat();
            Core.inRange(hsv, new Scalar(5,160,80), new Scalar(45,240,240), mask);
            hsv.release();
            Log.d("RingDetector", "thresholding done");

            Rect cropbox = new Rect(340,0,300,480);
            Mat cropped = new Mat(mask, cropbox);
            mask.release();

            orange_pixel_count = Core.sumElems(cropped).val[0]/255;
            cropped.release();
            Log.d("RingDetector", "orange pixels: " + orange_pixel_count);
            ring_count = 0;
            if (orange_pixel_count > 500)
                ring_count = 1;
            if (orange_pixel_count > 1500)
                ring_count = 4;

            Log.d("RingDetector", "ring number: " + ring_count);
        }
        return displayMat;
    }

    @Override
    public void useDefaults() {

    }

    public int get_ring_count() {
        return ring_count;
    }

}
