package com.disnodeteam.dogecv.detectors;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StonesDetector extends DogeCVDetector {

    // Defining Mats to be used.
    private Mat displayMat = new Mat(); // Display debug info to the screen (this is what is returned)
    private Mat workingMat = new Mat(); // Used for preprocessing and working with (blurring as an example)

    private Mat y_thresh = new Mat();
    //private Mat b_thresh = new Mat();
    private int [] y_list = new int[640];
    //private int [] b_list = new int[640];

    public boolean blockLocated;
    public int block_location;
    private int frameNumber;

    int left_rank = 0;
    int center_rank = 0;
    int right_rank = 0;


    /**
     * Simple constructor
     */
    public StonesDetector() {
        super();
        detectorName = "Generic Detector"; // Set the detector name
        blockLocated = false;
        block_location = -1;
        frameNumber = 0;
    }


    @Override
    public Mat process(Mat input) {

        // Copy the input mat to our working mats, then release it for memory
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();

        frameNumber++;
        if(!blockLocated && frameNumber > 5) {
            // Locate block

            Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2YCrCb);
            Core.inRange(workingMat, new Scalar(80, 160, 40), new Scalar(200, 180, 90), y_thresh);
            //Core.inRange(workingMat, new Scalar(0, 0, 0), new Scalar(20, 255, 255), b_thresh);

            Arrays.fill(y_list, 0);
            for (int i = 0; i < 640; i++) {
                for (int j = 0; j < 480; j++) {
                    if (y_thresh.get(j, i)[0] > 0.0)
                        y_list[j]++;
                    //if (b_thresh.get(j, i)[0] > 0.0)
                    //   b_list[j]++;
                }
            }

            for (int i = 150; i < 225; i++) {
                left_rank += y_list[i];
            }
            Log.e("Debug", "1 " + String.valueOf(left_rank));
            for (int i = 225; i < 300; i++) {
                center_rank += y_list[i];
            }
            Log.e("Debug", "2 " + String.valueOf(center_rank));
            for (int i = 300; i < 375; i++) {
                right_rank += y_list[i];
            }
            Log.e("Debug", "3 " + String.valueOf(right_rank));

            //Log.e("Debug", String.valueOf(displayMat.size()));
            if(frameNumber > 10) {
                int min_rank = Math.min(Math.min(left_rank, center_rank), right_rank);
                if (min_rank == left_rank) {
                    block_location = 1;
                } else if (min_rank == center_rank) {
                    block_location = 2;
                } else {
                    block_location = 3;
                }
                Log.e("Camera", String.valueOf(block_location));
                blockLocated = true;
            }
        }
        else {

        }

        return displayMat;
    }

    @Override
    public void useDefaults() {

    }

}
