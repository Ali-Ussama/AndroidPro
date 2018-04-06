package com.example.aliosama.dramatranslation.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;

public class SettingActivity extends AppCompatActivity {
    private Toolbar SettingToolbar;
    private ActionBar actionBar;
    private CheckBox Notifications;
    private TextView ShareApp;
    private TextView Responsibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        try {
            SettingToolbar = (Toolbar) findViewById(R.id.Setting_activity_toolbar);
            setSupportActionBar(SettingToolbar);
            actionBar = getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.action_settings));

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


            Notifications = (CheckBox) findViewById(R.id.Setting_activity_Notifications);
            ShareApp = (TextView) findViewById(R.id.Setting_activity_ShareApp);
            Responsibility = (TextView) findViewById(R.id.Setting_activity_Responsibility);

            Notifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Notifications.isChecked()){

                    }else{

                    }
                }
            });

            ShareApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                    } catch (android.content.ActivityNotFoundException anfe) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                    }
                }
            });

            Responsibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                    alert.setTitle("اخلاء مسؤولية");
                    alert.setMessage("فريق Ottake ليس مرتبط بأي وسيلة مع محتوى الفيديوهات هذا التطبيق يعمل كفهرس و قاعدة بيانات للدراما الموجوده بشكل علني على الانترنت");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
