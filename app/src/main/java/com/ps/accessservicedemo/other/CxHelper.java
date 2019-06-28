package com.ps.accessservicedemo.other;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * @author donghailong
 */
public class CxHelper {
    private Context context;


    public void init(Context cx) {
        if (null != cx) {
            this.context = cx.getApplicationContext();
        }
    }

    /**
     * 打开辅助功能设置
     */
    public void openAccessSetting() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
