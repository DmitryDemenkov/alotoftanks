package model;

/**
 * Припятсвия, которые мешают танку
 */
public abstract class Obstacle extends ObjectInCell {
    @Override
    public boolean canFaceWith(ObjectInCell object){
        return object instanceof Bullet;
    }
}
