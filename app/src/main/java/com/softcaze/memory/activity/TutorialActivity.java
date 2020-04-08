package com.softcaze.memory.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.listener.TutorialAnimationListener;
import com.softcaze.memory.model.Bonus;
import com.softcaze.memory.model.Card;
import com.softcaze.memory.model.CardState;
import com.softcaze.memory.model.CardType;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Position;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.MathUtil;
import com.softcaze.memory.view.CardView;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity implements TutorialAnimationListener {
    public static final int STEP_1 = 1;
    public static final int STEP_2 = 2;
    public static final int STEP_3 = 3;
    public static final int STEP_4 = 4;
    public static final int STEP_5 = 5;
    public static final int STEP_6 = 6;
    public static int NUM_STEP = STEP_1;
    protected TextView txtGameMode, txtTutorialHelper, txtBonus, txtCoin;
    protected LinearLayout gameContent, linearBonus;
    protected RelativeLayout parentTutorial;
    protected ImageView imgCoin, imgBonus;
    protected Card firstCard;
    protected Card lastCard;
    protected Bonus bonus;
    protected RelativeLayout relativeLayoutHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        txtGameMode = (TextView) findViewById(R.id.txt_game_mode);
        txtTutorialHelper = (TextView) findViewById(R.id.txt_tutorial_helper);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        gameContent = (LinearLayout) findViewById(R.id.game_content);
        linearBonus = (LinearLayout) findViewById(R.id.linear_bonus);
        parentTutorial = (RelativeLayout) findViewById(R.id.parent_tutorial);
        imgCoin = (ImageView) findViewById(R.id.img_coin);
        imgBonus = (ImageView) findViewById(R.id.img_bonus);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");

        txtGameMode.setText(GameInformation.getInstance().getCurrentMode().toString(this));

        buildContentGameByMode(GameInformation.getInstance().getCurrentMode());

        if (GameInformation.getInstance().getCardViews() != null && !GameInformation.getInstance().getCardViews().isEmpty()) {
            for(int i = 0; i < GameInformation.getInstance().getCardViews().size(); i++) {
                CardView card = GameInformation.getInstance().getCardViews().get(i);
                if(i == GameInformation.getInstance().getCardViews().size() - 1) {
                    card.flipCardOnVerso(5000, this);
                } else {
                    card.flipCardOnVerso(5000);
                }
            }
        }

        imgBonus.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgBonus.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] locations = new int[2];
                imgBonus.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                bonus = new Bonus((int) x, (int) y);
            }
        });

        linearBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NUM_STEP == STEP_4) {
                    for(CardView card: GameInformation.getInstance().getCardViews()) {
                        card.eyesBonusAction(3000, txtBonus);
                    }

                    relativeLayoutHand.removeAllViews();

                    GameInformation.getInstance().setCanPlay(false);

                    NUM_STEP = STEP_5;

                    AnimationUtil.setTextAnimation(txtTutorialHelper, getResources().getString(R.string.label_tutorial_step_5));
                }
            }
        });

        parentTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NUM_STEP == STEP_6) {
                    Intent intent = new Intent(TutorialActivity.this, LevelListActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TutorialActivity.this).toBundle());
                }
            }
        });
    }

    private void buildContentGameByMode(GameMode mode) {
        int numberCard = 4;
        int numberLane = 2;
        int elementByLane = 2;
        int numeroCard = 0;
        int previousRelative = 0;
        List<String> listCards = new ArrayList<String>();
        listCards.add(CardType.APPLE.name());
        listCards.add(CardType.BANANA.name());
        listCards.add(CardType.BANANA.name());
        listCards.add(CardType.APPLE.name());

        for(int i = 0; i < numberLane; i++) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setId(i);

            int previousCard = 0;
            for(int j = 1; j <= elementByLane; j++) {
                final CardView card = new CardView(this);
                card.setId((i * 10) + j);
                card.setType(CardType.valueOf(listCards.get(numeroCard)));

                numeroCard++;

                RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams
                        ((int) getResources().getDimension(R.dimen.card_background_width), (int) getResources().getDimension(R.dimen.card_background_height));

                if (j > 1) {
                    cardParams.addRule(RelativeLayout.RIGHT_OF, (i * 10) + previousCard);
                }

                card.setLayoutParams(cardParams);

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(NUM_STEP == STEP_2 && card.getId() == (int) 1) {
                            GameInformation.getInstance().getCardViews().remove(card);
                            card.flipCard(CardState.RECTO);

                            relativeLayoutHand.animate().translationXBy(lastCard.getX() - firstCard.getX())
                                    .translationYBy(lastCard.getY() - firstCard.getY()).setDuration(700);

                            AnimationUtil.setTextAnimation(txtTutorialHelper, getResources().getString(R.string.label_tutorial_step_3));

                            NUM_STEP = STEP_3;
                        }

                        if(NUM_STEP == STEP_3 && card.getId() == (int) 12) {
                            card.flipCard(CardState.RECTO);

                            GameInformation.getInstance().getCardViews().remove(card);

                            AnimationUtil.setTextAnimation(txtTutorialHelper, getResources().getString(R.string.label_tutorial_step_4));

                            NUM_STEP = STEP_4;

                            relativeLayoutHand.animate().translationX(bonus.getX())
                                    .translationY(bonus.getY() - relativeLayout.getHeight()/3).setDuration(700);
                        }

                        if(NUM_STEP == STEP_5 && GameInformation.getInstance().isCanPlay()) {
                            card.flipCard(CardState.RECTO);

                            GameInformation.getInstance().getCardViews().remove(card);

                            if(GameInformation.getInstance().getCardViews().size() <= 0) {
                                AnimationUtil.setTextAnimation(txtTutorialHelper, getResources().getString(R.string.label_tutorial_step_6));

                                NUM_STEP = STEP_6;
                            }
                        }
                    }
                });

                relativeLayout.addView(card);
                GameInformation.getInstance().addCardView(card);
                previousCard = j;

                /** Retrieve position of the first card **/
                if(i == 0 && j == 1) {
                    card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int[] locations = new int[2];
                            card.getLocationOnScreen(locations);
                            int x = locations[0];
                            int y = locations[1];
                            firstCard = new Card(card.getWidth(), card.getHeight(), (int) x, (int) y);
                        }
                    });
                }

                /** Retrieve the position of the last card **/
                if(i == i && j == 2) {
                    card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int[] locations = new int[2];
                            card.getLocationOnScreen(locations);
                            int x = locations[0];
                            int y = locations[1];
                            lastCard = new Card(card.getWidth(), card.getHeight(), (int) x, (int) y);
                        }
                    });
                }
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.addRule(RelativeLayout.BELOW, previousRelative);
            }
            //params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relativeLayout.setGravity(Gravity.CENTER);
            gameContent.addView(relativeLayout);

            previousRelative = i;
        }

        if(mode.equals(GameMode.CAREER)) {

        }
    }

    @Override
    public void allCardsHaveRotate() {
        AnimationUtil.setTextAnimation(txtTutorialHelper, getResources().getString(R.string.label_tutorial_step_2));

        createFingerFirstCard();
   }

    /**
     * Create finger ImageView for the firstCard
     */
   private void createFingerFirstCard() {
       createFingerImageView(firstCard);
   }

    /**
     * Create finger ImageView for the lastCard
     */
   private void createFingerLastCard() {
       createFingerImageView(lastCard);
   }

    /**
     * Create finger ImageView
     * @param card
     */
   private void createFingerImageView(Card card) {
       relativeLayoutHand = new RelativeLayout(getApplicationContext());
       ImageView finger = new ImageView(getApplicationContext());
       finger.setBackground(getResources().getDrawable(R.drawable.finger));

       RelativeLayout.LayoutParams paramsFinger = new RelativeLayout.LayoutParams(MathUtil.dpToPx(70), MathUtil.dpToPx(70));
       relativeLayoutHand.setLayoutParams(paramsFinger);
       relativeLayoutHand.setX(card.getX() + card.getWidth()/2);
       relativeLayoutHand.setY(card.getY() + card.getHeight()/3);
       relativeLayoutHand.setRotation(-45F);

       relativeLayoutHand.addView(finger);
       parentTutorial.addView(relativeLayoutHand);

       AnimationUtil.fingerAnimationTutorial(finger);

       NUM_STEP = STEP_2;
   }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
