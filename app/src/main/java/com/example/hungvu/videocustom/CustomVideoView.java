package com.example.hungvu.videocustom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.VideoView;

/**
 * Created by hungvu on 6/27/15.
 */
public class CustomVideoView extends VideoView {
    private int width;
    private int height;
    private Context context;

    public CustomVideoView(Context context) {
        super(context);
        init(context);
    }

    /**
     * get video screen width and height for calculate size
     * @param context Context
     */
    private void init(Context context) {
        this.context = context;
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            // full screen when landscape
            setSize(height, width);
        }else {
            // height = width * 9/16
            setSize(width, width * 9/16);
        }
    }

    /**
     * set video sie
     * @param w Width
     * @param h Height
     */
    private void setSize(int w, int h) {
        setMeasuredDimension(w, h);
        getHolder().setFixedSize(w, h);
    }
}
