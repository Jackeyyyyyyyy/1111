package PoolGame;
import PoolGame.Items.Ball;

import java.util.Collections;
import java.util.List;

public class EasyState  implements DifficultyState{
    private final int difficulty = 1;

    public int getDifficulty() {
        return difficulty;
    }
    public String initializeGame(App context)
    {
        return "src/main/resources/config_easy.json";
    }
}
