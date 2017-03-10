package com.app.jem.stockmanager.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jem on 2017/2/25.
 */

public class BackupAndRestore extends AsyncTask {
    private static final String BACKUP = "backupData";
    private static final String RESTORE = "restoreData";
    private static final int BACKUP_SUCCESS = 1;
    public static final int RESTORE_SUCCESS = 2;
    private static final int BACKUP_ERROR = 3;
    public static final int RESTORE_NOFLEERROR = 4;
    private Context context;
    private String[] upFileList, downFileList;


    public BackupAndRestore(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Object[] params) {

        //建立备份文件夹
        File backup_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                , "");
        if (!backup_file.exists()) {
            backup_file.mkdir();
        }

        //读取要备份的文件
        if (params[0].equals(BACKUP)) {

            return backup();

        } else if (params[0].equals(RESTORE)) {

            return restore();

        } else {
            return null;
        }

    }

    private Integer restore() {
        //从sd卡获得还原文件
        File downFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/backup_data");

        if (!downFile.exists()) {
            downFile.mkdir();
        }
        downFileList = downFile.list();

        //还原
        for (int i = 0; i < downFileList.length; i++) {
            File restore = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/backup_data/" + downFileList[i]);
            File restore_files = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/com.app" +
                    ".jem.stockmanager/databases/" + downFileList[i]);

            try {
                if (restore_files.exists()){
                    restore_files.delete();
                }
                if (restore.exists()){
                    restore_files.createNewFile();
                    customBufferBufferedStreamCopy(restore, restore_files);
                }

            } catch (IOException e) {
                Log.e("DbBackups", "还原数据异常");
                return RESTORE_NOFLEERROR;
            }
        }
        return RESTORE_SUCCESS;
    }

    private Integer backup() {
        //读取要备份的文件名列表
        File upFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/com.app" +
                ".jem.stockmanager/databases");

        if (!upFile.exists()) {
            upFile.mkdir();
        }
        upFileList = upFile.list();

        //备份
        for (int i = 0; i < upFileList.length; i++) {
            File backup = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/com.app" +
                    ".jem.stockmanager/databases/" + upFileList[i]);
            File backup_files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/backup_data/" + upFileList[i]);
            try {
                backup_files.createNewFile();
                customBufferBufferedStreamCopy(backup, backup_files);

            } catch (IOException e) {
                Log.e("DbBackups", "备份数据异常");
                return BACKUP_ERROR;
            }
        }
        return BACKUP_SUCCESS;
    }


    private static void customBufferBufferedStreamCopy(File source, File target) throws IOException {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(source));
            fos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            fos.close();
        }
    }


}