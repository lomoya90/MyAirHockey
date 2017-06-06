package com.example.lomoya.myairhockey.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resId) {
        // 1.生成纹理对象
        int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);
        if (textureObjectId[0] == 0) {
            Logger.i(TAG, "Gen texture failed.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resId, options);
        if (bitmap == null) {
            Logger.i(TAG, "Resource " + resId + " could not be found.");
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        // 2.将纹理对象作为TEXTURE_2D绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId[0]);

        // 3.设置纹理过滤器
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // 4.加载bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        // 5.解绑texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectId[0];
    }
}
