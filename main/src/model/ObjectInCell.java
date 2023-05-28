package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.properties.ObjectKeeper;

import java.util.HashSet;
import java.util.Set;

/**
 * Объект, располагающийся в ячейке на поле
 */
public abstract class ObjectInCell {

    /* --------------- Ячейка ---------------------- */

    /**
     * Ячейка в которой находится объект
     */
    private Cell _cell;

    public Cell getCell(){
        Cell cell = _cell;
        if (_parent != null){
            cell = _parent.getCell();
        }
        return cell;
    }

    protected void setCell(Cell cell){
        if (_cell != null && _cell != cell){
            _cell.takeObject(this);
        }
        _parent = null;
        _cell = cell;
    }

    protected void unsetCell(){
        setCell(null);
    }

    /* ------------------- Родитель ------------- */

    /**
     * Родитель объекта
     */
    private ObjectKeeper<? extends ObjectInCell> _parent;

    protected void setParent(ObjectKeeper<? extends ObjectInCell> parent){
        if (!canBeParent(parent)) {
            return;
        }

        unsetCell();
        _parent = parent;
    }

    /**
     * Может ли объект быть родителем объекта
     * @param object проверяемы объект
     * @return true - если объект может быть родителем
     */
    protected boolean canBeParent(ObjectInCell object){
        boolean isObjectKeeper = false;
        if (object instanceof ObjectKeeper<?> objectKeeper){
            isObjectKeeper = canBeParent(objectKeeper);
        }
        return isObjectKeeper;
    }

    /**
     * Может ли хранитель быть родителем объекта
     * @param keeper проверяемый хранитель
     * @return true - если хранитель может быть родителем
     */
    private boolean canBeParent(ObjectKeeper<? extends ObjectInCell> keeper){
        return keeper.getObjectClass().isAssignableFrom(this.getClass());
    }

    /* -------------------------- Взаимодействие с другими объектами --------------- */

    /**
     * Состояние объекта
     * @return true - если объект уничтожен
     */
    public abstract boolean isDestroying();

    /**
     * Может ли объект находится в одной ячейке с указанным объектом
     * @param object объект, с котоым проверяется столкновение
     * @return true если столькновение возможно
     */
    public abstract boolean canFaceWith(ObjectInCell object);

    /**
     * Столкнуть объект с переданным
     * @param object объект для столкновения
     * @throws IllegalArgumentException если столкновение невозможно
     */
    protected void faceWith(ObjectInCell object){
        if (!canFaceWith(object)){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Обновление состояния объекта, вызываемое из игрового цикла
     */
    protected void update(){
        if (isDestroying() && getCell() != null){
            getCell().takeObject(this);
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYED));
        }
    }

    /* -------------------- Слушатели и события ------------------- */

    /**
     * Слушатели игрового объекта
     */
    private final Set<IObjectInCellEventListener> _listeners = new HashSet<>();

    public void addListener(IObjectInCellEventListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(IObjectInCellEventListener listener) {
        _listeners.remove(listener);
    }

    /**
     * Сообщить об изменение игрового объекта
     * @param event событие с информацией об объекте
     */
    protected void fireEvent(ObjectInCellEvent event){
        for (IObjectInCellEventListener listener : _listeners){
            listener.onObjectInCellAction(event);
        }
    }
}
