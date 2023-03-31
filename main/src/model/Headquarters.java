package model;

import events.ObjectInCellEvent;

public class Headquarters extends ObjectInCell implements Damageable {

    private boolean isDestroying = false;

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
            isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DESTROYING));
        }
    }

    @Override
    void update() {
        if (isDestroying){
            getCell().takeObject(this);
        }
    }
}
