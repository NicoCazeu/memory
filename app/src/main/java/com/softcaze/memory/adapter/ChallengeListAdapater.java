package com.softcaze.memory.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.ChallengeActivity;
import com.softcaze.memory.listener.ChallengeAnimationListener;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.Award;
import com.softcaze.memory.util.AnimationUtil;

import java.util.List;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class ChallengeListAdapater extends RecyclerView.Adapter<ChallengeListAdapater.ViewHolder> {
    private static final float TRANSPARENCY = 0.3F;

    private List<Challenge> listChallenge;
    private boolean isRun = false;
    private ChallengeAnimationListener challengeAnimationListener;

    public ChallengeListAdapater(List<Challenge> listChallenge, ChallengeAnimationListener listener) {
        this.listChallenge = listChallenge;
        this.challengeAnimationListener = listener;
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
    public void onBindViewHolder(final ChallengeListAdapater.ViewHolder holder, final int position) {
        holder.labelChallenge.setText(listChallenge.get(position).getChallengeLabel());
        holder.countAward.setText(String.valueOf(listChallenge.get(position).getCountAward()));

        holder.imgAward.setImageResource(listChallenge.get(position).getAwardChallengeType().getResId());

        holder.imgAward.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.imgAward.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] locations = new int[2];
                holder.imgAward.getLocationInWindow(locations);
                int x = locations[0];
                int y = locations[1];
                holder.award = new Award(x, y);
            }
        });

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

        holder.relativeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ChallengeActivity.isAnimGetAwardsFinished()) {
                    AnimationUtil.btnClickedAnimation(view, view.getContext());
                    if(listChallenge.get(position).isUnlockChallenge() && !listChallenge.get(position).isGetAward()) {
                        listChallenge.get(position).setGetAward(true);
                        holder.award.setAmount(listChallenge.get(position).getCountAward());
                        // TODO SAVE IN DATABASE + ADD AWARD ON USER

                        // Calcul y = ax + b
                    /*double[] resultLinearFunction = MathUtil.calculLinearFunction(User.getInstance().getCoin(), holder.coin);*/
                        holder.relativeAward.animate().scaleX(1.3F).scaleY(1.3F).alpha(0.2F).setDuration(200).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                holder.relativeAward.setVisibility(View.INVISIBLE);

                                if(listChallenge.get(position).getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
                                    holder.award.setAwardChallengeType(AwardChallengeType.EYE_BONUS);
                                } else {
                                    holder.award.setAwardChallengeType(AwardChallengeType.COIN);
                                }
                                challengeAnimationListener.startAnimation(holder.award);
                            }
                        });
                    }
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
        Award award;

        public ViewHolder(View itemView) {
            super(itemView);

            labelChallenge = (TextView) itemView.findViewById(R.id.label_challenge);
            countAward = (TextView) itemView.findViewById(R.id.count_award);
            imgChallengeMedal = (ImageView) itemView.findViewById(R.id.img_challenge_medal);
            imgAward = (ImageView) itemView.findViewById(R.id.img_award);
            relativeAward = (RelativeLayout) itemView.findViewById(R.id.relative_award);
            relativeChallenge = (RelativeLayout) itemView.findViewById(R.id.relative_challenge);
            relativeContent = (RelativeLayout) itemView.findViewById(R.id.relative_content);
            award = new Award();
        }
    }
}
