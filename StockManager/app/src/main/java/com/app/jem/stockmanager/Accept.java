package com.app.jem.stockmanager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.app.jem.stockmanager.task.FileSync;
import com.app.jem.stockmanager.view.RippleBackground;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by jem on 2017/3/5.
 */

public class Accept extends Activity {

    private ImageView foundDevice;
    final private static String NAME = "com.app.jem.stockmanager";
    final private static String MY_UUID="1AED5597-62E2-69A1-6D13-6358C8DA58F3";


    private BluetoothServerSocket mmServerSocket;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept);

        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString(MY_UUID));
        } catch (IOException e) { }
        mmServerSocket = tmp;

        //涟漪效果
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();


        new Thread(){
            @Override
            public void run() {
                super.run();
                BluetoothSocket socket = null;
                // Keep listening until exception occurs or a socket is returned
                while (true) {
                    try {
                        socket = mmServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }
                    // If a connection was accepted
                    if (socket != null) {
                        // Do work to manage the connection (in a separate thread)
                        new FileSync(socket).start();
                        try {
                            mmServerSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }.start();

    }




    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}


