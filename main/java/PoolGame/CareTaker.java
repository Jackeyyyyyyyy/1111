package PoolGame;

import java.util.LinkedList;

public class CareTaker {
    private LinkedList<GameState> mementos = new LinkedList<>();

    public void saveState(Game game) {
        mementos.addFirst(game.saveState());
    }

    public void undo(Game game) {
        if (!mementos.isEmpty()) {
            game.setGameState(mementos.poll());
        }
    }

}
