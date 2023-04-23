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

    public boolean isDetonating(){
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
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (_isDestroying){
            getCell().takeObject(this);
        } else {
            move();
        }
    }

    @Override
    public boolean move(){
        boolean isMoved = super.move();
        if (!_isDestroying && isMoved){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVING));
        } else if (!isMoved){
            getCell().takeObject(this);
        }
        return isMoved;
    }
}
