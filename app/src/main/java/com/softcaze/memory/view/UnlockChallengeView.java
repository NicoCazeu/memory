package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.ChallengeActivity;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

public class UnlockChallengeView extends RelativeLayout {
    protected TextView labelTxtView, amountAwardTxtView, newUnlockChallenge, getAward;
    protected ImageView imgAward;
    protected RelativeLayout relativeAward;

    public UnlockChallengeView(Context context, Challenge challenge) {
        super(context);
        init(null, 0, challenge);
    }

    private void init(AttributeSet attrs, int defStyle, Challenge challenge) {
        inflate(getContext(), R.layout.unlock_challenge, this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.setLayoutParams(params);

        getAward = (TextView) findViewById(R.id.get_award);
        newUnlockChallenge = (TextView) findViewById(R.id.new_unlock_challenge);
        labelTxtView = (TextView) findViewById(R.id.label_challenge);
        amountAwardTxtView = (TextView) findViewById(R.id.count_award);
        imgAward = (ImageView) findViewById(R.id.img_award);
        relativeAward = (RelativeLayout) findViewById(R.id.relative_award);

        UIUtil.setTypeFaceText(this.getContext(), getAward, newUnlockChallenge, labelTxtView, amountAwardTxtView);

        labelTxtView.setText(challenge.getChallengeLabel());
        amountAwardTxtView.setText(challenge.getCountAward() + "");
        imgAward.setImageResource(challenge.getAwardChallengeType().getResId());

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_from_bottom_reverse);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.GONE);
                        Intent intent = new Intent(getContext(), ChallengeActivity.class);
                        getContext().startActivity(intent);
                        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((Activity) getContext()).finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(animation);
            }
        });
    }

    public void display() {
        //AnimationUtil.playAnimation(getContext(), R.anim.translate_from_bottom, this);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_from_bottom);
        final Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.translate_from_bottom_reverse_delay);
        final View view = this;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(animation);

        AnimationUtil.breathingAnimation(getAward);
    }
}
