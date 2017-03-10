package com.app.jem.stockmanager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.jem.stockmanager.utils.BackupAndRestore;

/**
 * Created by jem on 2017/3/5.
 */

public class Settings extends Activity implements View.OnClickListener {

    private Button backup,restore,sync;
    private static int RESULTCODE=1;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        initView();
    }

    private void initView() {
        backup = (Button) findViewById(R.id.backup);
        restore = (Button) findViewById(R.id.restore);
        sync = (Button) findViewById(R.id.sync);
        backup.setOnClickListener(this);
        restore.setOnClickListener(this);
        sync.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backup:
                new BackupAndRestore(this).execute("backupData");
                Toast.makeText(this,"备份已完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.restore:
                new BackupAndRestore(this).execute("restoreData");
                Toast.makeText(this,"还原已完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sync:
                //判断是否支持蓝牙
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(this,"该设备不支持蓝牙功能",Toast.LENGTH_SHORT).show();
                }else{
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,RESULTCODE);
                    }else if (mBluetoothAdapter.isEnabled()){
                        intent =new Intent(this,Sync.class);
                        startActivity(intent);

                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent =new Intent(this,Sync.class);
        startActivity(intent);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoon_in,R.anim.zoon_out);
    }
}
