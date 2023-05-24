package view;

import model.cellobjects.Water;

import java.io.File;

/**
 * Представление воды
 */
public class WaterWidget extends ObjectInCellWidget{

    public WaterWidget(Water water){
        super(water);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/water.png");
    }
}
