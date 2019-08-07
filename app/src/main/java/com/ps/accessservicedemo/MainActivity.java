package com.ps.accessservicedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ps.accessservicedemo.other.CxHelper;
import com.ps.accessservicedemo.other.SingletonManager;
import com.ps.accessservicedemo.service.AutoGetPacketService;
import com.ps.accessservicedemo.tools.CameraUtils;
import com.ps.accessservicedemo.tools.PacketUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        CameraUtils.getScreenSize(MainActivity.this);
    }

    private void initView() {
        findViewById(R.id.openAccess).setOnClickListener(this);
        findViewById(R.id.xiaoyumoney).setOnClickListener(this);
        findViewById(R.id.lingyongmoney).setOnClickListener(this);
        findViewById(R.id.zhuanke).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openAccess:
                SingletonManager.get(CxHelper.class).openAccessSetting();
                break;
            case R.id.xiaoyumoney:
                PacketUtil.openAPP(AutoGetPacketService.XYZQ);
                break;
            case R.id.lingyongmoney:
                PacketUtil.openAPP(AutoGetPacketService.LYQ);
                break;
            case R.id.zhuanke:
                PacketUtil.openAPP(AutoGetPacketService.ZHUANKE);
                break;
            default:
                break;
        }
    }


}
