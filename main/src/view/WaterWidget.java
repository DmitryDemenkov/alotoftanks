package view;

import model.Water;

import java.io.File;

public class WaterWidget extends ObjectInCellWidget{

    public WaterWidget(Water water){
        super(water);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/water.png");
    }
}
