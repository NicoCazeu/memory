package com.softcaze.memory.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.softcaze.memory.R;

import java.util.List;

/**
 * Created by Nicolas on 26/01/2020.
 */

public class AnimationUtil {
    public static void btnClickedAnimation(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click));
    }

    public static void playAnimation(Context context, int id, View... views) {
        for(View view: views) {
            view.startAnimation(AnimationUtils.loadAnimation(context, id));
        }
    }

    public static void  fingerAnimationTutorial(View view) {
        final ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "scaleX", .8f);
        final ObjectAnimator animationY = ObjectAnimator.ofFloat(view, "scaleY", .8f);
        animationY.setRepeatCount(ObjectAnimator.INFINITE);
        animationY.setRepeatMode(ValueAnimator.REVERSE);
        animationX.setRepeatCount(ObjectAnimator.INFINITE);
        animationX.setRepeatMode(ValueAnimator.REVERSE);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(animationX).with(animationY);
        scaleDown.setDuration(400);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDown.start();
    }

    public static void rotateCoin(View view) {
        final ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 180f);
        animation.setDuration(800);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    public static void animateStarsEndLevel(final List<ImageView> starList, int nbrStar, final View parent) {
        switch (nbrStar) {
            case 1:
                starList.get(0).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        starList.get(0).animate().scaleX(1F).scaleY(1F).setDuration(400);
                    }
                });
                break;
            case 2:
                starList.get(0).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        starList.get(0).animate().scaleX(1F).scaleY(1F).setDuration(400);
                        starList.get(1).setBackground(parent.getResources().getDrawable(R.drawable.star_score));

                        starList.get(1).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                starList.get(1).animate().scaleX(1F).scaleY(1F).setDuration(400);
                            }
                        });
                    }
                });
                break;
            case 3:
                starList.get(0).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        starList.get(0).animate().scaleX(1F).scaleY(1F).setDuration(400);
                        starList.get(1).setBackground(parent.getResources().getDrawable(R.drawable.star_score));

                        starList.get(1).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                starList.get(1).animate().scaleX(1F).scaleY(1F).setDuration(400);
                                starList.get(2).setBackground(parent.getResources().getDrawable(R.drawable.star_score));

                                starList.get(2).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        starList.get(2).animate().scaleX(1F).scaleY(1F).setDuration(400).withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                starList.get(0).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        starList.get(0).animate().scaleX(1F).scaleY(1F).setDuration(400);
                                                    }
                                                });
                                                starList.get(1).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        starList.get(1).animate().scaleX(1F).scaleY(1F).setDuration(400);
                                                    }
                                                });
                                                starList.get(2).animate().scaleX(1.3F).scaleY(1.3F).setDuration(400).withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        starList.get(2).animate().scaleX(1F).scaleY(1F).setDuration(400);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    public static void setTextAnimation(final TextView view, final String newText) {
        view.animate().alpha(0F).setDuration(600).withEndAction(new Runnable() {
            @Override
            public void run() {
                /** Display hand **/
                view.setText(newText);
                view.animate().alpha(1F).setDuration(600);
            }
        });
    }
}
