package view;

import model.Bullet;

import java.awt.*;
import java.io.File;

public class BulletWidget extends MovableObjectWidget {

    public BulletWidget(Bullet bullet){
        super(bullet);
    }

    @Override
    protected File getImageFile() {
        String path = ((Bullet)getObject()).isDetonating() ? "resources/explosion.png" : "resources/bullet.png";
        return new File(path);
    }

    @Override
    public Dimension getDimension(){
        int size = ((Bullet)getObject()).isDetonating() ? 60 : 20;
        return new Dimension(size, size);
    }

    @Override
    public Layer getLayer(){
        return Layer.TOP;
    }
}
