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
    public boolean isDestroying() {
        return _isDestroyed;
    }

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Bullet){
            _isDestroyed = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }
}
