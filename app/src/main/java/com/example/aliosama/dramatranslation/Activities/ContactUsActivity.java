package com.example.aliosama.dramatranslation.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactUsActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar ContactUsToolbar;
    private ImageView Logo;
    private LinearLayout Website;
    private LinearLayout email;
    private LinearLayout facebookPage;
    private LinearLayout facebookGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        try {

//          Initialize
            ContactUsToolbar = (Toolbar) findViewById(R.id.ContactUs_activity_toolbar);
            Logo = (ImageView) findViewById(R.id.ContactUs_activity_Logo);
            Website = (LinearLayout) findViewById(R.id.ContactUs_activity_Site);
            email = (LinearLayout) findViewById(R.id.ContactUs_activity_Email);
            facebookPage = (LinearLayout) findViewById(R.id.ContactUs_activity_FB_Page);
            facebookGroup = (LinearLayout) findViewById(R.id.ContactUs_activity_Group);

//          ToolBar
            setSupportActionBar(ContactUsToolbar);
            actionBar = getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.contact_us));
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

//          Actions

            Website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://ottakae.com/")));
                }
            });

            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"molas.mola@gmail.com"});
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            facebookPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://www.facebook.com/pg/ottakaefansub/";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                }
            });
            facebookGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://www.facebook.com/groups/705093886325984/";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
