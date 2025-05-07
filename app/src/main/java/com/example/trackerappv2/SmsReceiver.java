package com.example.trackerappv2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SMS_RECEIVER", "onReceive called");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = sms.getDisplayOriginatingAddress();
                    String message = sms.getMessageBody();

                    if (message.startsWith("Lat")) {

                        String[] parts = message.split(",");
                        double lat = Double.parseDouble(parts[0].split(":")[1]);
                        double lon = Double.parseDouble(parts[1].split(":")[1]);

                        Intent mapIntent = new Intent("com.example.trackerappreceiver.UPDATE_MAP");
                        mapIntent.putExtra("latitude", lat);
                        mapIntent.putExtra("longitude", lon);
                        context.sendBroadcast(mapIntent);
                    }

                }
            }
        }
    }
}
