package com.ps.accessservicedemo.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ps.accessservicedemo.R;
import com.ps.accessservicedemo.other.MeetAndroidApplication;
import com.ps.accessservicedemo.service.AutoGetPacketService;
import com.white.easysp.EasySP;


/**
 * Created by huyi on 19/5/9.
 */

public class BrightDialog extends Dialog {
    private SeekBar seekBar;
    private TextView brightnessTv;

    private int defaultTime;

    public BrightDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.dialog_bright);

        initView();
    }

    /**
     * 刷新亮度
     */
    public void refreshBright() {
        defaultTime = AutoGetPacketService.defaultTime;
        brightnessTv.setText(String.valueOf(defaultTime));
        seekBar.setProgress(defaultTime);
    }

    private void initView() {
        brightnessTv = (TextView) findViewById(R.id.brightness_tv);
        seekBar = (SeekBar) findViewById(R.id.brightness_seekbar);

        refreshBright();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                changeAppBrightness(seekBar.getProgress());
            }
        });

        findViewById(R.id.exit_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoGetPacketService.defaultTime = seekBar.getProgress();
                EasySP.init(MeetAndroidApplication.getInstance()).putInt("time",AutoGetPacketService.defaultTime);
                dismiss();
            }
        });
    }

}
