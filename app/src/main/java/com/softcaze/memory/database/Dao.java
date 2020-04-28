package com.softcaze.memory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;

import java.util.List;

/**
 * Created by Nicolas on 28/01/2020.
 */

public class Dao {
    public static int VERSION = 1;
    public static String NAME_DB = "memory.db";

    /**
     * User table
     */
    public static final String TABLE_NAME_USER = "user";
    public static final String COL_ID_USER = "id_user";
    public static final int NUM_COL_ID_USER = 0;
    public static final String COL_COIN_USER = "coin_user";
    public static final int NUM_COL_COIN_USER = 1;
    public static final String COL_BONUS_REVEAL_USER = "bonus_reveal_user";
    public static final int NUM_COL_BONUS_REVEAL_USER = 2;
    public static final String COL_NEED_CAREER_TUTO = "need_career_tuto";
    public static final int NUM_COL_NEED_CAREER_TUTO = 3;
    public static final String COL_NEED_AGAINSTTIME_TUTO = "need_againsttime_tuto";
    public static final int NUM_COL_NEED_AGAINSTTIME_TUTO = 4;
    public static final String COL_NEED_SURVIVAL_TUTO = "need_survival_tuto";
    public static final int NUM_COL_NEED_SURVIVAL_TUTO = 5;
    public static final String COL_NEED_SUDDENDEATH_TUTO = "need_suddendeath_tuto";
    public static final int NUM_COL_NEED_SUDDENDEATH_TUTO = 6;

    /**
     * Params table
     */
    public static final String TABLE_NAME_PARAMS = "params";
    public static final String COL_ID_PARAMS = "id_params";
    public static final int NUM_COL_ID_PARAMS = 0;
    public static final String COL_ENABLE_SOUND = "enable_sound";
    public static final int NUM_COL_ENABLE_SOUND = 1;
    public static final String COL_ENABLE_NOTIFICATION = "enable_notification";
    public static final int NUM_COL_ENABLE_NOTIFICATION = 2;
    public static final String COL_LANGUAGES = "languages";
    public static final int NUM_COL_LANGUAGES = 3;

    /**
     * Challenge table
     */
    public static final String TABLE_NAME_CHALLENGE = "challenge";
    public static final String COL_ID_CHALLENGE = "id_challenge";
    public static final int NUM_COL_ID_CHALLENGE = 0;
    public static final String COL_NAME_CHALLENGE = "name_challenge";
    public static final int NUM_COL_NAME_CHALLENGE = 1;
    public static final String COL_TYPE_AWARD = "type_award";
    public static final int NUM_COL_TYPE_AWARD = 2;
    public static final String COL_AMOUNT_AWARD = "amount_award";
    public static final int NUM_COL_AMOUNT_AWARD = 3;
    public static final String COL_IS_UNLOCK_CHALLENGE = "is_unlock_challenge";
    public static final int NUM_COL_IS_UNLOCK_CHALLENGE = 4;
    public static final String COL_GET_AWARD_CHALLENGE = "get_award_challenge";
    public static final int NUM_COL_GET_AWARD_CHALLENGE = 5;

    /**
     * Career Level table
     */
    public static final String TABLE_NAME_CAREER_LEVEL = "career_level";
    public static final String COL_ID_CAREER_LEVEL = "id_career_level";
    public static final int NUM_COL_ID_CAREER_LEVEL = 0;
    public static final String COL_NUM_LEVEL_CAREER_LEVEL = "num_level_career_level";
    public static final int NUM_COL_NUM_LEVEL_CAREER_LEVEL = 1;
    public static final String COL_IS_UNLOCK_CAREER_LEVEL = "is_unlock_career_level";
    public static final int NUM_COL_IS_UNLOCK_CAREER_LEVEL = 2;
    public static final String COL_STAR_NUMBER_CAREER_LEVEL = "star_number_career_level";
    public static final int NUM_COL_STAR_NUMBER_CAREER_LEVEL = 3;
    public static final String COL_BEST_SCORE_CAREER_LEVEL = "best_score_career_level";
    public static final int NUM_COL_BEST_SCORE_CAREER_LEVEL = 4;

    /**
     * Others Level table
     */
    public static final String TABLE_NAME_OTHERS_LEVEL = "others_level";
    public static final String COL_ID_OTHERS_LEVEL = "id_others_level";
    public static final int NUM_COL_ID_OTHERS_LEVEL = 0;
    public static final String COL_NUM_LEVEL_OTHERS_LEVEL = "num_level_others_level";
    public static final int NUM_COL_NUM_LEVEL_OTHERS_LEVEL = 1;
    public static final String COL_MODE_OTHERS_LEVEL = "mode_others_level";
    public static final int NUM_COL_MODE_OTHERS_LEVEL = 2;

