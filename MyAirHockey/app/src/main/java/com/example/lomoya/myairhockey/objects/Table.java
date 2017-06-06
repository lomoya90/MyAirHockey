package com.example.lomoya.myairhockey.objects;

import android.opengl.GLES20;

import com.example.lomoya.myairhockey.VertexArray;
import com.example.lomoya.myairhockey.shader.TextureShaderProgram;
import com.example.lomoya.myairhockey.utils.Constants;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class Table {
    private static final int POINT_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATE_COMPONENT_COUNT = 2;
    private static final int STRIDE =
            (POINT_COMPONENT_COUNT + TEXTURE_COORDINATE_COMPONENT_COUNT) * Constants.BYTE_PER_FLOAT;

    private static final float[] vertexData =
            new float[]{
                    // x, y, S, T
                    0f, 0f, 0.5f, 0.5f,
                    -0.5f, -0.8f, 0f, 0.9f,
                    0.5f, -0.8f, 1f, 0.9f,
                    0.5f, 0.8f, 1f, 0.1f,
                    -0.5f, 0.8f, 0f, 0.1f,
                    -0.5f, -0.8f, 0f, 0.9f
            };

    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(TextureShaderProgram program) {
        vertexArray.setVertextAttrPointer(0, POINT_COMPONENT_COUNT,
                STRIDE, program.getPositionAttrLocation());
        vertexArray.setVertextAttrPointer(POINT_COMPONENT_COUNT,
                TEXTURE_COORDINATE_COMPONENT_COUNT, STRIDE,
                program.getTextureCoorLocation());
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
