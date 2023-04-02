package model;

import events.ObjectInCellEvent;

public class Bullet extends MovableObject{

    private boolean isDestroying = false;

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
            isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (isDestroying){
            getCell().takeObject(this);
        } else {
            move();
        }
    }

    @Override
    public boolean move(){
        boolean isMoved = super.move();
        if (!isDestroying && isMoved){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVING));
        } else if (!isMoved){
            getCell().takeObject(this);
        }
        return isMoved;
    }
}
