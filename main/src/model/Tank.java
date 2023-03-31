package model;

public class Tank extends MovableObject{

    public Headquarters getHeadquarters(){
        return null;
    }

    public void setHeadquarters(Headquarters headquarters){ }

    public void setDirection(Direction direction){ }

    public boolean shoot(){ return false; }

    public void pass(){ }

    public int getHealth(){
        return -1;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return false;
    }

    @Override
    public void faceWith(ObjectInCell object) {

    }

    @Override
    void update() {

    }
}
