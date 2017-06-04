package com.example.lomoya.myairhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

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

    private static final int POINT_COMPONENT_COUNT = 2;

    private static final int BYTES_PER_FLOAT = 4; // 一个float型占用4个字节
    private FloatBuffer verticesData;
    private Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVertices = new float[]{
                0f, 0f,
                0f, 14f,
                9f, 14f,
                9f, 0f};

        float[] tableVerticesWithTriangles = new float[]{
                0f, 0f,
                9f, 14f,
                0f, 14f,

                0f, 0f,
                9f, 0f,
                9f, 14f
        };

        float[] lineVertices = new float[] {
                0f,7f,
                 9f,7f
        };

        float[] pointVertices = new float[] {
                4.5f, 2f,
                4.5f, 12f
        };

        verticesData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();




    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertextShaderId = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShaderResource);

        int programId = ShaderHelper.linkProgram(vertextShaderId, fragmentShaderId);
        if (Logger.DEBUG) {
            ShaderHelper.validProgram(programId);
        }

        GLES20.glUseProgram(programId);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }
}
