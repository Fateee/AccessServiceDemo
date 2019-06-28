package com.ps.accessservicedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ps.accessservicedemo.other.CxHelper;
import com.ps.accessservicedemo.other.SingletonManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView openAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        openAccess = findViewById(R.id.openAccess);
        openAccess.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openAccess:
                SingletonManager.get(CxHelper.class).openAccessSetting();
                break;
            default:
                break;
        }
    }
}
