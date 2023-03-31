package model;

import events.IGameEventListener;
import model.environment.Environment;

public class Game {

    public enum State {
        GAME_IS_ON,
        WINNER_FOUND
    }

    public Game(Environment environment){ }

    public void start() { }

    public Tank activeTank() {
        return null;
    }

    public Tank winner() {
        return null;
    }

    public void addListener(IGameEventListener listener){ }


}