    protected SQLiteDatabase database;
    protected DatabaseHandler handler;

    public Dao(Context context) {
        handler = new DatabaseHandler(context, NAME_DB, null, VERSION);
    }

    public void open() {
        database = handler.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public Cursor getDataByTable(String table) {
        return database.rawQuery("SELECT * FROM " + table, null);
    }

    public Cursor getDataByTable(String table, String whereClause) {
        return database.rawQuery("SELECT * FROM " + table + " WHERE " + whereClause, null);
    }

    public void insertUpdate(Cursor c, ContentValues values, String table, String whereClause) {
        if(c.getCount() == 0) {
            try {
                database.insert(table, null, values);
            } catch (Exception e) {
                Log.e("Dao.insertUpdate()", "Error insert data into " + table + " table : " + e);
            }
        } else {
            try {
                database.update(table, values, whereClause, null);
            } catch (Exception e) {
                Log.e("Dao.insertUpdate()", "Error update data into " + table + " table : " + e);
            }
        }
    }

    /**
     * Get coin amount
     * @return
     */
    public int getCoinUser() {
        Cursor c = getDataByTable(TABLE_NAME_USER);

        if(c.getCount() != 0) {
            c.moveToFirst();
            return c.getInt(NUM_COL_COIN_USER);
        }
        c.close();
        return -1;
    }

    public void initUserDatabase() {
        Cursor c = getDataByTable(TABLE_NAME_USER);

        if(c.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COL_COIN_USER, 0);
            values.put(COL_BONUS_REVEAL_USER, 2);
            values.put(COL_NEED_CAREER_TUTO, 1);
            values.put(COL_NEED_AGAINSTTIME_TUTO, 1);
            values.put(COL_NEED_SURVIVAL_TUTO, 1);
            values.put(COL_NEED_SUDDENDEATH_TUTO, 1);

            insertUpdate(c, values, TABLE_NAME_USER, null);
        }

        c.close();
    }

    public void initLevelsDatabase() {
        initCareerLevel();
        initOthersLevel();
    }

    public void initChallenges() {
        Cursor c = getDataByTable(TABLE_NAME_CHALLENGE);

        if(c.getCount() == 0) {
            List<Challenge> challenges = GameInformation.getInstance().getChallenges();
            ContentValues values = null;

            for(Challenge challenge: challenges) {
                values = new ContentValues();
                values.put(COL_NAME_CHALLENGE, challenge.getChallengeLabel());
                if(challenge.getAwardChallengeType().equals(AwardChallengeType.COIN)) {
                    values.put(COL_TYPE_AWARD, AwardChallengeType.COIN_DATABASE);
                } else if(challenge.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
                    values.put(COL_TYPE_AWARD, AwardChallengeType.EYE_BONUS_DATABASE);
                }
                values.put(COL_AMOUNT_AWARD, challenge.getCountAward());
                values.put(COL_IS_UNLOCK_CHALLENGE, (challenge.isUnlockChallenge()) ? 1 : 0);
                values.put(COL_GET_AWARD_CHALLENGE, (challenge.isGetAward()) ? 1 : 0);

                insertUpdate(c, values, TABLE_NAME_CHALLENGE, null);
            }
        }

        c.close();
    }

    public void setGetAwardChallenge(int idChallenge, boolean getAward) {
        Cursor c = getDataByTable(TABLE_NAME_CHALLENGE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GET_AWARD_CHALLENGE, (getAward) ? 1 : 0);

        insertUpdate(c, contentValues, TABLE_NAME_CHALLENGE, COL_ID_CHALLENGE + " = " + idChallenge);

        c.close();
    }

    public boolean getGetAwardChallenge(int idChallenge) {
        Cursor c = getDataByTable(TABLE_NAME_CHALLENGE, COL_ID_CHALLENGE + " = " + idChallenge);
        boolean getAward = false;

        if(c.getCount() != 0) {
            c.moveToFirst();
            getAward = (c.getInt(NUM_COL_GET_AWARD_CHALLENGE) == 1) ? true: false;
        }

        c.close();

        return getAward;
    }

    public void setIsUnlockChallenge(int idChallenge, boolean isUnlock) {
        Cursor c = getDataByTable(TABLE_NAME_CHALLENGE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IS_UNLOCK_CHALLENGE, (isUnlock) ? 1 : 0);

        insertUpdate(c, contentValues, TABLE_NAME_CHALLENGE, COL_ID_CHALLENGE + " = " + idChallenge);

        c.close();
    }

