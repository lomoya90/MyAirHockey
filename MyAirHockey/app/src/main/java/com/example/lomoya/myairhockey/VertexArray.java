package com.example.lomoya.myairhockey;

import android.opengl.GLES20;

import com.example.lomoya.myairhockey.utils.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class VertexArray {

    private final FloatBuffer vertexFloatBuffer;

    public VertexArray(float[] vertexData) {
        vertexFloatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertextAttrPointer(int offset, int componentCount, int stride, int vertexLocation) {
        vertexFloatBuffer.position(offset);
        GLES20.glVertexAttribPointer(vertexLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, vertexFloatBuffer);
        GLES20.glEnableVertexAttribArray(vertexLocation);

        vertexFloatBuffer.position(0);
    }
}
