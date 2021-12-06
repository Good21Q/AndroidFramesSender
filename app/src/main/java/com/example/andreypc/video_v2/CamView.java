package com.example.andreypc.video_v2;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alvin on 2016-05-20.
 */
public class CamView extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private int width;
    private int height;

    private FrameListener listener;

    public CamView(Context context, Camera camera, FrameListener listener){
        super(context);
        this.mCamera=camera;
        this.listener = listener;
        this.mHolder=getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera=null;
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        try{
            mCamera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(320,240);
            this.width=parameters.getPreviewSize().width;
            this.height=parameters.getPreviewSize().height;
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setJpegQuality(10);
            parameters.setPreviewFpsRange(30000, 60000);
            mCamera.setParameters(parameters);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onPreviewFrame(byte[] data,Camera camera){
        try {
            YuvImage yuvimage=new YuvImage(data,ImageFormat.NV21,this.width,this.height,null);

            ByteArrayOutputStream baos=new ByteArrayOutputStream();

            yuvimage.compressToJpeg(new Rect(0,0,this.width,this.height),100,baos);

            Bitmap btm_scaled = createScaledBitmap(baos.toByteArray(), 300,300);

            byte[] scaled = bitmapToByteArray(btm_scaled);
            ByteArrayOutputStream f = new ByteArrayOutputStream();
            f.write(scaled, 0 , scaled.length);

            if (listener != null){
                listener.onFrameReady(f.toByteArray());
            }

        }catch(Exception e){
            Log.d("parse","errpr");
        }
    }

    public static Bitmap createScaledBitmap(byte[] bitmapAsData, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapAsData, 0, bitmapAsData.length);
        return createScaledBitmap(bitmap, width, height);
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 32, blob);
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 50, blob);
        return blob.toByteArray();
    }
}