    public boolean isUnlockChallenge(int idChallenge) {
        Cursor c = getDataByTable(TABLE_NAME_CHALLENGE, COL_ID_CHALLENGE + " = " + idChallenge);
        boolean isUnlock = false;

        if(c.getCount() != 0) {
            c.moveToFirst();
            isUnlock = (c.getInt(NUM_COL_IS_UNLOCK_CHALLENGE) == 1) ? true: false;
        }

        c.close();

        return isUnlock;
    }

    /**
     * Init all career levels
     */
    private void initCareerLevel() {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL);

        if(c.getCount() == 0) {
            List<Level> levels = GameInformation.getInstance().getListLevelByGameMode(GameMode.CAREER);

            ContentValues values = null;

            for(Level level : levels) {
                CareerLevel careerLevel = (CareerLevel) level;
                values = new ContentValues();
                values.put(COL_NUM_LEVEL_CAREER_LEVEL, careerLevel.getId());
                if(level.getId() == 1) {
                    values.put(COL_IS_UNLOCK_CAREER_LEVEL, 1);
                } else {
                    values.put(COL_IS_UNLOCK_CAREER_LEVEL, 0);
                }
                values.put(COL_STAR_NUMBER_CAREER_LEVEL, careerLevel.getNumberStar());
                values.put(COL_BEST_SCORE_CAREER_LEVEL, careerLevel.getTouchUsed());

                insertUpdate(c, values, TABLE_NAME_CAREER_LEVEL, null);
            }
        }

