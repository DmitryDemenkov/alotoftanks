package view;

import model.measures.Direction;
import model.cellobjects.MovableObject;
import view.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Представление движущегося объекта
 */
public abstract class MovableObjectWidget extends ObjectInCellWidget{

    public MovableObjectWidget(MovableObject object) {
        super(object);
    }

    /**
     * Переместить виджет в указанный виджет ячейки
     * @param cellWidget виджет ячейки, куда нужно переместиься
     */
    protected void moveTo(CellWidget cellWidget){
        if (getCellWidget() != null) {
            getCellWidget().setActive(false);
            getCellWidget().removeObjectWidget(this);
        }
        if (cellWidget != null) {
            cellWidget.addObjectWidget(this);
        }
    }

    @Override
    protected BufferedImage getImage() {
        return getImageByDirection(((MovableObject)getObject()).getDirection());
    }

    private static final Map<Direction, Double> directionToAngles = Map.ofEntries(
            Map.entry(Direction.NORTH, 0.0),
            Map.entry(Direction.SOUTH, 180.0),
            Map.entry(Direction.WEST, 270.0),
            Map.entry(Direction.EAST, 90.0)
    );

    /**
     * Получить изображение объект, соответсвующее направлению движения
     * @param direction еаправление движения объекта
     * @return изображение объекта
     */
    private BufferedImage getImageByDirection(Direction direction){
        BufferedImage image = super.getImage();
        image = ImageUtils.rotateImage(image, directionToAngles.get(direction));
        return image;
    }
}
