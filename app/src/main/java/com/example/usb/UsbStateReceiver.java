package com.example.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

public class UsbStateReceiver extends BroadcastReceiver {

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final String TAG = this.getClass().getCanonicalName();
    Context mContext;
    UsbManager mUsbManager;
    MutableLiveData<String> SerialNumber = new MutableLiveData<>();
    PrintLogs mLogs;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mContext = context;
        mLogs.printLogs(TAG + "onReceive" + action);
//        ConnManDeviceRepository connManDeviceRepository = ConnManDeviceRepository.getInstance (mContext);
        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            mLogs.printLogs(TAG + "USB Attached super" + action);
            mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
            mUsbManager.requestPermission(usbDevice, permissionIntent);
            synchronized (this) {
                mLogs.printLogs(TAG + "Permission granted" + action);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(usbDevice != null){
                            SerialNumber.setValue(usbDevice.getSerialNumber());
                        }
                    }

            }

        }
    }

    public interface PrintLogs{
        void printLogs(String l);
    };
}
