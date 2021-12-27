package com.systemm.mobileservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
//telefon yeniden başlatıldığında otomatik olarak servisin başlatılması
    @Override
    public void onReceive(Context context, Intent intent) {



                Intent serviceintent = new Intent(context.getApplicationContext(), MyService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    context.startForegroundService(serviceintent);

                }
                else {
                    context.startService(serviceintent);
                }

        }
}