        c.close();
    }

    public int getOverallNumberStars() {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL);
        int numberStar = 0;

        if(c.getCount() == 0) {
            return numberStar;
        }

        while(c.moveToNext()) {
            numberStar += c.getInt(NUM_COL_STAR_NUMBER_CAREER_LEVEL);
        }

        return numberStar;
    }

    /**
     * Init 'Against time', 'Survival' and 'Sudden death' level
     */
    private void initOthersLevel() {
        Cursor c = null;
        ContentValues values = null;

        // Against time
        c = getDataByTable(TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + GameMode.AGAINST_TIME_DATABASE);

        if(c.getCount() == 0) {
            values = new ContentValues();
            values.put(COL_NUM_LEVEL_OTHERS_LEVEL, 1);
            values.put(COL_MODE_OTHERS_LEVEL, GameMode.AGAINST_TIME_DATABASE);

            insertUpdate(c, values, TABLE_NAME_OTHERS_LEVEL, null);
        }

        // Survival
        c = getDataByTable(TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + GameMode.SURVIVAL_DATABASE);

        if(c.getCount() == 0) {
            values = new ContentValues();
            values.put(COL_NUM_LEVEL_OTHERS_LEVEL, 1);
            values.put(COL_MODE_OTHERS_LEVEL, GameMode.SURVIVAL_DATABASE);

            insertUpdate(c, values, TABLE_NAME_OTHERS_LEVEL, null);
        }

        // Sudden death
        c = getDataByTable(TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + GameMode.SUDDEN_DEATH_DATABASE);

        if(c.getCount() == 0) {
            values = new ContentValues();
            values.put(COL_NUM_LEVEL_OTHERS_LEVEL, 1);
            values.put(COL_MODE_OTHERS_LEVEL, GameMode.SUDDEN_DEATH_DATABASE);

            insertUpdate(c, values, TABLE_NAME_OTHERS_LEVEL, null);
        }

        c.close();
    }

    public void setCoinUser(int coin) {
        Cursor c = getDataByTable(TABLE_NAME_USER);
        ContentValues values = new ContentValues();
        values.put(COL_COIN_USER, coin);

        insertUpdate(c, values, TABLE_NAME_USER, null);
        c.close();
    }

    public void setBonusRevealUser(int bonusReveal) {
        Cursor c = getDataByTable(TABLE_NAME_USER);
        ContentValues values = new ContentValues();
        values.put(COL_BONUS_REVEAL_USER, bonusReveal);

        insertUpdate(c, values, TABLE_NAME_USER, null);
        c.close();
    }

    /**
     * Get bonus reveal
     * @return
     */
    public int getBonusRevealUser() {
        Cursor c = getDataByTable(TABLE_NAME_USER);

        if(c.getCount() != 0) {
            try {
                c.moveToFirst();
                return c.getInt(NUM_COL_BONUS_REVEAL_USER);
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * Check if tuto by mode is required
     * @param mode
     * @return
     */
    public boolean needTutoByMode(GameMode mode) {
        Cursor c = getDataByTable(TABLE_NAME_USER);
        boolean needTuto = false;

        if(c.getCount() != 0) {
            c.moveToFirst();

            switch(mode) {
                case CAREER:
                    needTuto = (c.getInt(NUM_COL_NEED_CAREER_TUTO) == 1);
                    break;
                case AGAINST_TIME:
                    needTuto = (c.getInt(NUM_COL_NEED_AGAINSTTIME_TUTO) == 1);
                    break;
                case SURVIVAL:
                    needTuto = (c.getInt(NUM_COL_NEED_SURVIVAL_TUTO) == 1);
                    break;
                case SUDDEN_DEATH:
                    needTuto = (c.getInt(NUM_COL_NEED_SUDDENDEATH_TUTO) == 1);
                    break;
            }
        }
        c.close();
        return needTuto;
    }

    /**
     * Set state need tuto by mode
     * @param mode
     * @param state
     */
    public void setNeedTutoByMode(GameMode mode, boolean state){
        ContentValues values = new ContentValues();
        Cursor c = getDataByTable(TABLE_NAME_USER);
        int value = 0;

        if(state) {
            value = 1;
        }

        switch(mode) {
            case CAREER:
                values.put(COL_NEED_CAREER_TUTO, value);
                break;
            case AGAINST_TIME:
                values.put(COL_NEED_AGAINSTTIME_TUTO, value);
                break;
            case SURVIVAL:
                values.put(COL_NEED_SURVIVAL_TUTO, value);
                break;
            case SUDDEN_DEATH:
                values.put(COL_NEED_SUDDENDEATH_TUTO, value);
                break;
        }

        insertUpdate(c, values, TABLE_NAME_USER, null);
        c.close();
    }

    public void saveBestScoreCareerLevel(int numLevel, int bestScore) {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BEST_SCORE_CAREER_LEVEL, bestScore);

        insertUpdate(c, contentValues, TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);

        c.close();
    }

    public int getBestScoreCareerLevel(int numLevel) {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);
        int bestScore = 0;

        if(c.getCount() != 0) {
            c.moveToFirst();
            bestScore = c.getInt(NUM_COL_BEST_SCORE_CAREER_LEVEL);
        }

        c.close();

        return bestScore;
    }

    public void saveStateCareerLevel(int numLevel, LevelState state) {
        int isUnlock = (state.equals(LevelState.UNLOCK)) ? 1 : 0;
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL);
        ContentValues values = new ContentValues();
        values.put(COL_IS_UNLOCK_CAREER_LEVEL, isUnlock);

        insertUpdate(c, values, TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);

        c.close();
    }

    public LevelState getStateCareerLevel(int numLevel) {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);
        LevelState isUnlock = LevelState.LOCK;

        if(c.getCount() != 0) {
            c.moveToFirst();
            isUnlock = (c.getInt(NUM_COL_IS_UNLOCK_CAREER_LEVEL) == 1) ? LevelState.UNLOCK : LevelState.LOCK;
        }

        return isUnlock;
    }

    public int getNumberStarCareerLevel(int numLevel) {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);
        int numberStar = 0;

        if(c.getCount() != 0) {
            c.moveToFirst();
            numberStar = c.getInt(NUM_COL_STAR_NUMBER_CAREER_LEVEL);
        }

        c.close();

        return numberStar;
    }

    public void saveNumberStarCareerLevel(int numLevel, int numberStar) {
        Cursor c = getDataByTable(TABLE_NAME_CAREER_LEVEL);
        ContentValues values = new ContentValues();
        values.put(COL_STAR_NUMBER_CAREER_LEVEL, numberStar);

        insertUpdate(c, values, TABLE_NAME_CAREER_LEVEL, COL_NUM_LEVEL_CAREER_LEVEL + " = " + numLevel);

        c.close();
    }

    public int getNumLevelByMode(int mode) {
        Cursor c = getDataByTable(TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + mode);

        if(c.getCount() != 0) {
            c.moveToFirst();
            try {
                return c.getInt(NUM_COL_NUM_LEVEL_OTHERS_LEVEL);
            } finally {
                c.close();
            }
        }

        return 1;
    }

    public void saveNumLevelByMode(int numLevel, int mode) {
        Cursor c = getDataByTable(TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + mode);

        if(c.getCount() != 0) {
            try {
                ContentValues values = new ContentValues();
                values.put(COL_NUM_LEVEL_OTHERS_LEVEL, numLevel);

                insertUpdate(c, values, TABLE_NAME_OTHERS_LEVEL, COL_MODE_OTHERS_LEVEL + " = " + mode);
            } finally {
                c.close();
            }
        }
    }
}
