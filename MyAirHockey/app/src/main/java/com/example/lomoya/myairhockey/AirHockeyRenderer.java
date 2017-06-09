package com.example.lomoya.myairhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.lomoya.myairhockey.objects.Mallet;
import com.example.lomoya.myairhockey.objects.Puck;
import com.example.lomoya.myairhockey.objects.Table;
import com.example.lomoya.myairhockey.shader.ColorShaderProgram;
import com.example.lomoya.myairhockey.shader.TextureShaderProgram;
import com.example.lomoya.myairhockey.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRenderer";

    private Context mContext;

    private float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float[] modelViewProjectionMatrix = new float[16];

    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private Table table;
    private Mallet mallet;
    private Puck puck;

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
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureId = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);
        Matrix.setLookAtM(viewMatrix, 0,
                0f, 1f, 2.f, // eye
                0f, 0f, 0f, // 眼睛正在看的地方
                0f, 1.0f, 0f); // head

//        Matrix.setIdentityM(modelMatrix, 0); // 设置modelMatrix为单位矩阵
//        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3f); // 得到一个平移矩阵
//        Matrix.rotateM(modelMatrix, 0, -50f, 1f, 0f, 0f);
//
//        final float[] tmp = new float[16];
//        Matrix.multiplyMM(tmp, 0, projectionMatrix, 0, modelMatrix, 0); // projectionMatrix * modelMatrix
//        System.arraycopy(tmp, 0, projectionMatrix, 0, tmp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniform(modelViewProjectionMatrix, textureId);
        table.bindData(textureShaderProgram);
        table.draw();

        positionObjectInScene(0, puck.height / 2f, 0);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorShaderProgram);
        puck.draw();

        positionObjectInScene(0, mallet.height / 2f, -0.5f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(modelViewProjectionMatrix, 1.0f, 0f, 0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(0, mallet.height / 2f, 0.5f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(modelViewProjectionMatrix, 0f, 1.0f, 0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();


//        textureShaderProgram.useProgram();
//        textureShaderProgram.setUniform(projectionMatrix, textureId);
//        table.bindData(textureShaderProgram);
//        table.draw();
//
//        colorShaderProgram.useProgram();
//        colorShaderProgram.setUniform(projectionMatrix);
//        mallet.bindData(colorShaderProgram);
//        mallet.draw();
    }

    private void positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1.0f, 0f, 0f);
//        Matrix.rotateM(modelMatrix, 0, 45f, 0f, 1f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.rotateM(modelMatrix, 0, -45f, 0f, 0f, 1f);
        Matrix.translateM(modelMatrix, 0, x, y, z);
//        Matrix.rotateM(modelMatrix, 0, -45f, 1.0f, 0f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    public void handleTouchPress(float x, float y) {

    }

    public void handleTouchDrag(float x, float y) {

    }
}
