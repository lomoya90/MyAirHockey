package com.example.lomoya.myairhockey.shader;

import android.content.Context;
import android.opengl.GLES20;

import com.example.lomoya.myairhockey.R;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class TextureShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURECOOR = "a_TextureCoordinate";

    private int aPositionLocation;
    private int aTextureCoordinate;

    private static final String U_TEXTUREUNIT = "u_TextureUnit";
    private static final String U_MATRIX = "u_Matrix";

    private int uTextureUnitLocation;
    private int uMatrixLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinate = GLES20.glGetAttribLocation(program, A_TEXTURECOOR);

        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTUREUNIT);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
    }

    public void setUniform(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttrLocation() {
        return aPositionLocation;
    }

    public int getTextureCoorLocation() {
        return aTextureCoordinate;
    }
}
