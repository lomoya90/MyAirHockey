package com.example.lomoya.myairhockey;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Lomoya on 2017/6/4.
 */

public class FirstOpenGLProjectActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private AirHockeyRenderer airHockeyRenderer;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        airHockeyRenderer = new AirHockeyRenderer(this);

        boolean isSurpportGL2 = isSurpportGL2();
        if (isSurpportGL2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(airHockeyRenderer);
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            renderSet = true;

        } else {
            Toast.makeText(this, "This device doesn't surpport GL2.0.", Toast.LENGTH_SHORT).show();
        }
        setContentView(glSurfaceView);
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 将屏幕坐标转换成归一化设备坐标：
                final float normalizedX = (motionEvent.getX() / (view.getWidth() / 2f)) - 1;
                final float normalizedY = (motionEvent.getY() / (view.getHeight() / 2f)) - 1;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        glSurfaceView.post(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_MOVE:
                        glSurfaceView.post(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    private boolean isSurpportGL2() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (renderSet) {
            glSurfaceView.onResume();
        }
    }
}
