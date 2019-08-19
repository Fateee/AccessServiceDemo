package com.ps.accessservicedemo.other;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ps.accessservicedemo.DialogActivity;

import java.util.HashMap;
import java.util.Map;


/**
 * @author donghailong
 */
public class MeetAndroidApplication extends Application {

    private static MeetAndroidApplication inst;
    private String sensor_param;

    public static MeetAndroidApplication getInstance() {
        return inst;
    }

    private LocationManager locationManager;
    public static Map<String,String> mCommonParam = new HashMap();

    @Override
    public void onCreate() {
        super.onCreate();
        inst = this;
        init();
//        startLocation();
//        addCommonParam();
    }
    private void addCommonParam() {
        Map<String,String> mCommonParam = new HashMap<>();
        mCommonParam.put("url","aaaaa");
        mCommonParam.put("referrer","aaaaa");
        mCommonParam.putAll(this.mCommonParam);
//        sensor_param = JSON.toJSONString(mCommonParam);
        Log.e("huyi", this.mCommonParam.toString());
    }
    private void init() {
        SingletonManager.get(CxHelper.class).init(this);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, MyLocationListener);
    }

    /**
     * 位置监听
     */
    private LocationListener MyLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("huyi", "onLocationChanged" + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e("huyi", "onStatusChanged....");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.e("huyi", "onProviderEnabled....." );

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.e("huyi", "onProviderDisabled");
            startActivity(new Intent(inst, DialogActivity.class));
        }
    };
}
