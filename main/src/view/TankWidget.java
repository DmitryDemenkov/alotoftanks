package view;

import model.Tank;

import java.awt.*;
import java.io.File;

/**
 * Прежставление танка
 */
public class TankWidget extends MovableObjectWidget {

    public TankWidget(Tank tank){
        super(tank);
    }

    /**
     * Получить модель танка
     * @return модель танка
     */
    private Tank getTank(){
        return (Tank) getObject();
    }

    /**
     * Установить активность виджета
     * @param active активность виджета
     */
    public void setActive(boolean active){
        getCellWidget().setActive(active);
        repaint();
    }

    /**
     * Цвет танка
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
     * Получить цвет танка
     * @return цвет танка
     */
    Color getColor(){
        return _color;
    }

    /**
     * Отрисовать получение урона
     */
    @Override
    public void getDamage(){
        if (getTank().getHealth() <= 0){
            super.getDamage();
        }
    }

    @Override
    public Dimension getDimension(){
        return new Dimension(60, 60);
    }

    @Override
    protected File getImageFile() {
        return getImageFileByColor(getColor());
    }

    /**
     * Получить файл с изображением танка, соответствующее его цвету
     * @param color цвет танка
     * @return файл с изоборажением танка
     */
    private File getImageFileByColor(Color color){
        String path = color == Color.BLUE ? "resources/blue_tank.png" : "resources/orange_tank.png";
        return new File(path);
    }
}
