package model;

import events.IObjectInCellEventListener;
import events.ITankEventListener;
import events.ObjectInCellEvent;

import java.util.HashSet;
import java.util.Set;

public class Tank extends MovableObject implements Damageable {

    public Tank(){
        setDirection(Direction.NORTH);
    }

    /* --------------------- Состояние танка ------------------ */

    /**
     * Интерактивность танка, true если танком можно управлять
     */
    private boolean _isActive = true;

    public boolean isActive(){
        return _isActive;
    }

    void setActive(boolean active){
        _isActive = active;
    }

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
        if (!isActive()){
            return;
        }

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
        if (!isActive() || getCurrentReloadTime() > 0){
            return false;
        }

        Cell targetCell = getCell().getNeighbour(getDirection());
        if (targetCell == null){
            return false;
        }

        Bullet bullet = new Bullet(getDirection(), getCell());
        bullet.addListener(new BulletListener());
        fireShot(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.NEED_UPDATE));
        _currentReloadTime = RELOAD_TIME;
        return true;
    }

    /**
     * Пропустить ход
     */
    public void pass(){
        if (!isActive()){
            return;
        }

        _currentReloadTime -= 1;
        fireSkip(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
    }

    @Override
    public boolean move() {
        if (!isActive()){
            return false;
        }

        boolean isMoved = super.move();
        if (isMoved){
            _currentReloadTime -= 1;
            fireMoved(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
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
    public boolean canFaceWith(ObjectInCell object) {
        return object instanceof Bullet;
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
     * Слушатели танка
     */
    private final Set<ITankEventListener> _listeners = new HashSet<>();

    @Override
    public void addListener(IObjectInCellEventListener listener){
        if (!(listener instanceof ITankEventListener tankListener)){
            return;
        }

        _listeners.add(tankListener);
    }

    @Override
    protected void fireEvent(ObjectInCellEvent event) {
        for (ITankEventListener listener : _listeners){
            listener.onObjectInCellAction(event);
        }
    }

    /**
     * Сообщить об успешном перемещении танка
     * @param event событие с информацией о танке
     */
    private void fireMoved(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankMoved(event);
        }
    }

    /**
     * Сообщить об успешном выстреле
     * @param event событие с информацией о выпущенном снаряде
     */
    private void fireShot(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankShot(event);
        }
    }

    /**
     * Сообщение о пропуске хода
     * @param event событие с информацией о танке
     */
    private void fireSkip(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankSkipStep(event);
        }
    }

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
