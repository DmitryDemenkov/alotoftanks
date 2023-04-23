package view;

import model.Wall;

import java.io.File;

public class WallWidget extends ObjectInCellWidget{

    public WallWidget(Wall wall){
        super(wall);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/wall.png");
    }
}
