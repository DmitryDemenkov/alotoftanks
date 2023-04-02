package model;

import events.IGameEventListener;
import events.IObjectInCellEventListener;
import events.ITankEventListener;
import events.ObjectInCellEvent;
import model.environment.Environment;

import java.util.*;

public class Game {

    public enum State {
        GAME_IS_ON,
        WINNER_FOUND
    }

    private final Field _field;

    public Game(Environment environment){
        _field = new Field(environment);
        _field.addObjectInCellListener(new ObjectListener());
    }

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

    private Tank _activeTank = null;

    public Tank activeTank() {
        return _activeTank;
    }

    private Tank _winner = null;

    public Tank winner() {
        return _winner;
    }

    private final Queue<ObjectInCellEvent> activeEvents = new ArrayDeque<>();

    private void gameLoop(){
        int eventsCount = activeEvents.size();
        while (eventsCount > 0){
            for (int i = 0; i < eventsCount; i++){
                ObjectInCellEvent event = activeEvents.remove();
                event.getObject().update();
                fireObjectChanged(event);
            }
            waitRendering();
            eventsCount = activeEvents.size();
        }
    }

    private void waitRendering() {
        try {
            long sleepTime = 500L;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private State checkState(){
        ArrayList<Tank> tanks = _field.getTanks();

        for (int i = 0; i < tanks.size() && _winner == null; i++){
            Tank tank = tanks.get(i);
            Headquarters headquarters = tank.getHeadquarters();
            if (tank.getHealth() == 0 || headquarters.getCell() == null){
                _winner = getOtherTank(tank);
            }
        }

        State state = State.GAME_IS_ON;
        if (_winner != null){
            state = State.WINNER_FOUND;
        }
        return state;
    }

    private Tank getOtherTank(Tank tank){
        return _field.getTanks().stream().filter(t -> t != tank).findAny().orElse(null);
    }

    private final Set<IGameEventListener> _listeners = new HashSet<>();

    public void addListener(IGameEventListener listener){
        _listeners.add(listener);
    }

    private void fireObjectChanged(ObjectInCellEvent event){
        for (IGameEventListener listener : _listeners){
            listener.onObjectChanged(event);
        }
    }

    private void fireGameStateGanged(State state){
        for (IGameEventListener listener : _listeners){
            listener.onGameStateChanged(state);
        }
    }

    private class ObjectListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            activeEvents.add(event);
        }
    }

    private class TankListener implements ITankEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            if (event.getObject() == activeTank() && event.getType() == ObjectInCellEvent.EventType.MOVING){
                fireObjectChanged(event);
            } else {
                activeEvents.add(event);
            }
        }

        @Override
        public void onTankMoved(ObjectInCellEvent event) {
            if (event.getObject() != activeTank()){
                return;
            }

            activeTank().setActive(false);
            fireObjectChanged(event);

            _activeTank = getOtherTank(activeTank());
            activeTank().setActive(true);

            fireGameStateGanged(State.GAME_IS_ON);
        }

        @Override
        public void onTankShot(ObjectInCellEvent event) {
            activeTank().setActive(false);

            activeEvents.add(event);
            gameLoop();
            State state = checkState();
            if (state == State.GAME_IS_ON){
                _activeTank = getOtherTank(activeTank());
                activeTank().setActive(true);
            }
            fireGameStateGanged(state);
        }

        @Override
        public void onTankSkipStep(ObjectInCellEvent event) {
            if (event.getObject() != activeTank()){
                return;
            }

            activeTank().setActive(false);
            _activeTank = getOtherTank(activeTank());
            activeTank().setActive(true);

            fireGameStateGanged(State.GAME_IS_ON);
        }
    }
}
