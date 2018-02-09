package com.prospektdev.rotationlibrary;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.prospektdev.ezrotation.RotationManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RotationManager.getInstance().start(this, true)) {
            RotationManager.getInstance().start(this, true);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    RotationManager.getInstance().stop(MainActivity.this);
                }
            }, 5_000);
        }
    }
}
