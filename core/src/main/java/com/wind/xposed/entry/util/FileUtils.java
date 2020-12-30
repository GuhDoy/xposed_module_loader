package com.wind.xposed.entry.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean isFilePermissionGranted(Context context) {
        int pid = android.os.Process.myPid();
        int uid = Process.myUid();
        return context.checkPermission(PERMISSIONS_STORAGE[0], pid, uid) == PackageManager.PERMISSION_GRANTED &&
                context.checkPermission(PERMISSIONS_STORAGE[1], pid, uid) == PackageManager.PERMISSION_GRANTED;
    }

    public static String readTextFromAssets(Context context, String assetsFileName) {
        if (context == null) {
            return null;
        }
        try {
            InputStream is = context.getAssets().open(assetsFileName);
            return readTextFromInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readTextFromInputStream(InputStream is) {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(is, "UTF-8");
            bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSafely(reader);
            closeSafely(bufferedReader);
        }
        return null;
    }

    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d("Tag", "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d("Tag", "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, String targetPath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            in.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeSafely(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
