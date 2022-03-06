package com.example.usb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Observable;

public class MainActivity extends AppCompatActivity implements UsbStateReceiver.PrintLogs {

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final String TAG = MainActivity.class.getCanonicalName();

    UsbStateReceiver mUsbStateReceiver = new UsbStateReceiver();
    public TextView mlogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerBr();
        TextView serialNum = findViewById(R.id.serial_num);
        mlogs = findViewById(R.id.logs);
        mUsbStateReceiver.SerialNumber.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String serial) {
                serialNum.setText(serial);
            }
        });
    }

    public void registerBr() {
        IntentFilter filter = new IntentFilter();
        //Adding broadcast action for USB source
//        filter.addAction(ACTION_USB_STATE);
        filter.addAction (UsbManager . EXTRA_PERMISSION_GRANTED);
        filter.addAction(UsbManager. ACTION_USB_DEVICE_ATTACHED);
        filter.addAction (UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction (ACTION_USB_PERMISSION);
        registerReceiver (mUsbStateReceiver, filter);
        Log.i(TAG, "USB BroadcastReceiver Registered");
    }

    @Override
    public void printLogs(String l) {
        mlogs.append(l);
    }
}