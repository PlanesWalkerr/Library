package com.makhovyk.android.loglibrary;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by misha on 3/30/18.
 */

public class Logger {

    private static final int BUFFER = 1024;

    private static boolean isZipable = true;
    private static boolean rewriteOnFilling = true;
    private static long maxSize = 1024;
    private static String PATH = "";
    private static File file;
    private static File directory;
    private static boolean isEnabled = true;
    private static String appFolderName = "LogLibrary";
    private static String filename = "log.dat";


    public static int d(String tag, String msg) {
        int result = 0;
        if (isEnabled) {
            configDir();
            file = new File(directory, filename);
            Log.d(tag, PATH);

            try {
                if (!file.exists()) {
                    file.createNewFile();

                }
                if ((file.length() > (maxSize * maxSize)) && rewriteOnFilling) {
                    Log.d(tag, "size " + String.valueOf((double) file.length() / (maxSize * maxSize)) + "Mb");
                    new FileOutputStream(file);
                }
                String timeLog = new SimpleDateFormat("dd.MM.yy hh:mm:ss").format(new Date());
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                String line = timeLog + " (" + tag + ")\t" + msg + "\n";
                bw.append(line);
                bw.close();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public static int e(String tag, String msg, String error) {

        int result = 0;
        configDir();
        file = new File(directory, filename);
        Log.d(tag, PATH);

        try {
            if (!file.exists()) {
                file.createNewFile();

            }
            if ((file.length() > (maxSize * maxSize)) && rewriteOnFilling) {
                Log.d(tag, "size " + String.valueOf((double) file.length() / (maxSize * maxSize)) + "Mb");
                new FileOutputStream(file);
            }
            String timeLog = new SimpleDateFormat("dd.MM.yy hh:mm:ss").format(new Date());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            String line = timeLog + " (" + tag + ")\t" + msg + "\t" + error + "\n";
            bw.append(line);
            bw.close();
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static File zipLog() {
        if (isZipable) {
            configDir();
            file = new File(directory, filename);
            return zip(directory + "/" + filename, directory + "/zipLog.zip");
        }
        return null;
    }

    private static File zip(String file, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(zipFileName);
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled(boolean isEnabled) {
        Logger.isEnabled = isEnabled;
    }

    public static String getFilename() {
        return filename;
    }

    public static void setFilename(String filename) {
        configDir();
        File oldFile = new File(directory, Logger.filename);
        File latestname = new File(directory, filename);
        oldFile.renameTo(latestname);
        Logger.filename = filename;
    }

    public static boolean isZipable() {
        return isZipable;
    }

    public static void setZipable(boolean isZipable) {
        Logger.isZipable = isZipable;
    }

    public static long getMaxSize() {
        return maxSize;
    }

    public static void setMaxSize(long maxSize) {
        Logger.maxSize = maxSize;
    }

    public static boolean isRewriteOnFilling() {
        return rewriteOnFilling;
    }

    public static void setRewriteOnFilling(boolean rewriteOnFilling) {
        Logger.rewriteOnFilling = rewriteOnFilling;
    }

    private static void configDir() {
        PATH = Environment.getExternalStorageDirectory().getPath() + "/" + appFolderName + "/";
        directory = new File(PATH);
        directory.mkdirs();
    }

}
