package model;

import events.ObjectInCellEvent;

public class Wall extends Obstacle implements Damageable{

    private boolean isDestroyed = false;

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Bullet){
            isDestroyed = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (isDestroyed){
            getCell().takeObject(this);
        }
    }
}
