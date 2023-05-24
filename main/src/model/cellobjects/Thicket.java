package model.cellobjects;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.ObjectInCell;
import model.cellobjects.tank.Tank;
import model.properties.ObjectKeeper;

public class Thicket extends Obstacle implements ObjectKeeper<Tank> {

    private Tank _tank;

    @Override
    public Tank getObject(){
        return _tank;
    }

    @Override
    public Class<Tank> getObjectClass() {
        return Tank.class;
    }

    @Override
    public boolean isDestroying() {
        return false;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object){
        return super.canFaceWith(object) || (object instanceof Tank && getObject() == null);
    }

    @Override
    protected void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Tank tank){
            _tank = tank;
            getObject().addListener(_tankListener);
            fireNeedUpdate();
        }
    }

    @Override
    protected void update(){
        if (getObject() == null || getCell() == null){
            return;
        }

        if (getObject().getCell() == getCell()){
            getCell().takeObject(getObject());
            getObject().setParent(this);
        } else {
            getObject().removeListener(_tankListener);
            _tank = null;
        }
    }

    private void fireNeedUpdate(){
        fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
    }

    private final TankListener _tankListener = new TankListener();

    private class TankListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            if (event.getObject() != getObject()) {
                return;
            }

            if (event.getType() == ObjectInCellEvent.EventType.MOVED && event.getObject().getCell() != getCell()){
                fireNeedUpdate();
            }
        }
    }
}
