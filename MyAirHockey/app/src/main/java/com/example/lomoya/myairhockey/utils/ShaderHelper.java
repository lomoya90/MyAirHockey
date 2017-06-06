package com.example.lomoya.myairhockey.utils;

import android.opengl.GLES20;
import android.util.Log;

/*
 * Created by Lomoya on 2017/6/4.
 */

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            Logger.i(TAG, "Could not create new shader.");
            return 0;
        }

        // 上传和编译shader代码
        GLES20.glShaderSource(shaderId, shaderCode);
        GLES20.glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        // 获取shaderId的编译状态，并将状态存入到compileStatus数组中的第0个位置
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                + GLES20.glGetShaderInfoLog(shaderId));

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            Log.d(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderId;

    }

    /**
     * 将顶点着色器和片段着色器链接在一起
     *
     * @param vertexShaderId
     * @param fragmentShaderId
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            Log.e(TAG, "Could not create program.");
            return 0;
        }

        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        GLES20.glLinkProgram(programObjectId);

        int[] programlinkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, programlinkStatus, 0);
        Log.w(TAG, "Result of link program:\n" + GLES20.glGetProgramInfoLog(programObjectId));

        if (programlinkStatus[0] == 0) {
            Log.e(TAG, "Link program failed.");
            GLES20.glDeleteProgram(programObjectId);
            return 0;
        }

        return programObjectId;
    }


    public static boolean validProgram(int programId) {
        GLES20.glValidateProgram(programId);

        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        if (validateStatus[0] == 0) {
            Log.e(TAG, "Results of validating program:" + validateStatus[0]
                    + "\nLog:" + GLES20.glGetProgramInfoLog(programId));
        }
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderCode, String fragmentShaderCode) {
        int programHandle;
        int vertextShaderHandle = compileVertexShader(vertexShaderCode);
        int fragmentShaderHandle = compileFragmentShader(fragmentShaderCode);

        programHandle = linkProgram(vertextShaderHandle, fragmentShaderHandle);
        if (Logger.DEBUG) {
            validProgram(programHandle);
        }
        return programHandle;
    }
}
