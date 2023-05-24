package model.cellobjects.tank;

import events.ObjectInCellEvent;
import model.ObjectInCell;
import model.cellobjects.tank.Tank;
import model.properties.Damageable;
import model.properties.Damaging;

/**
 * Штаб танка
 */
public class Headquarters extends ObjectInCell implements Damageable {

    /**
     * Состояние штабы, true если уничтожен
     */
    private boolean _isDestroying = false;

    /* ------------------ Танк -------------- */

    /**
     * Танк, относящийся с штабу
     */
    private Tank _tank;

    public Tank getTank(){
        return _tank;
    }

    public void setTank(Tank tank){
        if (tank == _tank){
            return;
        }

        _tank = tank;
        tank.setHeadquarters(this);
    }

    @Override
    public boolean isDestroying() {
        return _isDestroying;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return object instanceof Damaging || canBeParent(object);
    }

    @Override
    protected void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Damaging){
            _isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    protected void update() {
        super.update();
    }
}
