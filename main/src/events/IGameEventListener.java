package events;

import model.Game;

public interface IGameEventListener {

    void onObjectChanged(ObjectInCellEvent event);

    void onGameStateChanged(Game.State state);
}
