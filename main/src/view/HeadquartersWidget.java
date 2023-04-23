package view;

import model.Headquarters;

import java.awt.*;
import java.io.File;

public class HeadquartersWidget extends ObjectInCellWidget{

    public HeadquartersWidget(Headquarters headquarters){
        super(headquarters);
    }

    private Color _color;

    void setColor(Color color){
        _color = color;
    }

    public Color getColor(){
        return _color;
    }

    @Override
    protected File getImageFile() {
        return getImageFileByColor(getColor());
    }

    private File getImageFileByColor(Color color) {
        String path = color == Color.BLUE ? "resources/blue_HQ.png" : "resources/orange_HQ.png";
        return new File(path);
    }
}
