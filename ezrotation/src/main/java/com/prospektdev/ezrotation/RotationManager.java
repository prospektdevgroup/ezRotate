package com.prospektdev.ezrotation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public class RotationManager {
    private static volatile RotationManager instance;

    public static RotationManager getInstance() {
        if (instance == null) {
            synchronized (RotationManager.class) {
                if (instance == null) {
                    instance = new RotationManager();
                }
            }
        }
        return instance;
    }

    private boolean serviceStarted;

    /**
     * @param autoResolve - true if need to check permissions and resolve it
     * @return false if a service wasn't started
     * true if overdraw service was started
     */
    public boolean start(@NonNull Activity activity, boolean autoResolve) {
        if (autoResolve && !resolveWriteSettings(activity)
                && !resolveDrawOverOtherApps(activity)) {
            return false;
        }
        final Intent intent = new Intent(activity, OverdrawService.class);
        intent.setAction(OverdrawService.ACTION_START);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
        serviceStarted = true;
        return true;
    }

    /**
     * @return false if a service wasn't started
     * true if overdraw service was stopped
     */
    public boolean stop(@NonNull Activity activity) {
        if (!serviceStarted) return false;
        final Intent intent = new Intent(activity, OverdrawService.class);
        intent.setAction(OverdrawService.ACTION_STOP);
        activity.startService(intent);
        serviceStarted = false;
        return true;
    }

    /**
     * @return true if an application has write settings permission
     * false if it doesn't
     */
    public boolean resolveWriteSettings(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if an application has draw over other apps permission
     * false if it doesn't
     */
    public boolean resolveDrawOverOtherApps(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
                return false;
            }
        }
        return true;
    }
}
