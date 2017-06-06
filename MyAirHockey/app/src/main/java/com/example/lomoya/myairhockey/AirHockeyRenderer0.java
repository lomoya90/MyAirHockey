package com.example.lomoya.myairhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.lomoya.myairhockey.utils.Logger;
import com.example.lomoya.myairhockey.utils.ShaderHelper;
import com.example.lomoya.myairhockey.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Lomoya on 2017/6/4.
 */

public class AirHockeyRenderer0 implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";

    private static final int BYTES_PER_FLOAT = 4; // 一个float型占用4个字节

    private static final int POINT_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            ((POINT_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT);


    private FloatBuffer vertexData;

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[16];

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;


    private final float[] modelMatrix = new float[16];

    private Context mContext;

    public AirHockeyRenderer0(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = new float[]{
//                -0.5f, -0.5f,
//                0.5f, 0.5f,
//                -0.5f, 0.5f,
//
//                -0.5f, -0.5f,
//                0.5f, -0.5f,
//                0.5f, 0.5f,
                // x, y, R, G, B
                0f, 0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.7f, 0.7f, 0.7f, 0.7f,
                0f, -0.7f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.7f, 0.7f, 0.7f, 0.7f,
                0.5f, 0f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.7f, 0.7f, 0.7f, 0.7f,
                0f, 0.7f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.7f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.7f, 0.7f, 0.7f, 0.7f,


//                // outer border
//                -0.55f, -0.55f,
//                0.55f, 0.55f,
//                -0.55f, 0.55f,

//                -0.55f, -0.55f,
//                0.55f, -0.55f,
//                0.55f, 0.55f,
                // line
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 0f, 1f,
                // pointer
                0f, -0.25f, 0f, 0f, 1f,
                0f, 0.25f, 0f, 0f, 1f,

        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertextShaderId = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShaderResource);

        int programId = ShaderHelper.linkProgram(vertextShaderId, fragmentShaderId);
        if (Logger.DEBUG) {
            ShaderHelper.validProgram(programId);
        }

        GLES20.glUseProgram(programId);
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX);

        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POINT_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(2);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
//
//        final float aspectRatio = width > height
//                ? (float) width / (float) height : (float) height / (float) width;
//
//        if (width > height) { // landscape
//            Matrix.orthoM(projectionMatrix, 0,
//                    -aspectRatio * 0.75f, aspectRatio * 0.75f, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(projectionMatrix, 0,
//                    -1f, 1f, -aspectRatio * 0.75f, aspectRatio * 0.75f, -1f, 1f);
//        }
        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0); // 设置modelMatrix为单位矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3f); // 得到一个平移矩阵
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] tmp = new float[16];
        Matrix.multiplyMM(tmp, 0, projectionMatrix, 0, modelMatrix, 0); // projectionMatrix * modelMatrix
        System.arraycopy(tmp, 0, projectionMatrix, 0, tmp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.i(TAG, "---draw fram----");
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

//        // draw outer border
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 6);

        // draw inner border
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 10);

        // draw line
        GLES20.glDrawArrays(GLES20.GL_LINES, 10, 2);

        // draw pointer
        GLES20.glDrawArrays(GLES20.GL_POINTS, 12, 1);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 13, 1);

    }
}
