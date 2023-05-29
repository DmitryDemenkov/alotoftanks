package model;

import events.*;
import model.cellobjects.tank.Player;
import model.cellobjects.tank.Tank;
import model.environment.Environment;

import java.util.*;

/**
 * Главный класс игровой модели
 */
public class Game {

    /**
     * Состояние игры
     */
    public enum State {
        GAME_IS_ON,
        WINNER_FOUND,
        DRAW
    }

    public Game(Environment environment){
        _field = new Field(environment);
        _field.addObjectInCellListener(new ObjectListener());

        ArrayList<Tank> tanks = _field.getTanks();
        PlayerListener playerListener = new PlayerListener();
        for (Tank tank : tanks){
            Player player = new Player(tank);
            player.addListener(playerListener);
            _players.add(player);
        }
    }

    /* ----------------- Игровое поле ----------------- */

    /**
     * Игровое поле
     */
    private final Field _field;

    public Field getField(){
        return _field;
    }

    /**
     * Запуск игры
     */
    public void start() {
        _activePlayer = _players.get(0);
        activePlayer().setActive(true);
    }

    /* ---------------- Танки ----------------- */

    private final ArrayList<Player> _players = new ArrayList<>();

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(_players);
    }

    /**
     * Текущий активный танк, которым можно управлять
     */
    private Player _activePlayer = null;

    public Player activePlayer() {
        return _activePlayer;
    }

    /**
     * Танк - победитель, определенный после окончания игры
     */
    private Player _winner = null;

    public Player winner() {
        return _winner;
    }

    /**
     * Получить танк-противник, для данного танка
     *
     * @param player танк, для которого нужно определить противника
     * @return танк-противник
     */
    private Player getEnemy(Player player){
        return _players.stream().filter(p -> p != player).findAny().orElse(player);
    }

    /* ----------------- Игровой цикл ------------ */

    /**
     * События требующие обработки в игровом цикле
     */
    private final Queue<ObjectInCellEvent> _activeEvents = new ArrayDeque<>();

    private final Timer _timer = new Timer();

    /**
     * Игровой цикл
     */
    private void gameLoop(){
        int eventsCount = _activeEvents.size();
        if (eventsCount > 0){
            for (int i = 0; i < eventsCount; i++){
                ObjectInCellEvent event = _activeEvents.remove();
                event.getObject().update();
            }
            waitRendering();
        } else {
            State state = checkState();
            if (state == State.GAME_IS_ON){
                _activePlayer = getEnemy(activePlayer());
                activePlayer().setActive(true);
            }
            fireGameStateGanged(state);
        }
    }

    /**
     * Ожидание отрисовки ереданных событий
     */
    private void waitRendering() {
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameLoop();
            }
        }, 500L);
    }

    /**
     * Определить текущее состояние игры
     * @return текущее состояние
     */
    private State checkState(){
        Player winner = null;
        int alivePlayersCount = 0;
        for (Player player : _players) {
            if (!player.isAlive()) {
                winner = getEnemy(player);
            } else {
                alivePlayersCount++;
            }
        }

        State state = State.GAME_IS_ON;
        if (alivePlayersCount == 0) {
            state = State.DRAW;
        } else if (winner != null){
            _winner = winner;
            state = State.WINNER_FOUND;
        }
        return state;
    }

    /* --------------- Игровые слушатели ---------------- */

    /**
     * Слушатели игровых событий
     */
    private final Set<IGameEventListener> _listeners = new HashSet<>();

    public void addListener(IGameEventListener listener){
        _listeners.add(listener);
    }

    /* ---------------- События -------------------- */

    /**
     * Сообщить о том, что объект на поле изменился
     * @param event событие, содержащий информацию об объекте
     */
    private void fireObjectChanged(ObjectInCellEvent event){
        for (IGameEventListener listener : _listeners){
            listener.onObjectChanged(event);
        }
    }

    /**
     * Сообщить о том что состояние игры изиенились
     * @param state новое состояние игры
     */
    private void fireGameStateGanged(State state){
        for (IGameEventListener listener : _listeners){
            listener.onGameStateChanged(state);
        }
    }

    /**
     * Слушатель игровых объектов нв поле
     */
    private class ObjectListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            if (event.getType() == ObjectInCellEvent.EventType.NEED_UPDATE) {
                _activeEvents.add(event);
            } else {
                fireObjectChanged(event);
            }
        }
    }

    private class PlayerListener implements IPlayerListener {

        @Override
        public void onPlayerMadeMove() {
            activePlayer().setActive(false);
            gameLoop();
        }
    }
}
