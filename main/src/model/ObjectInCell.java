package model;

import events.IObjectInCellEventListener;

public abstract class ObjectInCell {

    public Cell getCell(){
        return null;
    }

    void setCell(Cell cell){ }

    public abstract boolean canFaceWith(ObjectInCell object);

    public abstract void faceWith(ObjectInCell object);

    abstract void update();

    public void addListener(IObjectInCellEventListener listener) { }
}
