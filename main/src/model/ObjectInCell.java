package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Объект, располагающийся в ячейке на поле
 */
public abstract class ObjectInCell {

    /* --------------- Ячейка ---------------------- */

    private Cell _cell;

    public Cell getCell(){
        return _cell;
    }

    void setCell(Cell cell){
        if (_cell != null){
            _cell.takeObject(this);
        }
        _cell = cell;
    }

    void unsetCell(){
        _cell = null;
    }

    /* -------------------------- Взаимодействие с другими объектами --------------- */

    public abstract boolean canFaceWith(ObjectInCell object);

    public void faceWith(ObjectInCell object){
        if (!canFaceWith(object)){
            throw new IllegalArgumentException();
        }
    }

    abstract void update();

    /* -------------------- Слушатели и события ------------------- */

    private final Set<IObjectInCellEventListener> _listeners = new HashSet<>();

    public void addListener(IObjectInCellEventListener listener) {
        _listeners.add(listener);
    }

    protected void fireEvent(ObjectInCellEvent event){
        for (IObjectInCellEventListener listener : _listeners){
            listener.onObjectInCellAction(event);
        }
    }
}
