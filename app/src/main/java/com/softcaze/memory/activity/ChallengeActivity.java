package com.softcaze.memory.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.ChallengeListAdapater;
import com.softcaze.memory.adapter.LevelListAdapter;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity {
    private ChallengeListAdapater challengeListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Challenge> challengeList = new ArrayList<>();
        challengeList.add(new Challenge("Collectez 80 etoiles", AwardChallengeType.COIN, true, false, 50));
        challengeList.add(new Challenge("Terminez le niveau 150 en une seul tentative", AwardChallengeType.COIN, true, true, 1));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, true, 1));

        challengeListAdapter = new ChallengeListAdapater(challengeList);

        recyclerView.setAdapter(challengeListAdapter);
    }
}
