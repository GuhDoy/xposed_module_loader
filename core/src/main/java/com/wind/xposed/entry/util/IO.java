package com.wind.xposed.entry.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IO {
    private static final int BUFFER_SIZE = 10240;

    public static void copyFile(Object input, Object output) throws IOException {
        InputStream inputStream;
        if (input instanceof InputStream)
            inputStream = (InputStream) input;
        else if (input instanceof File)
            inputStream = new FileInputStream((File) input);
        else if (input instanceof FileDescriptor)
            inputStream = new FileInputStream((FileDescriptor) input);
        else if (input instanceof String)
            inputStream = new FileInputStream((String) input);
        else throw new IOException("unknown input type");

        FileOutputStream fileOutputStream;
        if (output instanceof FileOutputStream)
            fileOutputStream = (FileOutputStream) output;
        else if (output instanceof File)
            fileOutputStream = new FileOutputStream((File) output);
        else if (output instanceof FileDescriptor)
            fileOutputStream = new FileOutputStream((FileDescriptor) output);
        else if (output instanceof String)
            fileOutputStream = new FileOutputStream((String) output);
        else throw new IOException("unknown output type");

        copyFile(inputStream, fileOutputStream);
    }

    private static void copyFile(InputStream is, FileOutputStream fos) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int byteCount;
        while ((byteCount = is.read(buffer)) != -1)
            fos.write(buffer, 0, byteCount);
        fos.flush();
        is.close();
        fos.close();
    }

    public static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
                for (File f : files)
                    deleteFiles(f);
        }
        file.delete();
    }
}