package com.softcaze.memory.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.LevelListAdapter;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.OnItemClickListener;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

public class LevelListActivity extends AppCompatActivity {

    private TextView txtGameMode, txtCoin, txtBonus;
    private LevelListAdapter levelListAdapter;
    private RecyclerView recyclerView;
    private ImageView imgCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        txtGameMode = (TextView) findViewById(R.id.txt_game_mode);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);

        if(GameInformation.getInstance().getCurrentMode() != null) {
            txtGameMode.setText(GameInformation.getInstance().getCurrentMode().toString(this));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgCoin = (ImageView) findViewById(R.id.img_coin);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        if(User.getInstance().getCoin() != null) {
            txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        }
        if(User.getInstance().getBonus() != null) {
            txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Level> listLevel = GameInformation.getInstance().getListLevelByGameMode(GameInformation.getInstance().getCurrentMode());
        if(listLevel != null) {
            List<LevelRow> levelRowList = GameInformation.getInstance().convertToLevelRowList(listLevel);

            if(levelRowList != null) {
                levelListAdapter = new LevelListAdapter(levelRowList);

                recyclerView.setAdapter(levelListAdapter);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(LevelListActivity.this, GameModeActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) this).toBundle());
    }
}
