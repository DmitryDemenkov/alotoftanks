package model.cellobjects;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.Cell;
import model.ObjectInCell;
import model.cellobjects.damaging.Explosion;
import model.measures.Direction;
import model.properties.Damageable;
import model.properties.Damaging;

/**
 * Бочка мазута
 */
public class FuelOilBarrel extends Obstacle implements Damageable {

    /**
     * Состояние бочки, true - если бочка взорвана
     */
    private boolean _isDestroying = false;

    @Override
    public boolean isDestroying() {
        return _isDestroying;
    }

    @Override
    protected void faceWith(ObjectInCell object){
        super.faceWith(object);

        if (object instanceof Damaging){
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        }
    }

    @Override
    protected void update(){
        if (!isDestroying()){
            detonate();
            _isDestroying = true;
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.DAMAGED));
            fireEvent(new ObjectInCellEvent(this, ObjectInCellEvent.EventType.NEED_UPDATE));
        } else {
            super.update();
        }
    }

    /**
     * Взорваться
     */
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
