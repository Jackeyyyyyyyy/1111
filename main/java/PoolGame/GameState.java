package PoolGame;
import PoolGame.Items.Ball;

import java.util.List;

public class GameState {
    private int score;
    private int time;
    private List<Ball> balls;

    public GameState(int score, int time, List<Ball> balls) {
        this.score = score;
        this.time = time;
        this.balls = balls;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }
}
