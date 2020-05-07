package com.softcaze.memory.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.LevelListAdapter;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.OnItemClickListener;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LevelListActivity extends Activity {

    private TextView txtGameMode, txtCoin, txtBonus;
    private LevelListAdapter levelListAdapter;
    private RecyclerView recyclerView;
    private ImageView imgCoin;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        txtGameMode = (TextView) findViewById(R.id.txt_game_mode);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);

        UIUtil.setTypeFaceText(this, txtGameMode, txtCoin, txtBonus);

        dao = new Dao(this);

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
        loadLevelsDatabase(listLevel);

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
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void loadLevelsDatabase(List<Level> listLevel) {
        try {
            dao.open();
            if(listLevel != null) {
                for (Level level : listLevel) {
                    CareerLevel careerLevel = (CareerLevel) level;

                    careerLevel.setState(dao.getStateCareerLevel(level.getId()));
                    careerLevel.setTouchUsed(dao.getBestScoreCareerLevel(level.getId()));
                    careerLevel.setNumberStar(dao.getNumberStarCareerLevel(level.getId()));
                }
            }
        } finally {
            dao.close();
        }
    }
}
