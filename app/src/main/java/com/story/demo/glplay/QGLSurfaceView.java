package com.story.demo.glplay;

import android.content.Context;
import android.util.AttributeSet;

import com.story.demo.glplay.glutil.PEGLSurfaceView;
import com.story.demo.glplay.glutil.QRender;

/**
 * Created by qiuyayong on 2019/2/20.
 * Email:qiuyayong@bigo.sg
 */

public class QGLSurfaceView extends PEGLSurfaceView {

    public QGLSurfaceView(Context context) {
        this(context, null);
    }

    public QGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new QRender());
        setRenderMode(PEGLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
