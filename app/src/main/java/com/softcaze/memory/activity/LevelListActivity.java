package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.LevelListAdapter;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.OnItemClickListener;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

public class LevelListActivity extends AppCompatActivity {

    private TextView txtGameMode;
    private LevelListAdapter levelListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        txtGameMode = (TextView) findViewById(R.id.txt_game_mode);

        txtGameMode.setText(GameInformation.getInstance().getCurrentMode().toString(this));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
