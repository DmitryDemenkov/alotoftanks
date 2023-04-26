package view;

import model.ObjectInCell;
import view.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Представление объекта в ячейке
 */
public abstract class ObjectInCellWidget extends JPanel {

    public ObjectInCellWidget(ObjectInCell object){
        _object = object;
        setPreferredSize(getDimension());
        setLayout(new GridBagLayout());
        repaint();
        revalidate();
        setOpaque(false);
    }

    /**
     * Виджет ячейки, в котором находится виджет объекта
     */
    private CellWidget _cellWidget;

    /**
     * Установить виджет ячейки
     * @param cellWidget задаваемый виджет ячейки
     */
    void setCellWidget(CellWidget cellWidget){
        _cellWidget = cellWidget;
    }

    /**
     * Получить виджет ячейки
     * @return виджет ячейки
     */
    public CellWidget getCellWidget() {
        return _cellWidget;
    }

    /**
     * Модель объекта в ячейки
     */
    private final ObjectInCell _object;

    /**
     * Получить объект, соответсвующий виджету
     * @return модель объекта
     */
    public ObjectInCell getObject(){
        return _object;
    }

    /**
     * Размеры объекта
     * @return размер объекта
     */
    public Dimension getDimension(){
        return new Dimension(80, 80);
    }

    /**
     * Получить слой представления
     * @return слой представления
     */
    public Layer getLayer(){
        return Layer.BOTTOM;
    }

    /**
     * Получить файл с изображением объекта
     * @return файл с изображением
     */
    protected abstract File getImageFile();

    /**
     * Получить изображение объекта
     * @return изображение объекта
     */
    protected BufferedImage getImage(){
        File file = getImageFile();

        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            image = ImageUtils.resizeImage(image, getDimension().width, getDimension().height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Отрисовать получение урона объетктом
     */
    protected void getDamage(){
        destroy();
    }

    /**
     * Уничтожить представление объекта
     */
    protected void destroy(){
        _cellWidget.removeObjectWidget(this);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setPreferredSize(getDimension());
        revalidate();
        g.drawImage(getImage(), 0, 0, null);
    }
}
