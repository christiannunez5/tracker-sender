package com.example.trackerappv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

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
                        Log.d("SMS_RECEIVER", "Message: " + message);
                        String[] parts = message.split(",");
                        double lat = Double.parseDouble(parts[0].split(":")[1]);
                        Log.d("SMS_RECEIVER", "Latitude: " + lat);
                        double lon = Double.parseDouble(parts[1].split(":")[1]);
                        Log.d("SMS_RECEIVER", "Longitude: " + lon);

                        Intent broadcastIntent = new Intent("SMS_LOCATION_RECEIVED");
                        broadcastIntent.putExtra("latitude", lat);
                        broadcastIntent.putExtra("longitude", lon);

                        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                    }

                }
            }
        }
    }
}
