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

    /**
     * Ячейка в которой находится объект
     */
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
    public void faceWith(ObjectInCell object){
        if (!canFaceWith(object)){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Обновление состояния объекта, вызываемое из игрового цикла
     */
    abstract void update();

    /* -------------------- Слушатели и события ------------------- */

    /**
     * Слушатели игрового объекта
     */
    private final Set<IObjectInCellEventListener> _listeners = new HashSet<>();

    public void addListener(IObjectInCellEventListener listener) {
        _listeners.add(listener);
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
