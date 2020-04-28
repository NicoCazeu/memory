package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.model.User;
import com.softcaze.memory.util.AnimationUtil;

public class ShopActivity extends Activity {
    protected TextView txtCoin, txtBonus;
    protected ImageView imgCoin;
    protected RelativeLayout item1, item2, item3, item4, item5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);

        imgCoin = (ImageView) findViewById(R.id.img_coin);

        item1 = (RelativeLayout) findViewById(R.id.item1);
        item2 = (RelativeLayout) findViewById(R.id.item2);
        item3 = (RelativeLayout) findViewById(R.id.item3);
        item4 = (RelativeLayout) findViewById(R.id.item4);
        item5 = (RelativeLayout) findViewById(R.id.item5);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });

        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopActivity.this, MainMenuActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) this).toBundle());
    }
}
