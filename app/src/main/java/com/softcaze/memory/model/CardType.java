package com.softcaze.memory.model;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 09/07/2019.
 */

public enum CardType {
    // FRUIT
    GRAPES(R.drawable.fruits_grapes, 1, CardTheme.FRUIT),
    APPLE(R.drawable.fruits_apple, 2, CardTheme.FRUIT),
    BANANA(R.drawable.fruits_banana, 3, CardTheme.FRUIT),
    CHERRIE(R.drawable.fruits_cherries, 4, CardTheme.FRUIT),
    ORANGE(R.drawable.fruits_orange, 5, CardTheme.FRUIT),
    PINEAPPLE(R.drawable.fruits_pineapple, 6, CardTheme.FRUIT),
    STRAWBERRY(R.drawable.fruits_strawberry, 7, CardTheme.FRUIT),
    WATERMELON(R.drawable.fruits_watermelon, 8, CardTheme.FRUIT),

    // FLAG COUNTRY
    AUSTRALIA(R.drawable.flag_australia, 101, CardTheme.FLAG),
    BRAZIL(R.drawable.flag_brazil, 102, CardTheme.FLAG),
    CANADA(R.drawable.flag_canada, 103, CardTheme.FLAG),
    CHINA(R.drawable.flag_china, 104, CardTheme.FLAG),
    FRANCE(R.drawable.flag_france, 105, CardTheme.FLAG),
    GERMANY(R.drawable.flag_germany, 106, CardTheme.FLAG),
    INDIA(R.drawable.flag_india, 107, CardTheme.FLAG),
    ITALY(R.drawable.flag_italy, 108, CardTheme.FLAG),
    JAPAN(R.drawable.flag_japan, 109, CardTheme.FLAG),
    PORTUGAL(R.drawable.flag_portugal, 110, CardTheme.FLAG),
    RUSSIA(R.drawable.flag_russia, 111, CardTheme.FLAG),
    SOUTH_KOREA(R.drawable.flag_south_korea, 112, CardTheme.FLAG),
    SPAIN(R.drawable.flag_spain, 113, CardTheme.FLAG),
    THAILAND(R.drawable.flag_thailand, 114, CardTheme.FLAG),
    UNITED_KINGDOM(R.drawable.flag_united_kingdom, 115, CardTheme.FLAG),
    UNITED_STATES(R.drawable.flag_united_states, 116, CardTheme.FLAG),

    // Sport
    FOOTBALL(R.drawable.sports_football, 120, CardTheme.SPORT),
    SOCCER(R.drawable.sports_soccer, 121, CardTheme.SPORT),
    BASKETBALL(R.drawable.sports_basketball, 122, CardTheme.SPORT),
    PING_PONG(R.drawable.sports_pingpong, 123, CardTheme.SPORT),
    TENNIS(R.drawable.sports_tennis, 124, CardTheme.SPORT),
    ARCHERY(R.drawable.sports_archery, 125, CardTheme.SPORT),
    HOCKEY(R.drawable.sports_hockey, 126, CardTheme.SPORT),
    KAYAK(R.drawable.sports_kayak, 127, CardTheme.SPORT),

    // Music
    GUITAR(R.drawable.musics_guitar, 140, CardTheme.MUSIC),
    SAXOPHONE(R.drawable.musics_saxophone, 141, CardTheme.MUSIC),
    VIOLIN(R.drawable.musics_violin, 142, CardTheme.MUSIC),
    CLARINET(R.drawable.musics_clarinet, 143, CardTheme.MUSIC),
    TRUMPET(R.drawable.muscis_trumpet, 144, CardTheme.MUSIC),
    DRUM(R.drawable.musics_drum, 145, CardTheme.MUSIC),
    CYMBAL(R.drawable.muscis_cymbals, 146, CardTheme.MUSIC),
    HARP(R.drawable.musics_harp, 147, CardTheme.MUSIC),
    TRIANGLE(R.drawable.musics_triangle, 148, CardTheme.MUSIC),
    PIANO(R.drawable.musics_piano, 149, CardTheme.MUSIC),

    // Insects
    ANT(R.drawable.insects_ant, 160, CardTheme.INSECT),
    BEETLE(R.drawable.insects_beetle, 161, CardTheme.INSECT),
    BUTTERFLY(R.drawable.insects_butterfly, 162, CardTheme.INSECT),
    FLEA(R.drawable.insects_flea, 163, CardTheme.INSECT),
    FLY(R.drawable.insects_fly, 164, CardTheme.INSECT),
    GRASSHOPPER(R.drawable.insects_grasshopper, 165, CardTheme.INSECT),
    MOSQUITO(R.drawable.insects_mosquito, 166, CardTheme.INSECT),
    MOTH(R.drawable.insects_moth, 167, CardTheme.INSECT),
    SPIDER(R.drawable.insects_spider, 168, CardTheme.INSECT),
    WASP(R.drawable.insects_wasp, 169, CardTheme.INSECT),

    // Domino
    DOMINO_1(R.drawable.domino_1, 180, CardTheme.DOMINO),
    DOMINO_2(R.drawable.domino_2, 181, CardTheme.DOMINO),
    DOMINO_3(R.drawable.domino_3, 182, CardTheme.DOMINO),
    DOMINO_4(R.drawable.domino_4, 183, CardTheme.DOMINO),
    DOMINO_5(R.drawable.domino_5, 184, CardTheme.DOMINO),
    DOMINO_6(R.drawable.domino_6, 185, CardTheme.DOMINO),
    DOMINO_7(R.drawable.domino_7, 186, CardTheme.DOMINO),
    DOMINO_8(R.drawable.domino_8, 187, CardTheme.DOMINO),
    DOMINO_9(R.drawable.domino_9, 188, CardTheme.DOMINO),
    DOMINO_10(R.drawable.domino_10, 189, CardTheme.DOMINO),

    // Vehicule
    AEROPLANE(R.drawable.vehicles_aeroplane, 200, CardTheme.VEHICLE),
    AMBULANCE(R.drawable.vehicles_ambulance, 201, CardTheme.VEHICLE),
    BICYCLE(R.drawable.vehicles_bicycle, 202, CardTheme.VEHICLE),
    CABIN(R.drawable.vehicles_cabin, 203, CardTheme.VEHICLE),
    CARAVAN(R.drawable.vehicles_caravan, 204, CardTheme.VEHICLE),
    DEMOLISHING(R.drawable.vehicles_demolishing, 205, CardTheme.VEHICLE),
    MIXER(R.drawable.vehicles_mixer, 206, CardTheme.VEHICLE),
    SHIP(R.drawable.vehicles_ship, 207, CardTheme.VEHICLE),
    TAXI(R.drawable.vehicles_taxi, 208, CardTheme.VEHICLE),
    TRUCK(R.drawable.vehicles_truck, 209, CardTheme.VEHICLE);

    // Card ?


    private int drawable;
    private int value;
    private CardTheme theme;

    private CardType(int drawable, int value, CardTheme theme) {
        this.drawable = drawable;
        this.value = value;
        this.theme = theme;
    }

    public int getDrawable() {
        return this.drawable;
    }

    public int getValue() {
        return this.value;
    }

    public CardTheme getTheme() {
        return this.theme;
    }
}
