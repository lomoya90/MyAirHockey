package com.example.lomoya.myairhockey.shader;

import android.content.Context;
import android.opengl.GLES20;

import com.example.lomoya.myairhockey.utils.ShaderHelper;
import com.example.lomoya.myairhockey.utils.TextResourceReader;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class ShaderProgram {

    public final int program;

    public ShaderProgram(Context context, int vertexResId, int fragmentResId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexResId),
                TextResourceReader.readTextFileFromResource(context, fragmentResId));

        // 为何不检查了？
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }
}
