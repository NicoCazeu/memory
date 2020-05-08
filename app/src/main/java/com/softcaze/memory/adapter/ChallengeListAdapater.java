package com.softcaze.memory.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.softcaze.memory.R;
import com.softcaze.memory.activity.ChallengeActivity;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.listener.ChallengeAnimationListener;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.Award;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.HeaderItem;
import com.softcaze.memory.model.ListItem;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class ChallengeListAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private static final float TRANSPARENCY = 0.3F;
    private List<ListItem> listChallenge;
    private ChallengeAnimationListener challengeAnimationListener;
    private List<Boolean> hasFoundMode = new ArrayList<>();
    protected FirebaseAnalytics firebaseAnalytics;

    public ChallengeListAdapater(List<ListItem> listChallenge, ChallengeAnimationListener listener) {
        this.listChallenge = listChallenge;
        this.challengeAnimationListener = listener;

        for(GameMode mode: GameMode.values()) {
            hasFoundMode.add(false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(parent.getContext());
        if(viewType == TYPE_HEADER) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_title_row, null);
            return new ViewHeaderHolder(itemLayoutView);
        } else {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_row, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            ChallengeListAdapater.ViewHolder viewHolder = new ChallengeListAdapater.ViewHolder(itemLayoutView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHeaderHolder) {
            final HeaderItem currentItem = (HeaderItem) listChallenge.get(position);
            final ViewHeaderHolder viewHeaderHolder = (ViewHeaderHolder) holder;
            viewHeaderHolder.gameModeLabel.setText(currentItem.getResId());
        } else {
            final Challenge currentItem = (Challenge) listChallenge.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.labelChallenge.setText(currentItem.getChallengeLabel());
            viewHolder.countAward.setText(String.valueOf(currentItem.getCountAward()));

            viewHolder.imgAward.setImageResource(currentItem.getAwardChallengeType().getResId());

            viewHolder.imgAward.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    viewHolder.imgAward.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int[] locations = new int[2];
                    viewHolder.imgAward.getLocationInWindow(locations);
                    int x = locations[0];
                    int y = locations[1];
                    viewHolder.award = new Award(x, y);
                }
            });

            if(currentItem.isUnlockChallenge()) {
                viewHolder.relativeAward.setAlpha(1F);
                viewHolder.relativeChallenge.setAlpha(1F);

                if(currentItem.isGetAward()) {
                    viewHolder.relativeAward.setVisibility(View.INVISIBLE);
                    viewHolder.getAward.setVisibility(View.INVISIBLE);
                }
                else {
                    viewHolder.relativeAward.setVisibility(View.VISIBLE);
                    viewHolder.getAward.setVisibility(View.VISIBLE);
                }
            }
            else {
                viewHolder.relativeAward.setAlpha(TRANSPARENCY);
                viewHolder.relativeChallenge.setAlpha(TRANSPARENCY);

                if(!currentItem.isGetAward()) {
                    viewHolder.getAward.setVisibility(View.INVISIBLE);
                    viewHolder.relativeAward.setVisibility(View.VISIBLE);
                }
            }

            viewHolder.relativeChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ChallengeActivity.isAnimGetAwardsFinished()) {
                        AnimationUtil.btnClickedAnimation(view, view.getContext());
                        if(currentItem.isUnlockChallenge() && !currentItem.isGetAward()) {
                            Bundle bundle = new Bundle();
                            bundle.putString(ApplicationConstants.TAG_CHALLENGE_UNLOCK_NAME, currentItem.getChallengeLabel());
                            firebaseAnalytics.logEvent(ApplicationConstants.TAG_CLICK_GET_CHALLENGE_AWARD, bundle);
                            currentItem.setGetAward(true);
                            viewHolder.award.setAmount(currentItem.getCountAward());
                            try {
                                viewHolder.dao.open();

                                viewHolder.dao.setGetAwardChallenge(currentItem.getId(), currentItem.isGetAward());

                                if(currentItem.getAwardChallengeType().equals(AwardChallengeType.COIN)) {
                                    viewHolder.dao.setCoinUser(viewHolder.dao.getCoinUser() + currentItem.getCountAward());
                                } else {
                                    viewHolder.dao.setBonusRevealUser(viewHolder.dao.getBonusRevealUser() + currentItem.getCountAward());
                                }
                            } finally {
                                viewHolder.dao.close();
                            }

                            viewHolder.relativeAward.animate().scaleX(1.3F).scaleY(1.3F).alpha(0.2F).setDuration(200).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.relativeAward.setVisibility(View.INVISIBLE);
                                    viewHolder.getAward.setVisibility(View.INVISIBLE);

                                    if(currentItem.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
                                        viewHolder.award.setAwardChallengeType(AwardChallengeType.EYE_BONUS);
                                    } else {
                                        viewHolder.award.setAwardChallengeType(AwardChallengeType.COIN);
                                    }
                                    challengeAnimationListener.startAnimation(viewHolder.award);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listChallenge.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_CONTENT;
    }

    private boolean isPositionHeader(int position) {
        return listChallenge.get(position) instanceof HeaderItem;
    }

    public static class ViewHeaderHolder extends RecyclerView.ViewHolder {
        TextView gameModeLabel;

        public ViewHeaderHolder(View itemView) {
            super(itemView);

            gameModeLabel = (TextView) itemView.findViewById(R.id.game_mode_label);
            UIUtil.setTypeFaceText(itemView.getContext(), gameModeLabel);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView labelChallenge;
        TextView countAward;
        TextView getAward;
        ImageView imgAward;
        RelativeLayout relativeAward;
        RelativeLayout relativeChallenge;
        Dao dao;
        Award award;

        public ViewHolder(View itemView) {
            super(itemView);

            labelChallenge = (TextView) itemView.findViewById(R.id.label_challenge);
            countAward = (TextView) itemView.findViewById(R.id.count_award);
            getAward = (TextView) itemView.findViewById(R.id.get_award);
            imgAward = (ImageView) itemView.findViewById(R.id.img_award);
            relativeAward = (RelativeLayout) itemView.findViewById(R.id.relative_award);
            relativeChallenge = (RelativeLayout) itemView.findViewById(R.id.relative_challenge);
            dao = new Dao(itemView.getContext());
            award = new Award();

            UIUtil.setTypeFaceText(itemView.getContext(), labelChallenge, countAward, getAward);

            AnimationUtil.breathingAnimation(getAward);
        }
    }
}
