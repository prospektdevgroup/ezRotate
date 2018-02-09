package com.prospektdev.ezrotation;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class OverdrawService extends Service {
    private static final int FOREGROUND_NOTIFICATION_ID = 143;
    private final static int DISABLE_AUTO_ROTATION = 0;

    final static String ACTION_START = "start.action";
    final static String ACTION_STOP = "stop.action";

    private WindowManager windows;
    private View view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windows = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_START:
                    actionStart();
                    break;
                case ACTION_STOP:
                    actionStop();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void actionStart() {
        final Notification notification = NotificationHelper.createForegroundNotification(this,
                "Overdraw service",
                "Rotate screen");
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);
        saveUserSettings();
        updateSettings(DISABLE_AUTO_ROTATION, Surface.ROTATION_90);
        draw();
    }

    private void actionStop() {
        restoreUserSettings();
        stopDraw();
        stopSelf();
    }

    private void updateSettings(int accelerometerRotation,
                                int userRotation) {
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION,
                accelerometerRotation);
        Settings.System.putInt(getContentResolver(), Settings.System.USER_ROTATION,
                userRotation);
    }

    private void saveUserSettings() {
        try {
            final int accelerometerRotation = Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
            final int userRotation = Settings.System.getInt(getContentResolver(),
                    Settings.System.USER_ROTATION);
            Pref.saveAccelerometerRotation(this, accelerometerRotation);
            Pref.saveUserRotation(this, userRotation);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void restoreUserSettings() {
        final int accelerometerRotation = Pref.getAccelerometerRotation(this);
        final int userRotation = Pref.getUserRotation(this);
        if (accelerometerRotation != Pref.DEFAULT_VALUE && userRotation != Pref.DEFAULT_VALUE) {
            updateSettings(accelerometerRotation, userRotation);
            Pref.clear(this);
        }
    }

    private void draw() {
        view = new View(this);
        windows.addView(view, generateLayout());
        view.setVisibility(View.VISIBLE);
    }

    private void stopDraw() {
        if (view != null) {
            windows.removeView(view);
            view = null;
        }
    }

    @NonNull
    private WindowManager.LayoutParams generateLayout() {
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.alpha = 0f;
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        return layoutParams;
    }
}
