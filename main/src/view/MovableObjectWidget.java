package view;

import model.Direction;
import model.MovableObject;
import view.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class MovableObjectWidget extends ObjectInCellWidget{

    public MovableObjectWidget(MovableObject object) {
        super(object);
    }

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

    private BufferedImage getImageByDirection(Direction direction){
        Map<Direction, Double> directionToAngles = new HashMap<>();
        directionToAngles.put(Direction.NORTH, 0.0);
        directionToAngles.put(Direction.SOUTH, 180.0);
        directionToAngles.put(Direction.WEST, 270.0);
        directionToAngles.put(Direction.EAST, 90.0);

        BufferedImage image = super.getImage();
        image = ImageUtils.rotateImage(image, directionToAngles.get(direction));
        return image;
    }
}
