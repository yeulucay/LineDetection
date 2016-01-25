package edu.bahcesehir.linedetection;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.List;


public class CameraActivity extends AppCompatActivity
implements CvCameraViewListener2 {

    private CameraBridgeViewBase mCameraView;
    private List<Camera.Size> mSupportedImageSizes;
    private RelativeLayout mainLayout;
    private ImageView preview;
    private boolean isPreviewMode;

    private LineDetection mLineDetection;
    private EdgeDetection mEdgeDetection;

    private Mat mBgr;


    // The OpenCV loader callback.
    private BaseLoaderCallback mLoaderCallback =
            new BaseLoaderCallback(this) {
                @Override
                public void onManagerConnected(final int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS:

                            mCameraView.enableView();
                            //mCameraView.enableFpsMeter();
                            mLineDetection = new LineDetection();
                            mEdgeDetection = new EdgeDetection();
                            mBgr = new Mat();
                            break;
                        default:
                            super.onManagerConnected(status);
                            break;
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // hides status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // to make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                isPreviewMode = !isPreviewMode;

                if(isPreviewMode) {
                    mCameraView.setVisibility(View.GONE);
                    preview.setVisibility(View.VISIBLE);
                    previewImage();
                }
                else {
                    preview.setVisibility(View.GONE);
                    mCameraView.setVisibility(View.VISIBLE);
                }
            }
        });

        isPreviewMode = false;
        preview = (ImageView)findViewById(R.id.preview);
        preview.setVisibility(View.GONE);


        final Camera camera;

        camera = Camera.open();

        final Camera.Parameters parameters = camera.getParameters();
        camera.release();
        mSupportedImageSizes = parameters.getSupportedPreviewSizes();
        final Camera.Size size = mSupportedImageSizes.get(0);
        mCameraView = new JavaCameraView(this, 0); // 0: rare cam.
        mCameraView.setMaxFrameSize(size.width, size.height);
        mCameraView.setCvCameraViewListener(this);

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.addView(mCameraView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final Mat rgba = inputFrame.rgba();
        rgba.copyTo(mBgr);
        if(isPreviewMode)
        {
            return null;
        }
        return rgba;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,
                this, mLoaderCallback);
    }

    private void previewImage(){
        //mLineDetection.Detect(mBgr);

        mEdgeDetection.ApplyCanny(mBgr);

        Bitmap bmp = Bitmap.createBitmap(mBgr.cols(), mBgr.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mBgr, bmp);

        preview.setImageBitmap(bmp);
    }
}
