package be.kdg.gobackend.game.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Score {
    double blackScore;
    double whiteScore;

    public Score(double blackScore, double whiteScore) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public Score() {
        this.blackScore = 0;
        this.whiteScore = 0;
    }

    public void addScore(double score, Stone stone) {
        if(stone.equals(Stone.BLACK)) blackScore += score;
        else if(stone.equals(Stone.WHITE)) whiteScore += score;
    }

    public void addOneBlack() {
        blackScore++;
    }

    public void addOneWhite() {
        whiteScore++;
    }

    public void add(Score score) {
        blackScore += score.getBlackScore();
        whiteScore += score.getWhiteScore();
    }

    public void addBlack(double score) {
        blackScore += score;
    }

    public void addWhite(double score) {
        whiteScore += score;
    }

    public double getBlackMargin() {
        return blackScore - whiteScore;
    }

    public double getWhiteMargin() {
        return whiteScore - blackScore;
    }

    public Stone getWinner() {
        return blackScore > whiteScore ? Stone.BLACK : Stone.WHITE;
    }
}
