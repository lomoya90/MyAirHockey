package com.example.lomoya.myairhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";

    private static final int POINT_COMPONENT_COUNT = 2;

    private static final int BYTES_PER_FLOAT = 4; // 一个float型占用4个字节
    private FloatBuffer vertexData;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;


    private Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = new float[]{
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // outer border
                -0.55f, -0.55f,
                0.55f, 0.55f,
                -0.55f, 0.55f,

                -0.55f, -0.55f,
                0.55f, -0.55f,
                0.55f, 0.55f,
                // line
                -0.5f, 0f,
                0.5f, 0f,
                // pointer
                0f, -0.25f,
                0f, 0.25f,

                // ball
                0f, 0f
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
        uColorLocation = GLES20.glGetUniformLocation(programId, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POINT_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.i(TAG, "---draw fram----");
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // draw outer border
        GLES20.glUniform4f(uColorLocation, 0.5f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 6);

        // draw inner border
        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // draw line
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 12, 2);

        // draw pointer
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 14, 1);
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 15, 1);

        // draw ball
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glVertexAttribPointer();
        GLES20.glDrawArrays(GLES20.GL_POINTS, 16, 1);
    }
}
