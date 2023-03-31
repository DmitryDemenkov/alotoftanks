package model;

public class Bullet extends MovableObject{

    public Bullet(Direction direction, Cell startCell){ }

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
