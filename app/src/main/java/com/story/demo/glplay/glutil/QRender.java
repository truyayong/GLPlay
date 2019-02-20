package com.story.demo.glplay.glutil;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by qiuyayong on 2019/2/20.
 * Email:qiuyayong@bigo.sg
 */

public class QRender implements PEGLSurfaceView.PGLRender {
    private static final String TAG = "QRender";


    @Override
    public void onSurfaceCreate() {
        Log.i(TAG, "onSurfaceCreate");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {
        Log.i(TAG, "onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
    }
}
