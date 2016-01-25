package edu.bahcesehir.linedetection;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeulucay on 25.01.2016.
 */
public class EdgeDetection {

    public EdgeDetection(){

    }

    public void ApplyCanny(final Mat src){

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat cannyImg = new Mat();

        Imgproc.Canny(src, cannyImg, 30, 100);

        Imgproc.findContours(cannyImg, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        double val = 0;

        for(int i = 0; i<contours.size(); i++){

            MatOfPoint mop = contours.get(i);

            val = Imgproc.contourArea(mop);

            if(val >10){

                Rect rect = Imgproc.boundingRect(contours.get(i));

                if(rect.height > 100 && rect.width>100 ) {
                    Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0));
                }


            }


        }

        //cannyImg.copyTo(src);
    }
}
