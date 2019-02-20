package com.story.demo.glplay;

import android.content.Context;
import android.util.AttributeSet;

import com.story.demo.glplay.glutil.PEGLSurfaceView;
import com.story.demo.glplay.glutil.TextureRender;

/**
 * Created by qiuyayong on 2019/2/20.
 * Email:qiuyayong@bigo.sg
 */

public class TextureSurfaceView extends PEGLSurfaceView {

    public TextureSurfaceView(Context context) {
        this(context, null);
    }

    public TextureSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new TextureRender(context));
    }
}
