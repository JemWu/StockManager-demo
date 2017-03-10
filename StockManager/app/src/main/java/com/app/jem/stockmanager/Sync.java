package com.app.jem.stockmanager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/3/5.
 */

public class Sync extends Activity implements View.OnClickListener {
    private LinearLayout send, accept;
    private static int REQUWSTCODE = 1;
    private List<String> mArrayAdapter = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);
        send = (LinearLayout) findViewById(R.id.send);
        accept = (LinearLayout) findViewById(R.id.accept);
        send.setOnClickListener(this);
        accept.setOnClickListener(this);


        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, REQUWSTCODE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                Intent send = new Intent(this, Send.class);
                startActivity(send);
                break;
            case R.id.accept:
                Intent accept = new Intent(this, Accept.class);
                startActivity(accept);
                break;
        }
    }


}
