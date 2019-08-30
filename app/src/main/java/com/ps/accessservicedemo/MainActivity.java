package com.ps.accessservicedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ps.accessservicedemo.io.Consts;
import com.ps.accessservicedemo.other.CxHelper;
import com.ps.accessservicedemo.other.MeetAndroidApplication;
import com.ps.accessservicedemo.other.SingletonManager;
import com.ps.accessservicedemo.service.AutoGetPacketService;
import com.ps.accessservicedemo.tools.CameraUtils;
import com.ps.accessservicedemo.tools.PacketUtil;
import com.ps.accessservicedemo.views.BrightDialog;
import com.white.easysp.EasySP;

import showapplist.AppsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BrightDialog brightDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        CameraUtils.getScreenSize(MainActivity.this);
    }

    private void initView() {
        SwitchCompat bt = findViewById(R.id.auto_random_play);
        boolean check = EasySP.init(MeetAndroidApplication.getInstance()).getBoolean(Consts.AUTO_PLAY,false);
        bt.setChecked(check);
        bt.setOnCheckedChangeListener((compoundButton, checked) -> {
            EasySP.init(MeetAndroidApplication.getInstance()).putBoolean(Consts.AUTO_PLAY,checked);
        });
        findViewById(R.id.openAccess).setOnClickListener(this);
        findViewById(R.id.xiaoyumoney).setOnClickListener(this);
        findViewById(R.id.lingyongmoney).setOnClickListener(this);
        findViewById(R.id.zhuanke).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
        findViewById(R.id.app_list).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openAccess:
                SingletonManager.get(CxHelper.class).openAccessSetting();
                break;
            case R.id.xiaoyumoney:
                PacketUtil.openAPP(Consts.XYZQ);
                break;
            case R.id.lingyongmoney:
                PacketUtil.openAPP(Consts.KS_PACKAGE_NAME);
                break;
            case R.id.zhuanke:
                PacketUtil.openAPP(Consts.QK_PACKAGE_NAME);
                break;
            case R.id.time:
                if (brightDialog == null) {
                    brightDialog = new BrightDialog(this);
                    brightDialog.setCanceledOnTouchOutside(false);
                } else {
                    brightDialog.refreshBright();
                }
                brightDialog.show();
                break;
            case R.id.app_list:
                startActivity(new Intent(this, AppsActivity.class));
                break;
            default:
                break;
        }
    }


}
