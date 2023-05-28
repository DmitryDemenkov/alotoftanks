package model.testprefabs;

import model.ObjectInCell;
import model.cellobjects.damaging.Bullet;
import model.properties.ObjectKeeper;

public class BulletKeeper extends ObjectInCell implements ObjectKeeper<Bullet> {
    @Override
    public boolean isDestroying() {
        return false;
    }

    @Override
    public boolean canFaceWith(ObjectInCell object) {
        return object instanceof Bullet;
    }


    @Override
    public Bullet getObject() {
        return null;
    }

    @Override
    public Class<Bullet> getObjectClass() {
        return Bullet.class;
    }
}
