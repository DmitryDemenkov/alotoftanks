package model;

public class Headquarters extends ObjectInCell{

    public Tank getTank(){
        return null;
    }

    public void setTank(Tank tank){ }

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
