package model.cellobjects;

import events.ObjectInCellEvent;
import model.ObjectInCell;
import model.properties.Damageable;
import model.properties.Damaging;

/**
 * Стена, мешающая танку и уничтожаемая снарядом
 */
public class Wall extends Obstacle implements Damageable {

    /**
     * Состояние стены true если стена уничтожена
     */
    private boolean _isDestroyed = false;

    @Override
    public boolean isDestroying() {
        return _isDestroyed;
    }

    @Override
    protected void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Damaging){
            _isDestroyed = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    protected void update() {
        super.update();
    }
}
