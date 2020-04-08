package com.softcaze.memory.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.listener.TutorialAnimationListener;
import com.softcaze.memory.model.CardState;
import com.softcaze.memory.model.CardType;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.ApplicationConstants;

public class CardView extends RelativeLayout {
    private static boolean isSecondCard = false;
    private RelativeLayout background;
    private ImageView imgCard;
    private Drawable saveImgCard;
    private CardState cardState = CardState.RECTO;
    private CardType type;
    private RelativeLayout glowBorder, glowBorder2;
    private AnimatorSet mAnimationSet, mAnimationSet2;

    public CardView(Context context) {
        super(context);
        init(null, 0);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.card, this);

        background = (RelativeLayout) findViewById(R.id.card_background);
        imgCard = (ImageView) findViewById(R.id.card_content);
        glowBorder = (RelativeLayout) findViewById(R.id.glow_border);
        glowBorder2 = (RelativeLayout) findViewById(R.id.glow_border2);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(glowBorder, "alpha", .1f, .5f);
        fadeIn.setDuration(60);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(glowBorder2, "alpha", .1f, .4f);
        fadeIn2.setDuration(80);

        mAnimationSet = new AnimatorSet();
        mAnimationSet2 = new AnimatorSet();

        mAnimationSet.play(fadeIn);
        mAnimationSet2.play(fadeIn2);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet2.start();
            }
        });

        mAnimationSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (CardView c : GameInformation.getInstance().getCardsFlip()) {
                    c.setVisibility(View.INVISIBLE);
                }
                GameInformation.getInstance().getCardsFlip().clear();

                if(GameInformation.getInstance().hasFindAllCards() && isSecondCard) {
                    if(GameInformation.getInstance().hasNextLevel()) {
                        Intent endLevel = new Intent(ApplicationConstants.INTENT_END_LEVEL);
                        getContext().sendBroadcast(endLevel);
                    } else {
                        Intent endLevel = new Intent(ApplicationConstants.INTENT_END_ALL_LEVELS);
                        getContext().sendBroadcast(endLevel);
                    }
                }

                isSecondCard = true;
            }
        });

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CardView, defStyle, 0);

        if (a.hasValue(R.styleable.CardView_imgCard)) {
            imgCard.setBackground(a.getDrawable(R.styleable.CardView_imgCard));
        }

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImgCard(Drawable drawble) {
        imgCard.setBackground(drawble);
    }

    public ImageView getImgCard() {
        return imgCard;
    }

    public CardState getCardState() {
        return cardState;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }

    public Drawable getSaveImgCard() {
        return saveImgCard;
    }

    public void setSaveImgCard(Drawable saveImgCard) {
        this.saveImgCard = saveImgCard;
    }

    public void setType(CardType type) {
        this.type = type;

        this.setImgCard(getResources().getDrawable(type.getDrawable()));
        this.setSaveImgCard(getResources().getDrawable(type.getDrawable()));
    }

    public CardType getType() {
        return type;
    }

    public void flipCard(final CardState state) {
        flipCard(state, null);
    }
    public void flipCard(final CardState state, final RelativeLayout gameHelper) {
        if (GameInformation.getInstance().isCanPlay()) {
            this.animate().withLayer().rotationY(90).setDuration(300).withEndAction(new Runnable() {
                @Override
                public void run() {
                    if (state == CardState.RECTO) {
                        setRectoCard();
                        setCardState(CardState.RECTO);
                    } else {
                        setVersoCard();
                        setCardState(CardState.VERSO);
                    }

                    setRotationY(-90);
                    animate().withLayer().rotationY(0).setDuration(300).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(75);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (state == CardState.RECTO) {
                                if (!GameInformation.getInstance().containCard(getThis())) {
                                    GameInformation.getInstance().addCardFlip(getThis());
                                }

                                if (GameInformation.getInstance().getCardsFlip().size() >= 2) {
                                    if (GameInformation.getInstance().getCardsFlip().get(0).getType().getValue() == GameInformation.getInstance().getCardsFlip().get(1).getType().getValue()) {

                                        for (CardView c : GameInformation.getInstance().getCardsFlip()) {
                                            c.playAnimationGrewBorder();
                                        }
                                    } else {
                                        if(gameHelper != null) {
                                            int numberLife = gameHelper.getChildCount();
                                            ImageView imgGameHelper = (ImageView) gameHelper.getChildAt(numberLife-1);
                                            gameHelper.removeView(imgGameHelper);

                                            if(gameHelper.getChildCount() == 0) {
                                                Intent gameOver = new Intent(ApplicationConstants.INTENT_GAME_OVER_LEVEL);
                                                getContext().sendBroadcast(gameOver);
                                            }
                                        }
                                        for (CardView c : GameInformation.getInstance().getCardsFlip()) {
                                            c.flipCardOnVerso(0);
                                        }

                                        GameInformation.getInstance().getCardsFlip().clear();
                                    }
                                }
                                else {
                                    isSecondCard = false;
                                }
                            }
                        }
                    }).start();
                }
            }).start();
        }
    }

    /**
     * This method is invoked when user used an eyes bonus
     * @param delay
     */
    public void eyesBonusAction(final int delay, final TextView txtView) {
        this.animate().withLayer().rotationY(90).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                setRectoCard();
                setCardState(CardState.RECTO);

                setRotationY(-90);

                animate().withLayer().rotationY(0).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        flipCardOnVerso(delay);
                    }
                });
            }
        });
    }

    public void flipCardOnVerso(int delay) {
        flipCardOnVerso(delay, null);
    }

    public void flipCardOnVerso(int delay, final TutorialAnimationListener listener) {

        this.animate().withLayer().rotationY(90).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                setVersoCard();
                setCardState(CardState.VERSO);

                setRotationY(-90);
                if(listener != null) {
                    animate().withLayer().rotationY(0).setDuration(300).setStartDelay(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            listener.allCardsHaveRotate();
                        }
                    });
                } else {
                    animate().withLayer().rotationY(0).setDuration(300).setStartDelay(0).start();
                }

                GameInformation.getInstance().setCanPlay(true);
            }
        }).setStartDelay(delay).start();
    }

    public CardView getThis() {
        return this;
    }

    public void setVersoCard() {
        background.setBackground(getContext().getResources().getDrawable(ApplicationConstants.VERSO_BACKGROUND_CARD));
        setImgCard(null);
    }

    public void setRectoCard() {
        background.setBackground(getContext().getResources().getDrawable(ApplicationConstants.RECTO_BACKGROUND_CARD));
        setImgCard(getResources().getDrawable(getType().getDrawable()));
    }

    public void playAnimationGrewBorder() {
        mAnimationSet.start();
    }
}
