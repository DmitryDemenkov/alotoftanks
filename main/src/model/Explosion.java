package model;

import events.ObjectInCellEvent;
import model.properties.Damaging;

public class Explosion extends ObjectInCell implements Damaging {

    private boolean _isDestroying = false;

    @Override
    public boolean isDestroying() {
        return _isDestroying;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return this != object;
    }

    @Override
    void setCell(Cell cell){
        super.setCell(cell);
        _isDestroying = true;
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
    }
}
