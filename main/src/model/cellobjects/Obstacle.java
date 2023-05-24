package model.cellobjects;

import model.ObjectInCell;
import model.properties.Damaging;

/**
 * Припятсвия, которые мешают танку
 */
public abstract class Obstacle extends ObjectInCell {
    @Override
    public boolean canFaceWith(ObjectInCell object){
        return super.canFaceWith(object) || object instanceof Damaging;
    }

    @Override
    protected void faceWith(ObjectInCell object){
        super.faceWith(object);
    }
}
