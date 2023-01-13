package PoolGame;

public class HardState implements DifficultyState{
    private final int difficulty = 3;
    
    public int getDifficulty() {
        return difficulty;
    }
    public String initializeGame(App context)
    {
        return "src/main/resources/config_hard.json";
    }
}
