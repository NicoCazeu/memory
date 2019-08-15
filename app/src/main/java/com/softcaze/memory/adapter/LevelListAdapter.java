package com.softcaze.memory.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.OnItemClickListener;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 26/06/2019.
 */

public class LevelListAdapter extends RecyclerView.Adapter<LevelListAdapter.ViewHolder> {
    private List<LevelRow> levelRows;

    public LevelListAdapter(List<LevelRow> levelRows) {
        this.levelRows = levelRows;
    }

    @Override
    public LevelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_row, null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LevelListAdapter.ViewHolder holder, final int position) {
        LevelState currentColumn;

        // Manage lock/unlock state
        for(int i = 0; i < holder.contentLock.size(); i++) {
            if((i+1)%3 == 1) {
                currentColumn = levelRows.get(position).getLevel1().getState();
            }
            else if((i+1)%3 == 2) {
                currentColumn = levelRows.get(position).getLevel2().getState();
            }
            else if((i+1)%3 == 0) {
                currentColumn = levelRows.get(position).getLevel3().getState();
            }
            else {
                currentColumn = LevelState.UNLOCK;
            }

            if(currentColumn.equals(LevelState.UNLOCK)) {
                holder.contentLock.get(i).setVisibility(View.INVISIBLE);
                holder.contentUnLock.get(i).setVisibility(View.VISIBLE);
            }
            else {
                holder.contentLock.get(i).setVisibility(View.VISIBLE);
                holder.contentUnLock.get(i).setVisibility(View.INVISIBLE);
            }
        }

        // Manage label
        holder.numLevel.get(0).setText(String.valueOf(levelRows.get(position).getLevel1().getId()));
        holder.numLevel.get(1).setText(String.valueOf(levelRows.get(position).getLevel2().getId()));
        holder.numLevel.get(2).setText(String.valueOf(levelRows.get(position).getLevel3().getId()));

        // Maange star
        int starScoreInt = R.drawable.star_score;
        int starEmptyInt = R.drawable.star_empty;

        for(int i = 0; i < holder.levelStar.size(); i++) {
            int nbrStar = 0;

            if((i+1)%3 == 1) {
                nbrStar = levelRows.get(position).getLevel1().getStarNumber();
            }
            else if((i+1)%3 ==2) {
                nbrStar = levelRows.get(position).getLevel2().getStarNumber();
            }
            else if((i+1)%3 == 0) {
                nbrStar = levelRows.get(position).getLevel3().getStarNumber();
            }
            else {
                nbrStar = 0;
            }

            switch (nbrStar) {
                case 1:
                    holder.levelStar.get(i).get(0).setImageResource(starScoreInt);
                    holder.levelStar.get(i).get(1).setImageResource(starEmptyInt);
                    holder.levelStar.get(i).get(2).setImageResource(starEmptyInt);
                    break;
                case 2:
                    holder.levelStar.get(i).get(0).setImageResource(starScoreInt);
                    holder.levelStar.get(i).get(1).setImageResource(starScoreInt);
                    holder.levelStar.get(i).get(2).setImageResource(starEmptyInt);
                    break;
                case 3:
                    holder.levelStar.get(i).get(0).setImageResource(starScoreInt);
                    holder.levelStar.get(i).get(1).setImageResource(starScoreInt);
                    holder.levelStar.get(i).get(2).setImageResource(starScoreInt);
                    break;
                default:
                    holder.levelStar.get(i).get(0).setImageResource(starEmptyInt);
                    holder.levelStar.get(i).get(1).setImageResource(starEmptyInt);
                    holder.levelStar.get(i).get(2).setImageResource(starEmptyInt);
                    break;
            }
        }

        // Manage onclick content level
        for(int i = 0; i < holder.contentLevel.size(); i++) {
            final int index = i;
            holder.contentLevel.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameInformation.getInstance().setGoNextLevel(false);
                    switch (index) {
                        case 0:
                            if(levelRows.get(position).getLevel1().getState().equals(LevelState.UNLOCK)) {
                                Intent intent = new Intent(view.getContext(), GameActivity.class);
                                GameInformation.getInstance().setNumCurrentLevel((position*3) + index + 1);
                                view.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle());
                            }
                            break;
                        case 1:
                            if(levelRows.get(position).getLevel2().getState().equals(LevelState.UNLOCK)) {
                                Intent intent = new Intent(view.getContext(), GameActivity.class);
                                GameInformation.getInstance().setNumCurrentLevel((position*3) + index + 1);
                                view.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle());
                            }
                            break;
                        case 2:
                            if(levelRows.get(position).getLevel3().getState().equals(LevelState.UNLOCK)) {
                                Intent intent = new Intent(view.getContext(), GameActivity.class);
                                GameInformation.getInstance().setNumCurrentLevel((position*3) + index + 1);
                                view.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle());
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return levelRows.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        List<TextView> numLevel;

        List<RelativeLayout> contentLock;
        List<RelativeLayout> contentUnLock;
        List<RelativeLayout> contentLevel;

        List<List<ImageView>> levelStar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            numLevel = new ArrayList<>();
            contentLock = new ArrayList<>();
            contentUnLock = new ArrayList<>();
            contentLevel = new ArrayList<>();
            levelStar = new ArrayList<>();

            List<ImageView> levelListStar1 = new ArrayList<>();
            List<ImageView> levelListStar2 = new ArrayList<>();
            List<ImageView> levelListStar3 = new ArrayList<>();

            numLevel.add((TextView) itemLayoutView.findViewById(R.id.level1));
            numLevel.add((TextView) itemLayoutView.findViewById(R.id.level2));
            numLevel.add((TextView) itemLayoutView.findViewById(R.id.level3));

            contentLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_lock1));
            contentLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_lock2));
            contentLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_lock3));

            contentUnLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_unlock1));
            contentUnLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_unlock2));
            contentUnLock.add((RelativeLayout) itemLayoutView.findViewById(R.id.content_unlock3));

            contentLevel.add((RelativeLayout) itemLayoutView.findViewById(R.id.relative_1));
            contentLevel.add((RelativeLayout) itemLayoutView.findViewById(R.id.relative_2));
            contentLevel.add((RelativeLayout) itemLayoutView.findViewById(R.id.relative_3));

            levelListStar1.add((ImageView) itemLayoutView.findViewById(R.id.level1_star1));
            levelListStar1.add((ImageView) itemLayoutView.findViewById(R.id.level1_star2));
            levelListStar1.add((ImageView) itemLayoutView.findViewById(R.id.level1_star3));

            levelListStar2.add((ImageView) itemLayoutView.findViewById(R.id.level2_star1));
            levelListStar2.add((ImageView) itemLayoutView.findViewById(R.id.level2_star2));
            levelListStar2.add((ImageView) itemLayoutView.findViewById(R.id.level2_star3));

            levelListStar3.add((ImageView) itemLayoutView.findViewById(R.id.level3_star1));
            levelListStar3.add((ImageView) itemLayoutView.findViewById(R.id.level3_star2));
            levelListStar3.add((ImageView) itemLayoutView.findViewById(R.id.level3_star3));

            levelStar.add(levelListStar1);
            levelStar.add(levelListStar2);
            levelStar.add(levelListStar3);
        }
    }
}
