package events;

import model.Game;

/**
 * Слушатель игровых событий
 */
public interface IGameEventListener {

    void onObjectChanged(ObjectInCellEvent event);

    void onGameStateChanged(Game.State state);
}
