package com.ps.accessservicedemo.tools;

import android.content.Context;
import android.content.Intent;

import com.ps.accessservicedemo.other.MeetAndroidApplication;

public class PacketUtil {
    public static void openAPP(String pkg) {
        Intent intent = MeetAndroidApplication.getInstance().getPackageManager().getLaunchIntentForPackage(pkg);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MeetAndroidApplication.getInstance().startActivity(intent);
        }
    }
}
