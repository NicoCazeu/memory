package com.softcaze.memory.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.listener.RatePopupListener;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

public class RateView extends RelativeLayout {
    protected LinearLayout linearStars;
    protected RelativeLayout btnContainer;
    protected ImageView star1, star2, star3, star4, star5;
    protected TextView titleRate, btnNever, btnLater, btnClose;
    protected RatePopupListener ratePopupListener;
    protected Timer timerUpdating;

    public RateView(int id, Context context, RatePopupListener listener) {
        super(context);
        this.setId(id);
        this.ratePopupListener = listener;
        init(context);
    }

    private void init(final Context context) {
        inflate(getContext(), R.layout.rate_popup, this);

        linearStars = (LinearLayout) findViewById(R.id.linear_stars);

        btnContainer = (RelativeLayout) findViewById(R.id.btn_container);

        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        titleRate = (TextView) findViewById(R.id.title_rate);
        btnNever = (TextView) findViewById(R.id.btn_never);
        btnLater = (TextView) findViewById(R.id.btn_later);
        btnClose = (TextView) findViewById(R.id.btn_close);

        UIUtil.setTypeFaceText(context, titleRate, btnNever, btnLater, btnClose);


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star_score);
                star2.setImageResource(R.drawable.star_empty_rate);
                star3.setImageResource(R.drawable.star_empty_rate);
                star4.setImageResource(R.drawable.star_empty_rate);
                star5.setImageResource(R.drawable.star_empty_rate);

                closeBadRate();
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star_score);
                star2.setImageResource(R.drawable.star_score);
                star3.setImageResource(R.drawable.star_empty_rate);
                star4.setImageResource(R.drawable.star_empty_rate);
                star5.setImageResource(R.drawable.star_empty_rate);

                closeBadRate();
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star_score);
                star2.setImageResource(R.drawable.star_score);
                star3.setImageResource(R.drawable.star_score);
                star4.setImageResource(R.drawable.star_empty_rate);
                star5.setImageResource(R.drawable.star_empty_rate);

                closeBadRate();
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star_score);
                star2.setImageResource(R.drawable.star_score);
                star3.setImageResource(R.drawable.star_score);
                star4.setImageResource(R.drawable.star_score);
                star5.setImageResource(R.drawable.star_empty_rate);

                openMarket(context);
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star_score);
                star2.setImageResource(R.drawable.star_score);
                star3.setImageResource(R.drawable.star_score);
                star4.setImageResource(R.drawable.star_score);
                star5.setImageResource(R.drawable.star_score);

                openMarket(context);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, context);
                ratePopupListener.closePopup();
            }
        });
        btnNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, context);
                ratePopupListener.closePopup();
            }
        });

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, context);
                ratePopupListener.clickLater();
            }
        });
    }

    private void openMarket(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

        timerUpdating = new Timer(new Runnable() {
            @Override
            public void run() {
                ratePopupListener.closePopup();
            }
        }, 500, true);
        timerUpdating.startTimer();
    }

    private void closeBadRate() {
        timerUpdating = new Timer(new Runnable() {
            @Override
            public void run() {
                RateView self = RateView.this;
                titleRate.setText(getResources().getString(R.string.rate_popup_subtitle));
                btnContainer.setVisibility(View.GONE);
                linearStars.setVisibility(View.GONE);
                btnClose.setVisibility(View.VISIBLE);
                self.timerUpdating.stopTimer();
            }
        }, 500, true);
        timerUpdating.startTimer();
    }
}

