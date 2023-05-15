package view;

import model.cellobjects.Thicket;

import java.awt.*;
import java.io.File;

public class ThicketWidget extends ObjectInCellWidget{

    public ThicketWidget(Thicket object) {
        super(object);
    }

    @Override
    protected File getImageFile() {
        return new File("resources/thicket.png");
    }

    @Override
    public Layer getLayer(){
        return Layer.MIDDLE;
    }

    @Override
    public Dimension getDimension(){
        return new Dimension(60, 60);
    }
}
