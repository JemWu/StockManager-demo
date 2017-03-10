package com.app.jem.stockmanager;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jem.stockmanager.task.BLConnectThread;
import com.app.jem.stockmanager.view.RippleBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/3/5.
 */

public class Send extends Activity implements View.OnClickListener {

    private ImageView foundDevice, foundDevice2, foundDevice3;
    private TextView phoneName, phoneName2, phoneName3;
    final private static String MY_UUID = "1AED5597-62E2-69A1-6D13-6358C8DA58F3";
    private BluetoothDevice device;
    private BluetoothSocket mmSocket;
    private BluetoothSocket tmp = null;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> mArrayAdapter = new ArrayList<>();
    private List<ImageView> deviceList = new ArrayList<>();
    private List<TextView> nameList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);
        initView();

        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();


        //异步线程查找设备
        new Thread() {
            @Override
            public void run() {
                super.run();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);
                mBluetoothAdapter.startDiscovery();
            }
        }.start();


    }

    private void initView() {
        foundDevice = (ImageView) findViewById(R.id.foundDevice);
        foundDevice2 = (ImageView) findViewById(R.id.foundDevice2);
        foundDevice3 = (ImageView) findViewById(R.id.foundDevice3);
        deviceList.add(foundDevice);
        deviceList.add(foundDevice2);
        deviceList.add(foundDevice3);
        foundDevice.setOnClickListener(this);
        foundDevice2.setOnClickListener(this);
        foundDevice3.setOnClickListener(this);

        phoneName = (TextView) findViewById(R.id.phoneName);
        phoneName2 = (TextView) findViewById(R.id.phoneName2);
        phoneName3 = (TextView) findViewById(R.id.phoneName3);
        nameList.add(phoneName);
        nameList.add(phoneName2);
        nameList.add(phoneName3);
    }


    private void foundDevice(int i, String phone_name) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(deviceList.get(i), "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleXAnimator1 = ObjectAnimator.ofFloat(nameList.get(i), "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator1);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(deviceList.get(i), "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        ObjectAnimator scaleYAnimator1 = ObjectAnimator.ofFloat(nameList.get(i), "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator1);
        animatorSet.playTogether(animatorList);
        deviceList.get(i).setVisibility(View.VISIBLE);
        nameList.get(i).setVisibility(View.VISIBLE);
        nameList.get(i).setText(phone_name);
        animatorSet.start();
    }


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name to an array adapter to show in a ListView

                if (mArrayAdapter.indexOf(device) == -1) {
                    mArrayAdapter.add(device);
                }


                if (!mArrayAdapter.isEmpty()) {
                    for (int i = 0; i < mArrayAdapter.size(); i++) {
                        foundDevice(i, mArrayAdapter.get(i).getName());
                    }
                }

            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foundDevice:
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.setDuration(200);
                animatorSet1.setInterpolator(new AccelerateDecelerateInterpolator());
                ArrayList<Animator> animatorList1 = new ArrayList<Animator>();
                ObjectAnimator scaleXAnimator1 = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 1f, 0.7f, 1f);
                animatorList1.add(scaleXAnimator1);
                ObjectAnimator scaleYAnimator1 = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 1f, 0.7f, 1f);
                animatorList1.add(scaleYAnimator1);
                animatorSet1.playTogether(animatorList1);
                animatorSet1.start();
                if (!(mArrayAdapter.get(0)==null)) {
                    new BLConnectThread(mArrayAdapter.get(0), mBluetoothAdapter).start();
                }
                break;

            case R.id.foundDevice2:
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.setDuration(200);
                animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
                ArrayList<Animator> animatorList2 = new ArrayList<Animator>();
                ObjectAnimator scaleXAnimator2 = ObjectAnimator.ofFloat(foundDevice2, "ScaleX", 1f, 0.7f, 1f);
                animatorList2.add(scaleXAnimator2);
                ObjectAnimator scaleYAnimator2 = ObjectAnimator.ofFloat(foundDevice2, "ScaleY", 1f, 0.7f, 1f);
                animatorList2.add(scaleYAnimator2);
                animatorSet2.playTogether(animatorList2);
                animatorSet2.start();
                if (!(mArrayAdapter.get(1)==null)) {
                    new BLConnectThread(mArrayAdapter.get(1), mBluetoothAdapter).start();
                }
                break;

            case R.id.foundDevice3:
                AnimatorSet animatorSet3 = new AnimatorSet();
                animatorSet3.setDuration(200);
                animatorSet3.setInterpolator(new AccelerateDecelerateInterpolator());
                ArrayList<Animator> animatorList3 = new ArrayList<Animator>();
                ObjectAnimator scaleXAnimator3 = ObjectAnimator.ofFloat(foundDevice3, "ScaleX", 1f, 0.7f, 1f);
                animatorList3.add(scaleXAnimator3);
                ObjectAnimator scaleYAnimator3 = ObjectAnimator.ofFloat(foundDevice3, "ScaleY", 1f, 0.7f, 1f);
                animatorList3.add(scaleYAnimator3);
                animatorSet3.playTogether(animatorList3);
                animatorSet3.start();
                if (!(mArrayAdapter.get(2)==null)){
                    new BLConnectThread(mArrayAdapter.get(2), mBluetoothAdapter).start();
                }

                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Register the BroadcastReceiver
        unregisterReceiver(mReceiver);
    }
}
