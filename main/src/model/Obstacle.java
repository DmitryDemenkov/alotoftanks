package model;

public abstract class Obstacle extends ObjectInCell {
    @Override
    public boolean canFaceWith(ObjectInCell object){
        return false;
    }
}
