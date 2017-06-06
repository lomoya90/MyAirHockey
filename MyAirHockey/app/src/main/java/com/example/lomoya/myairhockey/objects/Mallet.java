package com.example.lomoya.myairhockey.objects;

import android.opengl.GLES20;

import com.example.lomoya.myairhockey.VertexArray;
import com.example.lomoya.myairhockey.shader.ColorShaderProgram;
import com.example.lomoya.myairhockey.utils.Constants;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class Mallet {
    private static final int POINT_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            ((POINT_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTE_PER_FLOAT);

    private final float[] mallet = new float[]{
            // x, y, R, G, B
            0f, 0.4f, 1.0f, 0.0f, 0.0f,
            0f, -0.4f, 0.0f, 1.0f, 0.0f
    };
    private VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(mallet);
    }

    public void bindData(ColorShaderProgram program) {
        vertexArray.setVertextAttrPointer(0, POINT_COMPONENT_COUNT,
                STRIDE, program.getPositionAttrLocation());
        vertexArray.setVertextAttrPointer(POINT_COMPONENT_COUNT,
                COLOR_COMPONENT_COUNT, STRIDE,
                program.getColorLocation());
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2);
    }
}
