package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;

public class Tank extends MovableObject implements Damageable {

    public Tank(){
        setDirection(Direction.NORTH);
    }

    /* --------------------- Состояние танка ------------------ */

    /**
     * Состояние танка, true если танк получает урон
     */
    private boolean _isDamaged = false;

    /* -------------------------- Штаб ----------------- */

    /**
     * Штаб, к которому относиться танк
     */
    private Headquarters _headquarters;

    public Headquarters getHeadquarters(){
        return _headquarters;
    }

    public void setHeadquarters(Headquarters headquarters){
        if (headquarters == _headquarters){
            return;
        }

        _headquarters = headquarters;
        headquarters.setTank(this);
    }

    /* ----------------- Управление танком ------------ */

    /**
     * Поворот танка в указанном направлении
     * @param direction направление поворота
     */
    public void rotate(Direction direction){
        if (direction == getDirection()){
            return;
        }

        setDirection(direction);
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
    }

    /**
     * Время перезарядки орудия
     */
    private final int RELOAD_TIME = 3;

    /**
     * Текущее время перезарядки
     */
    private int _currentReloadTime = 0;

    public int getCurrentReloadTime() { return _currentReloadTime; }

    /**
     * Соверщить выстрел
     * @return true если выстрел совершен успешно
     */
    public boolean shoot(){
        if (getCurrentReloadTime() > 0){
            return false;
        }

        Cell targetCell = getCell().getNeighbour(getDirection());
        if (targetCell == null){
            return false;
        }

        Bullet bullet = new Bullet(getDirection(), getCell());
        bullet.addListener(new BulletListener());
        fireEvent(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.NEED_UPDATE));
        _currentReloadTime = RELOAD_TIME;
        return true;
    }

    /**
     * Пропустить ход
     */
    public void pass(){
        _currentReloadTime -= 1;
    }

    @Override
    public boolean move() {
        boolean isMoved = super.move();
        if (isMoved){
            _currentReloadTime -= 1;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
        }
        return isMoved;
    }

    /* -------------------- Взаимодействие с другими объектами ------------------ */

    /**
     * Здоровье танка
     */
    private int _health = 3;

    public int getHealth(){
        return _health;
    }

    @Override
    public boolean isDestroying() {
        return false;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return object instanceof Bullet || object instanceof Thicket;
    }

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Bullet){
            _isDamaged = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    void update() {
        if (_isDamaged && getHealth() > 0){
            _health = getHealth() - 1;
            _isDamaged = false;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
        }
    }

    /* -------------------------- Слушатели и события ------------------- */

    /**
     * Слушатель снаряда
     */
    private class BulletListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            fireEvent(event);
        }
    }
}
