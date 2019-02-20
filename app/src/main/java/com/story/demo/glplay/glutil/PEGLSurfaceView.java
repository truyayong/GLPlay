package com.story.demo.glplay.glutil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLContext;

/**
 * Created by qiuyayong on 2019/2/20.
 * Email:qiuyayong@bigo.sg
 */

public abstract class PEGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "PEGLSurfaceView";

    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    private Surface mSurface;
    private EGLContext mEglContext;
    private PGLThread mPGLThread;
    private PGLRender mRender;

    private int mRenderMode = RENDERMODE_CONTINUOUSLY;

    public PEGLSurfaceView(Context context) {
        this(context, null);
    }

    public PEGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PEGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public void setRender(PGLRender render) {
        this.mRender = render;
    }

    public void setRenderMode(int renderMode) {
        if (mRender == null) {
            throw  new RuntimeException("must set render before");
        }
        this.mRenderMode = renderMode;
    }

    public void setSurfaceAndEglContext(Surface surface, EGLContext eglContext) {
        this.mSurface = surface;
        this.mEglContext = eglContext;
    }

    public EGLContext getEglContext() {
        if (mPGLThread != null) {
            return mPGLThread.getEglContext();
        }
        return null;
    }

    public void requestRender() {
        if (mPGLThread != null) {
            mPGLThread.requestRender();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mSurface == null) {
            mSurface = holder.getSurface();
        }
        mPGLThread = new PGLThread(this);
        mPGLThread.isCreate = true;
        mPGLThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mPGLThread.width = width;
        mPGLThread.height = height;
        mPGLThread.isChange = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPGLThread.onDestory();
        mPGLThread = null;
        mSurface = null;
        mEglContext = null;
    }

    public interface PGLRender {
        void onSurfaceCreate();
        void onSurfaceChanged(int width, int height);
        void onDrawFrame();
    }

    static class PGLThread extends Thread {
        private WeakReference<PEGLSurfaceView> peglSurfaceViewWeakReference;
        private EglHelper mEglHelper;
        private Object object = null;

        private boolean isExit = false;
        private boolean isCreate = false;
        private boolean isChange = false;
        private boolean isStart = false;

        private int width;
        private int height;

        public PGLThread(PEGLSurfaceView peglSurfaceView) {
            this.peglSurfaceViewWeakReference = new WeakReference<PEGLSurfaceView>(peglSurfaceView);
        }

        @Override
        public void run() {
            Log.e(TAG, "run");
            super.run();
            mEglHelper = new EglHelper();
            mEglHelper.initEgl(peglSurfaceViewWeakReference.get().mSurface
                    , peglSurfaceViewWeakReference.get().mEglContext);
            object = new Object();

            isExit = false;
            isStart = false;


            while (true) {
                if (isExit) {
                    release();
                    break;
                }

                if (isStart) {
                    if (peglSurfaceViewWeakReference.get().mRenderMode == RENDERMODE_WHEN_DIRTY) {
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (peglSurfaceViewWeakReference.get().mRenderMode == RENDERMODE_CONTINUOUSLY) {
                        try {
                            Thread.sleep(1000 / 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw  new RuntimeException("mRenderMode is wrong value");
                    }
                }
                onCreate();
                onChange(width, height);
                onDraw();
                isStart = true;
            }
        }

        private void onCreate() {
            if (isCreate && peglSurfaceViewWeakReference.get().mRender != null) {
                isCreate = false;
                peglSurfaceViewWeakReference.get().mRender.onSurfaceCreate();
            }
        }

        private void onChange(int width, int height) {
            if (isChange && peglSurfaceViewWeakReference.get().mRender != null) {
                isChange = false;
                peglSurfaceViewWeakReference.get().mRender.onSurfaceChanged(width, height);
            }
        }

        private void onDraw() {
            if (peglSurfaceViewWeakReference.get().mRender != null && mEglHelper != null) {
                peglSurfaceViewWeakReference.get().mRender.onDrawFrame();
                if (!isStart) {
                    peglSurfaceViewWeakReference.get().mRender.onDrawFrame();
                }
                mEglHelper.swapBuffers();
            }
        }

        private void requestRender() {
            if (object != null) {
                synchronized (object) {
                    object.notifyAll();
                }
            }
        }

        public void onDestory() {
            isExit = true;
            requestRender();
        }

        private void release() {
            if (mEglHelper != null) {
                mEglHelper.destoryEgl();
                mEglHelper = null;
                peglSurfaceViewWeakReference = null;
            }
        }

        public EGLContext getEglContext() {
            if (mEglHelper != null) {
                return mEglHelper.getEglContext();
            }
            return null;
        }
    }
}
