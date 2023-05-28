package model.cellobjects.damaging;

import events.ObjectInCellEvent;
import model.Cell;
import model.ObjectInCell;
import model.cellobjects.MovableObject;
import model.measures.Direction;
import model.properties.Damageable;
import model.properties.Damaging;

/**
 * Снаряд выпускаемый танком
 */
public class Bullet extends MovableObject implements Damaging {

    /**
     * Сосотояние снаряда, true - если мнаряд уничтожен
     */
    private boolean _isDestroying = false;

    @Override
    public boolean isDestroying(){
        return _isDestroying;
    }

    /**
     * Состояние снаряда, true - если снаряд самостоятельно не движется
     */
    private boolean _isFrozen = false;

    public Bullet(Direction direction, Cell startCell){
        setDirection(direction);
        setCell(startCell);
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return !(this == object);
    }

    @Override
    protected void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Damageable){
            _isDestroying = true;
        }

        if (canBeParent(object)){
            _isFrozen = true;
        }
    }

    @Override
    protected void update() {
        if (_isDestroying){
            destroy();
        } else {
            move();
        }
    }

    @Override
    protected boolean move(){
        return super.move();
    }

    @Override
    protected boolean moveAt(Cell newCell){
        boolean isMoved = super.moveAt(newCell);
        if (isMoved){
            fireMoved();
        } else {
            destroy();
        }
        return isMoved;
    }

    /**
     * Испустить сообщение о перемещении
     */
    private void fireMoved(){
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.MOVED));
        if (!_isFrozen){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        } else {
            _isFrozen = false;
        }
    }

    /**
     * Уничтожить снаряд
     */
    private void destroy(){
        getCell().takeObject(this);
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYED));
    }
}
