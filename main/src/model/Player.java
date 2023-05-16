package model;

import events.IPlayerListener;
import model.measures.Direction;

import java.util.HashSet;
import java.util.Set;

public class Player {

    private boolean _isActive = false;

    void setActive(boolean active){
        _isActive = active;
    }

    public boolean isActive(){
        return _isActive;
    }

    private final Tank _tank;

    public Player(Tank tank){
        _tank = tank;
    }

    public Tank getTank(){
        return _tank;
    }

    public boolean isAlive(){
        return _tank.getHealth() > 0 && _tank.getHeadquarters().getCell() != null;
    }

    public void rotate(Direction direction){
        if (!isActive()){
            return;
        }
        _tank.rotate(direction);
    }

    public void move(){
        if (!isActive()){
            return;
        }

        if (_tank.move()){
            firePlayerMadeMove();
        }
    }

    public void shoot(){
        if (!isActive()){
            return;
        }

        if (_tank.shoot()){
            firePlayerMadeMove();
        }
    }

    public void pass(){
        if (!isActive()){
            return;
        }

        _tank.pass();
        firePlayerMadeMove();
    }

    private final Set<IPlayerListener> _listeners = new HashSet<>();

    public void addListener(IPlayerListener listener){
        _listeners.add(listener);
    }

    public void removeListener(IPlayerListener listener){
        _listeners.remove(listener);
    }

    private void firePlayerMadeMove(){
        for (IPlayerListener listener : _listeners){
            listener.onPlayerMadeMove();
        }
    }
}
