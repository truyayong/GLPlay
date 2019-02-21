package com.story.demo.glplay;

import android.content.Context;
import android.util.AttributeSet;

import com.story.demo.glplay.glutil.MultiRender;
import com.story.demo.glplay.glutil.PEGLSurfaceView;

/**
 * Created by qiuyayong on 2019/2/21.
 * Email:qiuyayong@bigo.sg
 */

public class MultiSurfaceView extends PEGLSurfaceView {

    private MultiRender mMultiRender;

    public MultiSurfaceView(Context context) {
        this(context, null);
    }

    public MultiSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMultiRender = new MultiRender(context);
        setRender(mMultiRender);
    }

    public void setTextureId(int textureId, int index) {
        mMultiRender.setTextureId(textureId, index);
    }
}
