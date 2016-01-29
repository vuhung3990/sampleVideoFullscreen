package com.example.hungvu.videocustom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.VideoView;

import java.lang.reflect.Method;

/**
 * Created by hungvu on 6/27/15.
 */
public class CustomVideoView extends android.widget.VideoView {
    private int width;
    private int height;
    private Context context;
    private VideoSizeChangeListener listener;
    private boolean isFullscreen;

    public CustomVideoView(Context context) {
        super(context);
        init(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * get video screen width and height for calculate size
     *
     * @param context Context
     */
    private void init(Context context) {
        this.context = context;
        setScreenSize();
    }

    /**
     * calculate real screen size
     */
    private void setScreenSize() {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            width = realMetrics.widthPixels;
            height = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                width = (Integer) mGetRawW.invoke(display);
                height = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                width = display.getWidth();
                height = display.getHeight();
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            width = display.getWidth();
            height = display.getHeight();
        }

        // when landscape w > h, swap it
        if (width > height) {
            int temp = width;
            width = height;
            height = temp;
        }
    }

    /**
     * set video size change listener
     *
     */
    public void setVideoSizeChangeListener(VideoSizeChangeListener listener) {
        this.listener = listener;
    }

    public interface VideoSizeChangeListener {
        /**
         * when landscape
         */
        void onFullScreen();

        /**
         * when portrait
         */
        void onNormalSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // full screen when landscape
            setSize(height, width);
            if (listener != null) listener.onFullScreen();
            isFullscreen = true;
        } else {
            // height = width * 9/16
            setSize(width, width * 9 / 16);
            if (listener != null) listener.onNormalSize();
            isFullscreen = false;
        }
    }

    /**
     * @return true: fullscreen
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }

    /**
     * set video sie
     *
     * @param w Width
     * @param h Height
     */
    private void setSize(int w, int h) {
        setMeasuredDimension(w, h);
        getHolder().setFixedSize(w, h);
    }
}