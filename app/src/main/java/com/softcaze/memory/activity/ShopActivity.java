package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.softcaze.memory.R;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.inappbilling.BillingManager;
import com.softcaze.memory.listener.BoughtItemListener;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.UIUtil;
import com.softcaze.memory.view.EndLevelView;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopActivity extends Activity implements RewardedVideoAdListener {
    protected TextView txtCoin, txtBonus, getBonus, getCoins, award1, award2, award3, award4, award5, pricePack1, pricePack2, price1, price2, labelFooter;
    protected ImageView imgCoin;
    protected RelativeLayout item1, item2, item3, item4, item5;
    protected BoughtItemListener boughtItemListener;
    protected int counter = 0;
    protected int debitCoinCounter = 0;
    protected int creditBonusCounter = 0;
    protected int coinAdCounter = 0;
    protected Timer timer, timerBonusManager, timerCoinManager, timerCoinAdVideo, timerLoadingAd;
    protected Dao dao;
    protected int amountCoin;
    protected RewardedVideoAd rewardedVideoAd;
    protected boolean canHaveAdVideoReward = false;
    protected boolean canPruchase = true;
    protected ProgressBar loadingAdProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        pricePack1 = (TextView) findViewById(R.id.price4);
        pricePack2 = (TextView) findViewById(R.id.price5);
        price1 = (TextView) findViewById(R.id.price1);
        price2 = (TextView) findViewById(R.id.price2);
        getBonus = (TextView) findViewById(R.id.get_bonus);
        getCoins = (TextView) findViewById(R.id.get_coins);
        award1 = (TextView) findViewById(R.id.award1);
        award2 = (TextView) findViewById(R.id.award2);
        award3 = (TextView) findViewById(R.id.award3);
        award4 = (TextView) findViewById(R.id.award4);
        award5 = (TextView) findViewById(R.id.award5);
        labelFooter = (TextView) findViewById(R.id.label_footer);

        UIUtil.setTypeFaceText(this, txtBonus, txtCoin, getBonus, getCoins, award1, award2, award3, award4, award5, price1, price2, labelFooter);

        imgCoin = (ImageView) findViewById(R.id.img_coin);

        item1 = (RelativeLayout) findViewById(R.id.item1);
        item2 = (RelativeLayout) findViewById(R.id.item2);
        item3 = (RelativeLayout) findViewById(R.id.item3);
        item4 = (RelativeLayout) findViewById(R.id.item4);
        item5 = (RelativeLayout) findViewById(R.id.item5);

        loadingAdProgressBar = (ProgressBar) findViewById(R.id.loading_ad_progressbar);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        dao = new Dao(this);

        BillingManager.getInstance().setActivity(this);
        BillingManager.getInstance().initialize(pricePack1, pricePack2, new BoughtItemListener() {
            @Override
            public void boughtItem(String id) {
                amountCoin = 0;
                if(id.equals(ApplicationConstants.IN_APP_BILLING_ITEM_1)) {
                    amountCoin = ApplicationConstants.IN_APP_BILLING_ITEM_1_AMOUNT;
                } else if (id.equals(ApplicationConstants.IN_APP_BILLING_ITEM_2)) {
                    amountCoin = ApplicationConstants.IN_APP_BILLING_ITEM_2_AMOUNT;
                }

                int coinTemp = 0;
                try {
                    dao.open();

                    coinTemp = dao.getCoinUser();
                } finally {
                    dao.close();
                }

                final int coin = coinTemp;

                Runnable runnableCoin = new Runnable () {
                    @Override
                    public void run() {
                        ShopActivity self = ShopActivity.this;
                        self.counter++;
                        int newValue = coin + self.counter;
                        txtCoin.setText(newValue + "");
                        if (self.counter == amountCoin) {
                            self.counter = 0;
                            self.timer.stopTimer();
                            canPruchase = true;
                        }
                    }
                };

                timer = new Timer(runnableCoin, 2, true);

                if(User.getInstance().getCoin() != null) {
                    User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + amountCoin);

                    try {
                        dao.open();

                        dao.setCoinUser(User.getInstance().getCoin().getAmount());
                    } finally {
                        dao.close();
                    }
                }
            }

            @Override
            public void itemCanceled() {
                canPruchase = true;
            }
        });

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        if(User.getInstance().getCoin() != null) {
            txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        }
        if(User.getInstance().getBonus() != null) {
            txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        }

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(User.getInstance().getCoin() != null && User.getInstance().getCoin().getAmount() >= 80) {
                    purchaseBonus(80, User.getInstance().getCoin().getAmount(), 1, User.getInstance().getBonus().getAmount());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.shop_dont_have_money), Toast.LENGTH_SHORT).show();
                }
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(User.getInstance().getCoin().getAmount() >= 750) {
                    purchaseBonus(750, User.getInstance().getCoin().getAmount(), 10, User.getInstance().getBonus().getAmount());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.shop_dont_have_money), Toast.LENGTH_SHORT).show();
                }
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(canPruchase) {
                    if (rewardedVideoAd.isLoaded()) {
                        canPruchase = false;
                        rewardedVideoAd.show();
                    }
                }
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(canPruchase) {
                    canPruchase = false;
                    BillingManager.getInstance().tryToPurchase(ApplicationConstants.IN_APP_BILLING_ITEM_1);
                }}
        });

        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(canPruchase) {
                    canPruchase = false;
                    BillingManager.getInstance().tryToPurchase(ApplicationConstants.IN_APP_BILLING_ITEM_2);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopActivity.this, MainMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BillingManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    private void loadRewardedVideoAd(){
        rewardedVideoAd.loadAd(ApplicationConstants.ID_AD_VIDEO_REWARD_SHOP_COINS,
                new AdRequest.Builder().build());

        loadingAdProgress();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        canHaveAdVideoReward = false;
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(canHaveAdVideoReward) {
            watchingAdVideo(40, User.getInstance().getCoin().getAmount());
        } else {
            loadRewardedVideoAd();
        }
        canPruchase = true;
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        canHaveAdVideoReward = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void watchingAdVideo(final Integer gain, final Integer totalCoin) {
        Runnable manageCoinAd = new Runnable() {
            @Override
            public void run() {
                ShopActivity self = ShopActivity.this;
                self.coinAdCounter++;
                int newValue = totalCoin + self.coinAdCounter;
                txtCoin.setText(newValue + "");
                if (self.coinAdCounter == gain) {
                    self.coinAdCounter = 0;
                    self.timerCoinAdVideo.stopTimer();
                    loadRewardedVideoAd();
                    canPruchase = true;
                }
            }
        };

        timerCoinAdVideo = new Timer(manageCoinAd, 2, true);

        User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + gain);

        try {
            dao.open();
            dao.setCoinUser(User.getInstance().getCoin().getAmount());
        } finally {
            dao.close();
        }
    }

    /**
     * Method invoked to purchase bonus with coin
     * @param price
     * @param totalCoin
     */
    private void purchaseBonus(final Integer price, final Integer totalCoin, final Integer gainBonus, final Integer totalBonus) {
        if(canPruchase) {
            canPruchase = false;
            Runnable manageBonus = new Runnable() {
                @Override
                public void run() {
                    ShopActivity self = ShopActivity.this;
                    self.creditBonusCounter++;
                    int newValue = totalBonus + self.creditBonusCounter;
                    txtBonus.setText(newValue + "");
                    if (self.creditBonusCounter == gainBonus) {
                        self.creditBonusCounter = 0;
                        self.timerBonusManager.stopTimer();
                    }
                }
            };

            debitCoinCounter = price;

            Runnable manageCoin = new Runnable() {
                @Override
                public void run() {
                    ShopActivity self = ShopActivity.this;
                    self.debitCoinCounter--;
                    int newValue = totalCoin - (price - self.debitCoinCounter);
                    txtCoin.setText(newValue + "");
                    if (self.debitCoinCounter == 0) {
                        self.timerCoinManager.stopTimer();
                        canPruchase = true;
                    }
                }
            };

            timerBonusManager = new Timer(manageBonus, 2, true);
            timerCoinManager = new Timer(manageCoin, 2, true);

            User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() - price);
            User.getInstance().getBonus().setAmount(User.getInstance().getBonus().getAmount() + gainBonus);

            try {
                dao.open();

                dao.setCoinUser(User.getInstance().getCoin().getAmount());
                dao.setBonusRevealUser(User.getInstance().getBonus().getAmount());
            } finally {
                dao.close();
            }
        }
    }

    private void loadingAdProgress() {
        Runnable loadingAd = new Runnable() {
            @Override
            public void run() {
                ShopActivity self = ShopActivity.this;
                try {
                    if(rewardedVideoAd.isLoaded()) {
                        loadingAdProgressBar.setVisibility(View.INVISIBLE);
                        self.timerLoadingAd.stopTimer();
                    } else {
                        loadingAdProgressBar.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    ;
                }
            }
        };

        timerLoadingAd = new Timer(loadingAd, 500, true);
    }
}
