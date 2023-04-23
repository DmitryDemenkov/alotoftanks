package view;

import model.ObjectInCell;
import view.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ObjectInCellWidget extends JPanel {

    public ObjectInCellWidget(ObjectInCell object){
        _object = object;
        setPreferredSize(getDimension());
        setLayout(new GridBagLayout());
        repaint();
        revalidate();
        setOpaque(false);
    }

    private CellWidget _cellWidget;

    void setCellWidget(CellWidget cellWidget){
        _cellWidget = cellWidget;
    }

    public CellWidget getCellWidget() {
        return _cellWidget;
    }

    private final ObjectInCell _object;

    public ObjectInCell getObject(){
        return _object;
    }

    public Dimension getDimension(){
        return new Dimension(80, 80);
    }

    public Layer getLayer(){
        return Layer.BOTTOM;
    }

    protected abstract File getImageFile();

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

    protected void getDamage(){
        destroy();
    }

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
