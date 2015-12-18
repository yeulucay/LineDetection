package edu.bahcesehir.linedetection;

/**
 * Created by ye on 17/12/15.
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ye on 13/12/15.
 */
public class LineDetection {


    public LineDetection() {

    }

    public void Detect(final Mat src){


        Mat imgSource = new Mat();
        Mat imgSourceGray = new Mat();
        Mat imgCanny = new Mat();
        Mat imgLinesOut = new Mat();


        Imgproc.GaussianBlur(src, imgSource, new Size(5, 5), 2, 2);
        int threshold = 100;
        int minLineSize = 100;
        int lineGap = 30;

        Imgproc.cvtColor(imgSource,imgSourceGray,Imgproc.COLOR_RGB2GRAY, 4);

        Imgproc.Canny(imgSourceGray, imgCanny, 80, 100);

        Imgproc.HoughLinesP(imgCanny, imgLinesOut, 2, Math.PI/180, threshold, minLineSize, lineGap);

        int cols = imgLinesOut.cols();

        for( int j = 0; j < imgLinesOut.rows(); j++ )
        {
            double[] vec=imgLinesOut.get(j,0);

            Point pt1, pt2;
            pt1=new Point(vec[0],vec[1]);
            pt2=new Point(vec[2],vec[3]);

            Imgproc.line(src, pt1, pt2, new Scalar(0, 255, 0), 3, Core.LINE_AA, 0);
        }



    }

}

