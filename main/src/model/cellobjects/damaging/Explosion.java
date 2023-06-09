package model.cellobjects.damaging;

import events.ObjectInCellEvent;
import model.Cell;
import model.ObjectInCell;
import model.properties.Damaging;

/**
 * Взрыв - поражающий объект, который поражает только одну ячейку
 */
public class Explosion extends ObjectInCell implements Damaging {

    /**
     * Состояние взрыва, true - если взрыв уничтожен
     */
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
    protected void setCell(Cell cell){
        super.setCell(cell);
        if (cell != null) {
            _isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    protected  void update(){
        super.update();
    }
}
