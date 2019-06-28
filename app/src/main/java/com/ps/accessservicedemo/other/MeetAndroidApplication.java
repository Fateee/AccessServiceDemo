package com.ps.accessservicedemo.other;

import android.app.Application;


/**
 * @author donghailong
 */
public class MeetAndroidApplication extends Application {

    private static MeetAndroidApplication inst;

    public static MeetAndroidApplication getInstance() {
        return inst;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        inst = this;
        init();

    }

    private void init() {
        SingletonManager.get(CxHelper.class).init(this);
    }
}
