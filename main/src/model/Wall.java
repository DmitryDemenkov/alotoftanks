package model;

import events.ObjectInCellEvent;

/**
 * Стена, мешающая танку и уничтожаемая снарядом
 */
public class Wall extends Obstacle implements Damageable{

    /**
     * Состояние стены true если стена уничтожена
     */
    private boolean _isDestroyed = false;

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Bullet){
            _isDestroyed = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (_isDestroyed){
            getCell().takeObject(this);
        }
    }
}
