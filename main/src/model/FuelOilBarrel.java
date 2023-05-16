package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.measures.Direction;
import model.properties.Damageable;
import model.properties.Damaging;

public class FuelOilBarrel extends Obstacle implements Damageable {

    private boolean _isDestroying = false;

    @Override
    public boolean isDestroying() {
        return _isDestroying;
    }

    @Override
    void faceWith(ObjectInCell object){
        super.faceWith(object);

        if (object instanceof Damaging){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    void update(){
        if (!isDestroying()){
            detonate();
            _isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        } else {
            super.update();
        }
    }

    private void detonate(){
        ExplosionListener listener = new ExplosionListener();
        for (Direction direction : Direction.values()){
            Cell neighbour = getCell().getNeighbour(direction);
            if (neighbour != null){
                Explosion explosion = new Explosion();
                explosion.addListener(listener);
                neighbour.addObject(explosion);
            }
        }
    }

    private class ExplosionListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            fireEvent(event);
        }
    }
}
