package view;

import model.cellobjects.damaging.Explosion;

import java.awt.*;
import java.io.File;

public class ExplosionWidget extends ObjectInCellWidget{

    public ExplosionWidget(Explosion explosion) {
        super(explosion);
    }

    @Override
    public Dimension getDimension(){
        return new Dimension(60, 60);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/explosion.png");
    }

    @Override
    protected void getDamage(){
        repaint();
        revalidate();
    }
}
