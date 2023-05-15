package model;

import events.ObjectInCellEvent;

/**
 * Снаряд выпускаемый танком
 */
public class Bullet extends MovableObject{

    /**
     * Сосотояние снаряда, true - если мнаряд уничтожен
     */
    private boolean _isDestroying = false;

    @Override
    public boolean isDestroying(){
        return _isDestroying;
    }

    public Bullet(Direction direction, Cell startCell){
        setDirection(direction);
        setCell(startCell);
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return !(this == object);
    }

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Damageable){
            _isDestroying = true;
        }
    }

    @Override
    void update() {
        if (_isDestroying){
            destroy();
        } else {
            move();
        }
    }

    @Override
    public boolean move(){
        boolean isMoved = super.move();
        if (isMoved){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        } else {
            destroy();
        }
        return isMoved;
    }

    private void destroy(){
        getCell().takeObject(this);
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYED));
    }
}
