package com.example.lomoya.myairhockey;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Lomoya on 2017/6/4.
 */

public class FirstOpenGLProjectActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);

        boolean isSurpportGL2 = isSurpportGL2();
        if (isSurpportGL2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new AirHockeyRenderer(this));
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            renderSet = true;

        } else {
            Toast.makeText(this, "This device doesn't surpport GL2.0.", Toast.LENGTH_SHORT).show();
        }
        setContentView(glSurfaceView);
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
