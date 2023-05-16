package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.properties.TankKeeper;

public class Thicket extends Obstacle implements TankKeeper {

    private Tank _tank;

    @Override
    public Tank getTank(){
        return _tank;
    }

    @Override
    public boolean isDestroying() {
        return false;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object){
        return super.canFaceWith(object) || (object instanceof Tank && getTank() == null);
    }

    @Override
    void faceWith(ObjectInCell object) {
        super.faceWith(object);

        if (object instanceof Tank tank){
            _tank = tank;
            getTank().addListener(_tankListener);
            fireNeedUpdate();
        }
    }

    @Override
    void update(){
        if (getTank() == null || getCell() == null){
            return;
        }

        if (getTank().getCell() == getCell()){
            getCell().takeObject(getTank());
            getTank().setCell(getCell());
        } else {
            getTank().removeListener(_tankListener);
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
            if (event.getObject() != getTank()) {
                return;
            }

            if (event.getType() == ObjectInCellEvent.EventType.MOVED && event.getObject().getCell() != getCell()){
                fireNeedUpdate();
            }
        }
    }
}
