package view;

import model.cellobjects.Wall;

import java.io.File;

/**
 * Представление стены
 */
public class WallWidget extends ObjectInCellWidget{

    public WallWidget(Wall wall){
        super(wall);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/wall.png");
    }
}
