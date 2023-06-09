package view;

import model.cellobjects.tank.Headquarters;
import view.utils.ColorUtil;

import java.awt.*;
import java.io.File;

/**
 * Представление штаба
 */
public class HeadquartersWidget extends ObjectInCellWidget{

    public HeadquartersWidget(Headquarters headquarters){
        super(headquarters);
    }

    /**
     * Цвет штаба
     */
    private Color _color;

    /**
     * Задать цвет
     * @param color задаваемый цвет
     */
    void setColor(Color color){
        _color = color;
    }

    /**
     * Получить цвет
     * @return цвет штаба
     */
    public Color getColor(){
        return _color;
    }

    @Override
    protected File getImageFile() {
        return getImageFileByColor(getColor());
    }

    /**
     * Получить изображение штаба, соответсвующее его цвету
     * @param color цвет штаба
     * @return файл с изображением штаба
     */
    private File getImageFileByColor(Color color) {
        String path = "resources/" + ColorUtil.ColorName(color) + "_HQ.png";
        return new File(path);
    }
}
