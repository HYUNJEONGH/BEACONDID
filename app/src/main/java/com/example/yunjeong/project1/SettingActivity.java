package com.example.yunjeong.project1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_setting);

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_addcard_toolbar);
        /* center */
        toolbar.inflateMenu(R.menu.setting_menu);
        toolbar.setTitle("Settings" +
                "");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });


    }

   public void onClick(View v) {
       switch (v.getId()) {
           case R.id.updateinfoLayout:
               Intent intent = new Intent(SettingActivity.this, UpdateListActivity.class);
               startActivity(intent);
               break;
           case R.id.logoutLayout:
               //Creating an alert dialog to confirm logout
               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
               alertDialogBuilder.setMessage("Are you sure you want to logout?");
               alertDialogBuilder.setPositiveButton("Yes",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface arg0, int arg1) {
                               //Getting out sharedpreferences
                               SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                               //Getting editor
                               SharedPreferences.Editor editor = preferences.edit();
                               editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                               editor.putString(Config.USERID_SHARED_PREF, "");
                               editor.commit();

                               //Starting login activity
                               Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                               startActivity(intent);
                           }
                       });

               alertDialogBuilder.setNegativeButton("No",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface arg0, int arg1) {

                           }
                       });

               AlertDialog alertDialog = alertDialogBuilder.create();
               alertDialog.show();
               break;
       }
   }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
