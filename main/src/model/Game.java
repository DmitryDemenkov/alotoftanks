package model;

import events.IGameEventListener;
import events.IObjectInCellEventListener;
import events.ITankEventListener;
import events.ObjectInCellEvent;
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
        WINNER_FOUND
    }

    public Game(Environment environment){
        _field = new Field(environment);
        _field.addObjectInCellListener(new ObjectListener());
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
        ArrayList<Tank> tanks = _field.getTanks();
        TankListener tankListener = new TankListener();
        for (Tank tank : tanks){
            tank.setActive(false);
            tank.addListener(tankListener);
        }

        _activeTank = tanks.get(0);
        activeTank().setActive(true);
    }

    /* ---------------- Танки ----------------- */

    /**
     * Текущий активный танк, которым можно управлять
     */
    private Tank _activeTank = null;

    public Tank activeTank() {
        return _activeTank;
    }

    /**
     * Танк - победитель, определенный после окончания игры
     */
    private Tank _winner = null;

    public Tank winner() {
        return _winner;
    }

    /**
     * Получить танк-противник, для данного танка
     * @param tank танк, для которого нужно определить противника
     * @return танк-противник
     */
    private Tank getEnemyTank(Tank tank){
        return _field.getTanks().stream().filter(t -> t != tank).findAny().orElse(tank);
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
                _activeTank = getEnemyTank(activeTank());
                activeTank().setActive(true);
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
        ArrayList<Tank> tanks = _field.getTanks();

        for (int i = 0; i < tanks.size() && _winner == null; i++){
            Tank tank = tanks.get(i);
            Headquarters headquarters = tank.getHeadquarters();
            if (tank.getHealth() == 0 || headquarters.getCell() == null){
                _winner = getEnemyTank(tank);
            }
        }

        State state = State.GAME_IS_ON;
        if (_winner != null){
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

    /**
     * Слушатель танков
     */
    private class TankListener implements ITankEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            if (event.getType() == ObjectInCellEvent.EventType.NEED_UPDATE) {
                _activeEvents.add(event);
            } else {
                fireObjectChanged(event);
            }
        }

        @Override
        public void onTankMoved(ObjectInCellEvent event) {
            if (event.getObject() != activeTank()){
                return;
            }

            activeTank().setActive(false);
            fireObjectChanged(event);

            _activeTank = getEnemyTank(activeTank());
            activeTank().setActive(true);

            fireGameStateGanged(State.GAME_IS_ON);
        }

        @Override
        public void onTankShot(ObjectInCellEvent event) {
            activeTank().setActive(false);

            _activeEvents.add(event);
            gameLoop();
        }

        @Override
        public void onTankSkipStep(ObjectInCellEvent event) {
            if (event.getObject() != activeTank()){
                return;
            }

            activeTank().setActive(false);
            _activeTank = getEnemyTank(activeTank());
            activeTank().setActive(true);

            fireGameStateGanged(State.GAME_IS_ON);
        }
    }
}
