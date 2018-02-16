package com.alexeyosadchy.giphy.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.alexeyosadchy.giphy.di.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FileUtils {

    public static String getEmptyImageFilePath(@ApplicationContext final Context context) throws IOException {
        @SuppressLint("SimpleDateFormat")
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "GIF_" + timeStamp + ".gif";
        final File storageDir = context.getFilesDir();
        storageDir.mkdir();
        final File image = new File(storageDir, imageFileName);
        return image.getAbsolutePath();
    }
}
