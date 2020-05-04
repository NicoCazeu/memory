package com.softcaze.memory.singleton;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.softcaze.memory.model.ChallengeType;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.model.CardTheme;
import com.softcaze.memory.model.CardType;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.util.CollectionUtil;
import com.softcaze.memory.view.CardView;
import com.softcaze.memory.view.UnlockChallengeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class GameInformation {
    private static final GameInformation ourInstance = new GameInformation();

    private GameMode currentMode;
    private HashMap<GameMode, List<Level>> listLevel = new HashMap<>();
    private boolean canPlay = false;
    private List<CardView> cardViews = new ArrayList<>();
    private List<CardView> cardsFlip = new ArrayList<>();
    private int currentLevel;
    private boolean nextLevel = false;
    private List<Challenge> challenges = new ArrayList<>();
    private int overallNumberStars;

    public static GameInformation getInstance() {
        return ourInstance;
    }

    private GameInformation() {
    }

    public GameMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(GameMode currentMode) {
        this.currentMode = currentMode;
    }

    public List<Level> getListLevelByGameMode(GameMode gameMode) {
        return listLevel.get(gameMode);
    }

    public Level getLevelByNumAndGameMode(int num, GameMode gameMode) {
        if (num > 0 && num <= listLevel.get(gameMode).size()) {
            return listLevel.get(gameMode).get(num-1);
        }
        return null;
    }

    public void setListLevelByGameMode(List<Level> listLevelByGameMode, GameMode gameMode) {
        listLevel.put(gameMode, listLevelByGameMode);
    }

    public List<Level> convertToLevelList(List<LevelRow> levelRowList) {
        List<Level> levels = new ArrayList<>();

        for (LevelRow levelRow : levelRowList) {
            levels.add(levelRow.getLevel1());
            levels.add(levelRow.getLevel2());
            levels.add(levelRow.getLevel3());
        }

        return levels;
    }

    public List<LevelRow> convertToLevelRowList(List<Level> levelList) {
        List<LevelRow> levelRows = new ArrayList<>();
        LevelRow levelRow = new LevelRow();

        // TODO : Fix bug if number of level isn't a multiple to 3
        for (Level level : levelList) {

            if (level.getId() % 3 == 1) {
                levelRow.setLevel1(level);
            } else if (level.getId() % 3 == 2) {
                levelRow.setLevel2(level);
            } else if (level.getId() % 3 == 0) {
                levelRow.setLevel3(level);

                levelRows.add(levelRow);
                levelRow = new LevelRow();
            } else {
                return null;
            }
        }

        return levelRows;
    }

    private List<String> getTypeCardListByTheme(CardTheme theme) {
        List<String> listTypeCard = new ArrayList<>();
        CardType[] cardTypes = CardType.values();

        for (CardType cardType : cardTypes) {
            if (cardType.getTheme().equals(theme)) {
                listTypeCard.add(cardType.name());
            }
        }

        return listTypeCard;
    }

    public List<String> getRandomListByNumberCard(int numberCard, CardTheme theme) {
        List<String> listCardTemp = new ArrayList<>();
        List<String> listCard = new ArrayList<>();
        List<String> listCardType = getTypeCardListByTheme(theme);
        int maxCardType = listCardType.size();

        for (int i = 0; i < numberCard / 2; i++) {
            int cardType = (int) (Math.random() * maxCardType);

            if(numberCard == 4 && i > 0) {
                if(listCardTemp.get(0).equals(cardType)) {
                    if(cardType < maxCardType) {
                        cardType += 1;
                    } else {
                        cardType -= 1;
                    }
                }
            }
            listCardTemp.add(listCardType.get(cardType));
            listCardTemp.add(listCardType.get(cardType));
        }

        int maxCard = listCardTemp.size();

        while (!listCardTemp.isEmpty()) {
            int randomIndex = (int) (Math.random() * maxCard);

            listCard.add(listCardTemp.get(randomIndex));
            listCardTemp.remove(randomIndex);
            maxCard = maxCard - 1;
        }

        return listCard;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public List<CardView> getCardViews() {
        return cardViews;
    }

    public void setCardViews(List<CardView> cardViews) {
        this.cardViews = cardViews;
    }

    public void addCardView(CardView cardView) {
        if (this.cardViews != null) {
            cardViews.add(cardView);
        }
    }

    public List<CardView> getCardsFlip() {
        return cardsFlip;
    }

    public void setCardsFlip(List<CardView> cardsFlip) {
        this.cardsFlip = cardsFlip;
    }

    public void addCardFlip(CardView card) {
        cardsFlip.add(card);
    }

    public boolean containCard(CardView card) {
        if (this.cardsFlip != null && this.cardsFlip.size() > 0) {
            for (CardView c : this.cardsFlip) {
                if (c.getId() == card.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasFindAllCards() {
        int nbrCard = 0;

        if (getCardViews().size() > 0) {
            for (CardView card : getCardViews()) {
                if (card.getVisibility() == View.VISIBLE) {
                    nbrCard += 1;
                }
            }
        }

        if (nbrCard == 0) {
            return true;
        }

        return false;
    }

    public void resetCards() {
        int nbrCard = 0;
        if(getCardViews().size() > 0) {
            for(CardView card: getCardViews()) {
                card.setVisibility(View.INVISIBLE);
                if (card.getVisibility() == View.VISIBLE) {
                    nbrCard += 1;
                }
            }
        }
    }

    public int getNumCurrentLevel() {
        return currentLevel;
    }

    public void setNumCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public boolean goNextLevel() {
        return this.nextLevel;
    }

    public boolean hasNextLevel() {
        if(getLevelByNumAndGameMode(getNumNextLevel(), getCurrentMode()) != null) {
            return true;
        }

        return false;
    }

    public void setGoNextLevel(boolean goNextLevel) {
        this.nextLevel = goNextLevel;
    }

    public int getNumNextLevel() {
        if(getLevelByNumAndGameMode(currentLevel, getCurrentMode()) != null) {
            return currentLevel + 1;
        }

        return currentLevel;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public List<Challenge> getChallengesByType(final ChallengeType type, final GameMode mode)  {
        List<Challenge> listChallenge = new ArrayList<>();

        for(Challenge challenge: getChallenges()) {
            if(challenge.getChallengeType().equals(type) && challenge.getMode().equals(mode)) {
                listChallenge.add(challenge);
            }
        }

        return listChallenge;
    }

    public void checkChallengeDone(Dao dao, Context context, RelativeLayout parent, ChallengeType type, int value) {
        List<Challenge> challenges = GameInformation.getInstance().getChallengesByType(type, GameInformation.getInstance().getCurrentMode());
        for(Challenge challenge: challenges) {
            if(type.equals(ChallengeType.END_LEVEL) || type.equals(ChallengeType.GLOBAL_STAR)) {
                if(value >= challenge.getValueToReach()  && !challenge.isUnlockChallenge()) {
                    displayUnlockChallenge(challenge, context, dao, parent);
                }
            } else {
                if(value == challenge.getValueToReach()  && !challenge.isUnlockChallenge()) {
                    displayUnlockChallenge(challenge, context, dao, parent);
                }
            }
        }
    }

    public Level getCurrentLevel() {
        return this.getLevelByNumAndGameMode(this.getNumCurrentLevel(), this.getCurrentMode());
    }

    private void displayUnlockChallenge(Challenge challenge, Context context, Dao dao, RelativeLayout parent) {
        challenge.setUnlockChallenge(true);

        dao.setIsUnlockChallenge(challenge.getId(), challenge.isUnlockChallenge());
        UnlockChallengeView unlockChallengeView = new UnlockChallengeView(context, challenge);
        parent.addView(unlockChallengeView);
        unlockChallengeView.display();
    }

    public int getOverallNumberStars() {
        return overallNumberStars;
    }

    public void setOverallNumberStars(int overallNumberStars) {
        this.overallNumberStars = overallNumberStars;
    }
}
