package com.softcaze.memory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicolas on 28/01/2020.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * User table
     */
    public static final String TABLE_NAME_USER = "user";
    public static final String COL_ID_USER = "id_user";
    public static final String COL_COIN_USER = "coin_user";
    public static final String COL_BONUS_REVEAL_USER = "bonus_reveal_user";
    public static final String COL_NEED_CAREER_TUTO = "need_career_tuto";
    public static final String COL_NEED_AGAINSTTIME_TUTO = "need_againsttime_tuto";
    public static final String COL_NEED_SURVIVAL_TUTO = "need_survival_tuto";
    public static final String COL_NEED_SUDDENDEATH_TUTO = "need_suddendeath_tuto";

    /**
     * Params table
     */
    public static final String TABLE_NAME_PARAMS = "params";
    public static final String COL_ID_PARAMS = "id_params";
    public static final String COL_ENABLE_SOUND = "enable_sound";
    public static final String COL_ENABLE_NOTIFICATION = "enable_notification";
    public static final String COL_LANGUAGES = "languages";

    /**
     * Challenge table
     */
    public static final String TABLE_NAME_CHALLENGE = "challenge";
    public static final String COL_ID_CHALLENGE = "id_challenge";
    public static final String COL_NAME_CHALLENGE = "name_challenge";
    public static final String COL_TYPE_AWARD = "type_award";
    public static final String COL_AMOUNT_AWARD = "amount_award";
    public static final String COL_IS_UNLOCK_CHALLENGE = "is_unlock_challenge";
    public static final String COL_GET_AWARD_CHALLENGE = "get_award_challenge";

    /**
     * Career Level table
     */
    public static final String TABLE_NAME_CAREER_LEVEL = "career_level";
    public static final String COL_ID_CAREER_LEVEL = "id_career_level";
    public static final String COL_NUM_LEVEL_CAREER_LEVEL = "num_level_career_level";
    public static final String COL_IS_UNLOCK_CAREER_LEVEL = "is_unlock_career_level";
    public static final String COL_STAR_NUMBER_CAREER_LEVEL = "star_number_career_level";
    public static final String COL_BEST_SCORE_CAREER_LEVEL = "best_score_career_level";

    /**
     * Others Level table
     */
    public static final String TABLE_NAME_OTHERS_LEVEL = "others_level";
    public static final String COL_ID_OTHERS_LEVEL = "id_others_level";
    public static final String COL_NUM_LEVEL_OTHERS_LEVEL = "num_level_others_level";
    public static final String COL_MODE_OTHERS_LEVEL = "mode_others_level";

    /**
     * Count Ad
     */
    public static final String TABLE_NAME_COUNT_AD = "table_count_ad";
    public static final String COL_COUNT_AD = "count_ad";

    /**
     * Create TABLE user
     */
    public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + " (" +
            COL_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_COIN_USER + " INTEGER, " +
            COL_BONUS_REVEAL_USER + " INTEGER, " +
            COL_NEED_CAREER_TUTO + " INTEGER, " +
            COL_NEED_AGAINSTTIME_TUTO + " INTEGER, " +
            COL_NEED_SURVIVAL_TUTO + " INTEGER, " +
            COL_NEED_SUDDENDEATH_TUTO + " INTEGER);";

    /**
     * Create TABLE params
     */
    public static final String CREATE_TABLE_PARAMS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PARAMS + " (" +
            COL_ID_PARAMS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ENABLE_SOUND + " INTEGER, " +
            COL_ENABLE_NOTIFICATION + " INTEGER, " +
            COL_LANGUAGES + " INTEGER);";

    /**
     * Create TABLE challenge
     */
    public static final String CREATE_TABLE_CHALLENGE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CHALLENGE + " (" +
            COL_ID_CHALLENGE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME_CHALLENGE + " TEXT, " +
            COL_TYPE_AWARD + " INTEGER, " +
            COL_AMOUNT_AWARD + " INTEGER, " +
            COL_IS_UNLOCK_CHALLENGE + " INTEGER, " +
            COL_GET_AWARD_CHALLENGE + " INTEGER);";

    /**
     * Create TABLE career level
     */
    public static final String CREATE_TABLE_CAREER_LEVEL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CAREER_LEVEL + " (" +
            COL_ID_CAREER_LEVEL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NUM_LEVEL_CAREER_LEVEL + " INTEGER, " +
            COL_IS_UNLOCK_CAREER_LEVEL + " INTEGER, " +
            COL_STAR_NUMBER_CAREER_LEVEL + " INTEGER, " +
            COL_BEST_SCORE_CAREER_LEVEL + " INTEGER);";

    /**
     * Create TABLE others level
     */
    public static final String CREATE_TABLE_OTHERS_LEVEL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_OTHERS_LEVEL + " (" +
            COL_ID_OTHERS_LEVEL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NUM_LEVEL_OTHERS_LEVEL + " INTEGER, " +
            COL_MODE_OTHERS_LEVEL + " INTEGER);";

    /**
     * CreateTAble Count AD
     */
    public static final String CREATE_TABLE_COUNT_AD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_COUNT_AD + " (" +
            COL_COUNT_AD + " INTEGER PRIMARY KEY);";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_PARAMS);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHALLENGE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CAREER_LEVEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_OTHERS_LEVEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_COUNT_AD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /**
         * Exemple:
         *  if(oldVersion < 2) {
         *      db.execSQL(REQUEST_ALTER_TABLE);
         *  }
         */
    }
}
