package com.ps.accessservicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;


public class DialogActivity extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_location_permission);
    findViewById(R.id.confirm_bt).setOnClickListener(v -> {
      Intent intent = new Intent(Settings.ACTION_SETTINGS);
      startActivity(intent);
    });
  }
}
