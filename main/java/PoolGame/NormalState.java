package PoolGame;

public class NormalState implements DifficultyState{
    private final int difficulty = 2;
    
    public int getDifficulty() {
        return difficulty;
    }
    public String initializeGame(App context)
    {
        return "src/main/resources/config_normal.json";
    }
}
