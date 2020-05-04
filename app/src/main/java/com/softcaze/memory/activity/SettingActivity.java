package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.BuildConfig;
import com.softcaze.memory.R;
import com.softcaze.memory.model.User;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

public class SettingActivity extends Activity {
    protected TextView settingSonTxt, versionApp, txtCoin, txtBonus, labelFooter, settingNotifTxt, settingLanguageTxt;
    protected TextView sonTitle, notifTitle, languageTitle, rateTitle, creditTitle, reportTitle, versionTitle;
    protected RelativeLayout son, notification, rateUs, reportABug, credits;
    protected ImageView imgCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingSonTxt = (TextView) findViewById(R.id.setting_son_txt);
        versionApp = (TextView) findViewById(R.id.version_app);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        labelFooter = (TextView) findViewById(R.id.label_footer);
        sonTitle = (TextView) findViewById(R.id.setting_son_title);
        notifTitle = (TextView) findViewById(R.id.setting_notif_title);
        languageTitle = (TextView) findViewById(R.id.setting_language_title);
        rateTitle = (TextView) findViewById(R.id.setting_rate_title);
        creditTitle = (TextView) findViewById(R.id.setting_credit_title);
        reportTitle = (TextView) findViewById(R.id.setting_report_bug_title);
        versionTitle = (TextView) findViewById(R.id.setting_version_title);
        settingNotifTxt = (TextView) findViewById(R.id.setting_notif_txt);
        settingLanguageTxt  = (TextView) findViewById(R.id.setting_language_txt);

        imgCoin = (ImageView) findViewById(R.id.img_coin);

        son = (RelativeLayout) findViewById(R.id.son);
        rateUs = (RelativeLayout) findViewById(R.id.rate_us);
        notification = (RelativeLayout) findViewById(R.id.notification);
        reportABug = (RelativeLayout) findViewById(R.id.report_bug);
        credits = (RelativeLayout) findViewById(R.id.credits);

        versionApp.setText(BuildConfig.VERSION_NAME);

        UIUtil.setTypeFaceText(this, settingSonTxt, versionApp, txtCoin, txtBonus, labelFooter, sonTitle,
                notifTitle, languageTitle, rateTitle, creditTitle, reportTitle, versionTitle, settingNotifTxt, settingLanguageTxt);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");

        son.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                Intent intent = new Intent(SettingActivity.this, CreditsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        reportABug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(SettingActivity.this, ContactFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, MainMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
