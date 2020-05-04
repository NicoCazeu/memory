package com.softcaze.memory.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.CreditsListAdapter;
import com.softcaze.memory.model.Credit;
import com.softcaze.memory.model.User;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditsActivity extends Activity {
    protected TextView txtCoin, txtBonus, labelFooter;
    protected ImageView imgCoin;
    protected CreditsListAdapter creditsListAdapter;
    protected RecyclerView recyclerView;
    private static String attribution = "Icon made by {0} from {1}";

    HashMap<String, Integer[]> credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        labelFooter = (TextView) findViewById(R.id.label_footer);
        imgCoin = (ImageView) findViewById(R.id.img_coin);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        UIUtil.setTypeFaceText(this, txtBonus, txtCoin, labelFooter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        credits = new HashMap<>();
        Integer[] imgIds;

        /**
         * Smashicons
         */
        imgIds = new Integer[]{R.drawable.fruits_watermelon, R.drawable.insects_ant, R.drawable.insects_beetle, R.drawable.insects_butterfly, R.drawable.insects_flea
                , R.drawable.fruits_orange, R.drawable.fruits_grapes, R.drawable.fruits_strawberry
                , R.drawable.insects_fly, R.drawable.insects_grasshopper, R.drawable.insects_mosquito, R.drawable.insects_moth, R.drawable.insects_spider
                , R.drawable.insects_wasp, R.drawable.muscis_cymbals, R.drawable.muscis_trumpet, R.drawable.musics_clarinet, R.drawable.musics_drum
                , R.drawable.musics_guitar, R.drawable.musics_harp, R.drawable.musics_piano, R.drawable.musics_saxophone
                , R.drawable.musics_triangle, R.drawable.musics_violin, R.drawable.sports_archery, R.drawable.sports_basketball, R.drawable.sports_boxing
                , R.drawable.sports_football, R.drawable.sports_hockey, R.drawable.sports_kayak, R.drawable.sports_pingpong, R.drawable.sports_soccer
                , R.drawable.sports_tennis, R.drawable.vehicles_aeroplane, R.drawable.vehicles_ambulance, R.drawable.vehicles_bicycle, R.drawable.vehicles_cabin
                , R.drawable.vehicles_caravan, R.drawable.vehicles_demolishing, R.drawable.vehicles_mixer, R.drawable.vehicles_ship, R.drawable.vehicles_taxi
                , R.drawable.vehicles_truck, R.drawable.domino_1, R.drawable.domino_2, R.drawable.domino_3, R.drawable.domino_4, R.drawable.domino_5, R.drawable.domino_6, R.drawable.domino_7
                , R.drawable.domino_8, R.drawable.domino_9, R.drawable.domino_10};
        credits.put("Smashicons", imgIds);

        /**
         * Those icons
         */
        imgIds = new Integer[]{R.drawable.level_lock};
        credits.put("Those icons", imgIds);

        /**
         * Freepik
         */
        imgIds = new Integer[]{R.drawable.coin, R.drawable.eyes_bonus, R.drawable.fruits_banana, R.drawable.fruits_cherries, R.drawable.fruits_pineapple
                , R.drawable.heart_pink, R.drawable.skull, R.drawable.trophy_end_level, R.drawable.application_ad};
        credits.put("Freepik", imgIds);

        /**
         * Pixel perfect
         */
        imgIds = new Integer[]{R.drawable.finger, R.drawable.img_credit};
        credits.put("Pixel perfect", imgIds);

        /**
         * Roundicons
         */
        imgIds = new Integer[]{R.drawable.flag_australia, R.drawable.flag_brazil, R.drawable.flag_canada, R.drawable.flag_china, R.drawable.flag_france, R.drawable.flag_germany
                , R.drawable.flag_india, R.drawable.flag_italy, R.drawable.flag_portugal, R.drawable.flag_russia, R.drawable.flag_south_korea, R.drawable.flag_spain, R.drawable.flag_thailand
                , R.drawable.flag_united_kingdom, R.drawable.flag_united_states};
        credits.put("Roundicons", imgIds);

        /**
         * Vectors Market
         */
        imgIds = new Integer[]{R.drawable.fruits_apple, R.drawable.medal, R.drawable.trophy};
        credits.put("Vectors Market", imgIds);

        /**
         * Flat icons
         */
        imgIds = new Integer[]{R.drawable.hourglass};
        credits.put("Flat icons", imgIds);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");

        creditsListAdapter = new CreditsListAdapter(getListCreditFromMap(credits));
        recyclerView.setAdapter(creditsListAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreditsActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static String getAttribution(String creator) {
        return attribution.replace("{0}", creator).replace("{1}", "www.flaticon.com");
    }

    private List<Credit> getListCreditFromMap(HashMap<String, Integer[]> map) {
        List<Credit> credits = new ArrayList<>();

        for(Map.Entry<String, Integer[]> entry: map.entrySet()) {
            for(Integer val: entry.getValue()) {
                credits.add(new Credit(val, entry.getKey()));
            }
        }

        return credits;
    }
}
