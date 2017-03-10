package com.app.jem.stockmanager.task;

import android.bluetooth.BluetoothSocket;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by jem on 2017/3/9.
 */

public class FileSync extends Thread {

    private final BluetoothSocket mSocket;
    private final InputStream fileInputStream, nameInputStream;
    private final OutputStream fileOutputStream, nameOutputStream;
    private int mode;
    private String[] upFileList, downFileList;


    public FileSync(BluetoothSocket mSocket, int mode) {
        this.mSocket = mSocket;
        this.mode = mode;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mSocket.getInputStream();
            tmpOut = mSocket.getOutputStream();
        } catch (IOException e) {
        }

        fileInputStream = tmpIn;
        nameInputStream = tmpIn;
        fileOutputStream = tmpOut;
        nameOutputStream = tmpOut;

    }

    @Override
    public void run() {
        super.run();

        if (mode == 1) {
            //send

            //获取文件列表
            File upFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/com.app" +
                    ".jem.stockmanager/databases");

            if (!upFile.exists()) {
                upFile.mkdir();
            }
            upFileList = upFile.list();


            for (int i = 0; i < upFileList.length; i++) {
                try {
                    //发送文件名
                    OutputStreamWriter outputWriter = new OutputStreamWriter(nameOutputStream);
                    BufferedWriter bwName = new BufferedWriter(outputWriter);
                    bwName.write(upFileList[i]);
                    bwName.close();
                    outputWriter.close();

                    //发送文件
                    FileInputStream fileInput = new FileInputStream(Environment.getDataDirectory().getAbsolutePath() + "/data/com.app" +
                            ".jem.stockmanager/databases" + upFileList[i]);
                    int size = -1;
                    byte[] buffer = new byte[1024];

                    while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                        fileOutputStream.write(buffer, 0, size);
                    }

                    fileInput.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } else if (mode == 2) {
            //accept
            InputStreamReader streamReader = new InputStreamReader(nameInputStream);
            BufferedReader br = new BufferedReader(streamReader);
            try {
                String fileName = br.readLine();
                br.close();
                streamReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
        }
    }
}

