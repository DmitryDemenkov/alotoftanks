package model;

import events.IObjectInCellEventListener;
import events.ITankEventListener;
import events.ObjectInCellEvent;

import java.util.HashSet;
import java.util.Set;

public class Tank extends MovableObject implements Damageable {

    private boolean _isActive = true;

    boolean isActive(){
        return _isActive;
    }

    void setActive(boolean active){
        _isActive = active;
    }

    private boolean _isDamaged = false;

    public Tank(){
        setDirection(Direction.NORTH);
    }

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

    public void rotate(Direction direction){
        if (!isActive()){
            return;
        }

        if (direction == getDirection()){
            return;
        }

        setDirection(direction);
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVING));
    }

    private final int RELOAD_TIME = 3;

    private int _currentReloadTime = 0;

    public int getCurrentReloadTime() { return _currentReloadTime; }

    public boolean shoot(){
        if (!isActive() || getCurrentReloadTime() > 0){
            return false;
        }

        Cell targetCell = getCell().neighbour(getDirection());
        if (targetCell == null){
            return false;
        }

        Bullet bullet = new Bullet(getDirection(), getCell());
        bullet.addListener(new BulletListener());
        fireShot(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.MOVING));
        _currentReloadTime = RELOAD_TIME;
        return true;
    }

    public void pass(){
        if (!isActive()){
            return;
        }

        _currentReloadTime -= 1;
        fireSkip(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVING));
    }

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
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
        }
    }

    @Override
    void update() {
        if (_isDamaged && getHealth() > 0){
            _health = getHealth() - 1;
            _isDamaged = false;
        }
    }

    @Override
    public boolean move() {
        if (!isActive()){
            return false;
        }

        boolean isMoved = super.move();
        if (isMoved){
            _currentReloadTime -= 1;
            fireMoved(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVING));
        }
        return isMoved;
    }

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

    private void fireMoved(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankMoved(event);
        }
    }

    private void fireShot(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankShot(event);
        }
    }

    private void fireSkip(ObjectInCellEvent event){
        for (ITankEventListener listener : _listeners){
            listener.onTankSkipStep(event);
        }
    }

    private class BulletListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            fireEvent(event);
        }
    }
}
