package com.example.lomoya.myairhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.lomoya.myairhockey.objects.Mallet;
import com.example.lomoya.myairhockey.objects.Table;
import com.example.lomoya.myairhockey.shader.ColorShaderProgram;
import com.example.lomoya.myairhockey.shader.TextureShaderProgram;
import com.example.lomoya.myairhockey.utils.Logger;
import com.example.lomoya.myairhockey.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRenderer";

    private Context mContext;

    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private Table table;
    private Mallet mallet;

    private int textureId;

    public AirHockeyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram(mContext);

        table = new Table();
        mallet = new Mallet();

        textureId = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0); // 设置modelMatrix为单位矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3f); // 得到一个平移矩阵
        Matrix.rotateM(modelMatrix, 0, -50f, 1f, 0f, 0f);

        final float[] tmp = new float[16];
        Matrix.multiplyMM(tmp, 0, projectionMatrix, 0, modelMatrix, 0); // projectionMatrix * modelMatrix
        System.arraycopy(tmp, 0, projectionMatrix, 0, tmp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        textureShaderProgram.useProgram();
        textureShaderProgram.setUniform(projectionMatrix, textureId);
        table.bindData(textureShaderProgram);
        table.draw();

        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();
    }
}
