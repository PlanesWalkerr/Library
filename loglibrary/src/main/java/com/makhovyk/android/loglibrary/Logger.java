package com.makhovyk.android.loglibrary;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by misha on 3/30/18.
 */

public class Logger {
    public static boolean isEnabled = true;
    public static String filename = "log.dat";
    public static String appFolderName = "LogLibrary";
    public static long size;

    private static String PATH = "";
    private static File file;
    private static File directory;
    private static long sizeInMB = 1024;
    private static RandomAccessFile fileWithSize;


    public static void d(String tag, String msg){
        if (isEnabled){
            PATH = Environment.getExternalStorageDirectory().getPath() + "/" + appFolderName + "/";
            directory = new File(PATH);
            directory.mkdirs();
            file = new File(directory,filename);
            Log.d(tag, PATH);
            int result = 0;
            try {
                if (!file.exists()) {
                   // file.createNewFile();
                    file.delete();
                    fileWithSize = new RandomAccessFile(PATH + "/" + filename,"rw");
                    fileWithSize.setLength(sizeInMB*sizeInMB);
                    Log.d(tag, "created");

                }
                String timeLog = new SimpleDateFormat("dd.MM.yy hh:mm:ss").format(new Date());
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(timeLog).append(" (").append(tag).append(")\t").append(msg).append("\n");
                bw.close();
                Log.d(tag, "success");
                Log.d(tag, String.valueOf(file.length()));
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (file!=null){
                    try {
                        fileWithSize.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public static void e(String tag, String msg){

    }

    public static void zipLog(){

    }
}
