package com.example.lomoya.myairhockey.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Lomoya on 2017/6/4.
 */

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder builder = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                builder.append(nextLine);
                builder.append("\n");
            }
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource not found:" + resId, e);
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource:" + resId, e);
        }
        return builder.toString();
    }
}
