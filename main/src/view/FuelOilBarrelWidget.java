package view;

import model.cellobjects.FuelOilBarrel;

import java.awt.*;
import java.io.File;

public class FuelOilBarrelWidget extends ObjectInCellWidget{

    public FuelOilBarrelWidget(FuelOilBarrel barrel) {
        super(barrel);
    }

    @Override
    protected void getDamage(){
        repaint();
    }

    @Override
    public Dimension getDimension(){
        return new Dimension(60, 60);
    }

    @Override
    protected File getImageFile() {
        return new File(getImageFileName());
    }

    private String getImageFileName(){
        String filename = "resources/fuel_oil_barrel";
        if (getObject().isDestroying()){
            filename += "_detonating";
        }
        filename += ".png";

        return filename;
    }
}
