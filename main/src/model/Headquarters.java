package model;

import events.ObjectInCellEvent;

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
    public boolean canFaceWith(ObjectInCell object) {
        return object instanceof Bullet;
    }

    @Override
    public void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Bullet){
            _isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (_isDestroying){
            getCell().takeObject(this);
        }
    }
}
