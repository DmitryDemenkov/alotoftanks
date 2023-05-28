package model.cellobjects.tank;

import events.IPlayerListener;
import model.measures.Direction;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс игрока, необходимый для управления танком извне
 */
public class Player {

    /* ------------------ Состояние игрока ----------------- */

    /**
     * Состояние игрока, true - если игрок активен
     */
    private boolean _isActive = false;

    public void setActive(boolean active){
        _isActive = active;
    }

    public boolean isActive(){
        return _isActive;
    }

    /* ----------------- Танк игрока ------------ */

    /**
     * Танк, принадлежащий игроку
     */
    private final Tank _tank;

    public Player(Tank tank){
        _tank = tank;
    }

    public Tank getTank(){
        return _tank;
    }

    /**
     * Узнать жив ли игрок
     * @return true - если танк и штаб игрока целы
     */
    public boolean isAlive(){
        return _tank.getHealth() > 0 && _tank.getHeadquarters().getCell() != null;
    }

    /* --------------------- Управление танком ---------------- */

    /**
     * Повернуть танк в указанном направлении
     * @param direction направление поворота
     */
    public void rotate(Direction direction){
        if (!isActive()){
            return;
        }
        _tank.rotate(direction);
    }

    /**
     * Переместить танк
     */
    public void move(){
        if (!isActive()){
            return;
        }

        if (_tank.move()){
            firePlayerMadeMove();
        }
    }

    /**
     * Совершить выстрел
     */
    public void shoot(){
        if (!isActive()){
            return;
        }

        if (_tank.shoot()){
            firePlayerMadeMove();
        }
    }

    /**
     * Пропустить ход
     */
    public void pass(){
        if (!isActive()){
            return;
        }

        _tank.pass();
        firePlayerMadeMove();
    }

    /* ------------------- Слушатели и события --------------- */

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
