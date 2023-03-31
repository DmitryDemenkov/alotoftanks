package model;

public abstract class MovableObject extends ObjectInCell{

    public Direction getDirection(){
        return null;
    }

    public boolean move(){
        return false;
    }
}
