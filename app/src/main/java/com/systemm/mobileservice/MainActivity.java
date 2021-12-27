package com.systemm.mobileservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomer.fadingtextview.FadingTextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    Button btntara, btnbgtara;
    TextView txttaraniyor,txtpb;
    FadingTextView txtyazi;
    ImageView imgpng;
    GifImageView imggif;
    ProgressBar pb;
    int random;

    private Timer sureTimer;
    private Handler sureHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAppPermissions();
        intent = new Intent(getApplicationContext(), MyService.class);
        btnbgtara = (Button) findViewById(R.id.btnarkaplan);
        btntara = (Button) findViewById(R.id.btntara);
        imggif = (GifImageView) findViewById(R.id.gif);
        imgpng = (ImageView) findViewById(R.id.imageView);
        txttaraniyor = (TextView) findViewById(R.id.txttaraniyor);
        txtyazi = (FadingTextView) findViewById(R.id.txtyazi);
        txtpb = (TextView) findViewById(R.id.txtpb);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        btntara.setOnClickListener(this);
        btnbgtara.setOnClickListener(this);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


    private void sureSayacDurdur() {
        if (sureTimer != null) sureTimer.cancel();
        sureHandler = null;
        sureTimer = null;
    }

    private void sureSayac() {
        boolean a[] = {false};
        sureSayacDurdur();
        sureHandler = new Handler();
        sureTimer = new Timer();

        TimerTask sureTimerTask = new TimerTask() {
            @Override
            public void run() {

                sureHandler.post(() -> {
                    try {


                            pb.setProgress(pb.getProgress() + 1);
                            txtpb.setText(pb.getProgress()+"%");

                            if (pb.getProgress() == 100) {
                                a[0] = true;
                                txtyazi.restart();
                                txtyazi.setVisibility(View.VISIBLE);
                                txttaraniyor.setVisibility(View.GONE);
                                imggif.setVisibility(View.GONE);
                                imgpng.setVisibility(View.VISIBLE);
                                btntara.setVisibility(View.VISIBLE);
                                startAnimation();
                                sureSayacDurdur();

                            }


                    } catch (Exception ex) {

                    }

                });

            }
        };
        if (!a[0]) {
            random = new Random().nextInt(3000) ;
            sureTimer.schedule(sureTimerTask, 0, random);
        }

    }

    public void startAnimation() {
        String[] virusyok = {"Virüs Bulunmadı!", "Telefonunuzu En İyi Şekilde", "Koruyoruz!", ""};


        txtyazi.setTexts(virusyok);
        txtyazi.setTimeout(300, TimeUnit.MILLISECONDS);
        txtyazi.forceRefresh();
    }


    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions()) {
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.FOREGROUND_SERVICE

                }, 1);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onClick(View view) {
        if (view == btnbgtara) {
            pb.setProgress(0);
            finish();

        } else if (view == btntara) {
            txtyazi.pause();
            txtyazi.setVisibility(View.GONE);
            pb.setProgress(0);
            imgpng.setVisibility(View.GONE);
            imggif.setVisibility(View.VISIBLE);
            btntara.setVisibility(View.INVISIBLE);
            txttaraniyor.setVisibility(View.VISIBLE);
            sureSayac();

        }
    }


}