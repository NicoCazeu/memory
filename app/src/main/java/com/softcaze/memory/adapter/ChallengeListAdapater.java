package com.softcaze.memory.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.OnItemClickListener;

import java.util.List;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class ChallengeListAdapater extends RecyclerView.Adapter<ChallengeListAdapater.ViewHolder> {
    private List<Challenge> listChallenge;

    private static final float TRANSPARENCY = 0.3F;

    public ChallengeListAdapater(List<Challenge> listChallenge) {
        this.listChallenge = listChallenge;
    }

    @Override
    public ChallengeListAdapater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_row, null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);

        ChallengeListAdapater.ViewHolder viewHolder = new ChallengeListAdapater.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChallengeListAdapater.ViewHolder holder, final int position) {
        holder.labelChallenge.setText(listChallenge.get(position).getChallengeLabel());
        holder.countAward.setText(String.valueOf(listChallenge.get(position).getCountAward()));

        holder.imgAward.setImageResource(listChallenge.get(position).getAwardChallengeType().getResId());

        if(listChallenge.get(position).isUnlockChallenge()) {
            holder.imgChallengeMedal.setAlpha(1F);
            holder.relativeAward.setAlpha(1F);

            if(listChallenge.get(position).isGetAward()) {
                holder.relativeAward.setVisibility(View.INVISIBLE);
            }
            else {

                holder.relativeAward.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.imgChallengeMedal.setAlpha(TRANSPARENCY);
            holder.relativeAward.setAlpha(TRANSPARENCY);
        }

        final ViewHolder holderTemp = holder;

        holder.relativeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listChallenge.get(position).isUnlockChallenge() && !listChallenge.get(position).isGetAward()) {
                    listChallenge.get(position).setGetAward(true);
                    holderTemp.relativeAward.setVisibility(View.INVISIBLE);
                    // TODO SAVE IN DATABASE + ADD AWARD ON USER
                    Log.i("************", "GET AWARD, position : " + position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChallenge.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView labelChallenge;
        TextView countAward;
        ImageView imgChallengeMedal;
        ImageView imgAward;
        RelativeLayout relativeAward;
        RelativeLayout relativeChallenge;
        RelativeLayout relativeContent;

        public ViewHolder(View itemView) {
            super(itemView);

            labelChallenge = (TextView) itemView.findViewById(R.id.label_challenge);
            countAward = (TextView) itemView.findViewById(R.id.count_award);
            imgChallengeMedal = (ImageView) itemView.findViewById(R.id.img_challenge_medal);
            imgAward = (ImageView) itemView.findViewById(R.id.img_award);
            relativeAward = (RelativeLayout) itemView.findViewById(R.id.relative_award);
            relativeChallenge = (RelativeLayout) itemView.findViewById(R.id.relative_challenge);
            relativeContent = (RelativeLayout) itemView.findViewById(R.id.relative_content);
        }
    }
}
