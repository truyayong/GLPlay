package com.story.demo.glplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.story.demo.glplay.glutil.TextureRender;

public class TextureSurfaceViewActivity extends AppCompatActivity {

    private TextureSurfaceView mTextureSurfaceView;
    private LinearLayout mBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_surface_view);
        mTextureSurfaceView = (TextureSurfaceView) findViewById(R.id.tsv_top);
        mBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mTextureSurfaceView.getRender().setOnRenderCreateListener(new TextureRender.OnRenderCreateListener() {
            @Override
            public void onCreate(final int texId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBottom.getChildCount() > 0) {
                            mBottom.removeAllViews();
                        }

                        for (int i = 0; i < 3; i++) {
                            MultiSurfaceView multiSurfaceView = new MultiSurfaceView(TextureSurfaceViewActivity.this);
                            multiSurfaceView.setTextureId(texId, i);
                            multiSurfaceView.setSurfaceAndEglContext(null, mTextureSurfaceView.getEglContext());

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp.width = 200;
                            lp.height = 300;
                            multiSurfaceView.setLayoutParams(lp);

                            mBottom.addView(multiSurfaceView);
                        }
                    }
                });
            }
        });

    }
}
