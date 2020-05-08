package com.softcaze.memory.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.ChallengeListAdapater;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.listener.ChallengeAnimationListener;
import com.softcaze.memory.model.Award;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.HeaderItem;
import com.softcaze.memory.model.ListItem;
import com.softcaze.memory.model.Position;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.MathUtil;
import com.softcaze.memory.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChallengeActivity extends Activity implements ChallengeAnimationListener {
    public static boolean animGetAwardsFinished = true;
    protected ChallengeListAdapater challengeListAdapter;
    protected RecyclerView recyclerView;
    protected ImageView imgCoin, imgBonus;
    protected LinearLayout linearCoin, linearBonus;
    protected RelativeLayout challengeBackground;
    protected TextView textCoin, textBonus, labelFooter;
    protected int counter;
    Timer timer;
    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgCoin = (ImageView) findViewById(R.id.img_coin);
        imgBonus = (ImageView) findViewById(R.id.img_bonus);
        linearCoin = (LinearLayout) findViewById(R.id.linear_coin);
        linearBonus = (LinearLayout) findViewById(R.id.linear_bonus);
        challengeBackground = (RelativeLayout) findViewById(R.id.challenge_background);
        textCoin = (TextView) findViewById(R.id.txt_coin);
        textBonus = (TextView) findViewById(R.id.txt_bonus);
        labelFooter = (TextView) findViewById(R.id.label_footer);

        UIUtil.setTypeFaceText(this, textBonus, textCoin, labelFooter);

        dao = new Dao(this);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        textCoin.setText(User.getInstance().getCoin().getAmount() + "");
        textBonus.setText(User.getInstance().getBonus().getAmount() + "");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //challengeListAdapter = new ChallengeListAdapater(GameInformation.getInstance().getChallenges(), this);
        challengeListAdapter = new ChallengeListAdapater(getList(), this);

        recyclerView.setAdapter(challengeListAdapter);

        imgCoin.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgCoin.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] locations = new int[2];
                imgCoin.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                User.getInstance().getCoin().setX(x);
                User.getInstance().getCoin().setY(y);
            }
        });

        imgBonus.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgBonus.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] locations = new int[2];
                imgBonus.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                User.getInstance().getBonus().setX(x);
                User.getInstance().getBonus().setY(y);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChallengeActivity.this, MainMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /**
     * This method is called when user get awards
     * @param award
     */
    @Override
    public void startAnimation(final Award award) {
        setAnimGetAwardsFinished(false);
        final ImageView imgAward = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MathUtil.dpToPx(20), MathUtil.dpToPx(20));

        params.leftMargin = award.getX();
        params.topMargin = award.getY() - params.height - recyclerView.computeVerticalScrollOffset();

        Position positionAward = new Position();
        int amountGeneric = 0;

        if(award.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
            positionAward.setX(User.getInstance().getBonus().getX());
            positionAward.setY(User.getInstance().getBonus().getY());
            amountGeneric = User.getInstance().getBonus().getAmount();
            imgAward.setImageDrawable(getDrawable(R.drawable.eyes_bonus));
        } else {
            positionAward.setX(User.getInstance().getCoin().getX());
            positionAward.setY(User.getInstance().getCoin().getY());
            amountGeneric = User.getInstance().getCoin().getAmount();
            imgAward.setImageDrawable(getDrawable(R.drawable.coin));
        }

        challengeBackground.addView(imgAward, params);

        imgAward.animate().translationYBy(positionAward.getY() - award.getY())
                .translationXBy(positionAward.getX() - award.getX())
                .setDuration(300)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                challengeBackground.removeView(imgAward);
            }
        });

        final int amount = amountGeneric;

        Runnable runnableEyesBonus = new Runnable () {
            @Override
            public void run() {
                ChallengeActivity self = ChallengeActivity.this;
                self.counter++;
                int newValue = amount + self.counter;
                textBonus.setText(newValue + "");
                if (self.counter == award.getAmount()) {
                    self.counter = 0;
                    self.timer.stopTimer();
                    setAnimGetAwardsFinished(true);
                }
            }
        };

        Runnable runnableCoin = new Runnable () {
            @Override
            public void run() {
                ChallengeActivity self = ChallengeActivity.this;
                self.counter++;
                int newValue = amount + self.counter;
                textCoin.setText(newValue + "");
                if (self.counter == award.getAmount()) {
                    self.counter = 0;
                    self.timer.stopTimer();
                    setAnimGetAwardsFinished(true);
                }
            }
        };

        if(award.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
            timer = new Timer(runnableEyesBonus, 50, true);
            User.getInstance().getBonus().setAmount(User.getInstance().getBonus().getAmount() + award.getAmount());
        } else {
            timer = new Timer(runnableCoin, 50, true);
            User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + award.getAmount());
        }

        try {
            dao.open();

            dao.setCoinUser(User.getInstance().getCoin().getAmount());
            dao.setBonusRevealUser(User.getInstance().getBonus().getAmount());
        } finally {
            dao.close();
        }

    }

    public static boolean isAnimGetAwardsFinished() {
        return animGetAwardsFinished;
    }

    public static void setAnimGetAwardsFinished(boolean animGetAwardsFinished) {
        ChallengeActivity.animGetAwardsFinished = animGetAwardsFinished;
    }

    private List<ListItem> getList() {
        List<ListItem> listChallenges = new ArrayList<>();
        HashMap<GameMode, Boolean> alreadyAdded = new HashMap<>();

        for(GameMode mode: GameMode.values()) {
            alreadyAdded.put(mode, false);
        }

        for(Challenge challenge: GameInformation.getInstance().getChallenges()) {
            if(!alreadyAdded.get(challenge.getMode())) {
                alreadyAdded.put(challenge.getMode(), true);
                listChallenges.add(new HeaderItem(challenge.getMode().getResId()));
            }
            listChallenges.add(challenge);
        }

        return listChallenges;
    }
}
