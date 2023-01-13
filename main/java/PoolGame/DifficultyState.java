package PoolGame;

import javafx.collections.ObservableList;
import javafx.scene.Node;


    public interface DifficultyState {
        int getDifficulty();
        String initializeGame(App context);
    }

