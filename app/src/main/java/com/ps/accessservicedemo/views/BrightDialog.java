package com.ps.accessservicedemo.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ps.accessservicedemo.R;
import com.ps.accessservicedemo.io.Consts;
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
    private EditText editTimeEt;

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
        defaultTime = EasySP.init(MeetAndroidApplication.getInstance()).getInt(Consts.TIME_SET,18);
        brightnessTv.setText(String.valueOf(defaultTime));
        seekBar.setProgress(defaultTime);
        editTimeEt.setText(String.valueOf(defaultTime));
        editTimeEt.setSelection(editTimeEt.getText().length());
    }

    private void initView() {
        brightnessTv = (TextView) findViewById(R.id.brightness_tv);
        seekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
        editTimeEt = (EditText) findViewById(R.id.edit_time);

        refreshBright();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessTv.setText(String.valueOf(progress));
                editTimeEt.setText(String.valueOf(progress));
                editTimeEt.setSelection(editTimeEt.getText().length());
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
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Consts.RESET_TIME_ACTION)) ;
                String setTime = brightnessTv.getText().toString();
                AutoGetPacketService.defaultTime = Integer.parseInt(setTime);
                EasySP.init(MeetAndroidApplication.getInstance()).putInt(Consts.TIME_SET,AutoGetPacketService.defaultTime);
                dismiss();
            }
        });

        editTimeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String defaultTime = s.toString();
                brightnessTv.setText(defaultTime);
            }
        });
    }

}
