package PoolGame;

public class ScoreDisplay implements Observer{
    private int score;

    public void update(Game subject) {
        if (subject instanceof Game) {
            Game game = (Game) subject;
            score = game.getScore();
            displayScore(subject);
        }
    }

    public void displayScore(Game subject) {
        String scoreString = String.format("Score: %d", score);
       subject.getScoreText().setText(scoreString);
    }

}
