package model.cellobjects;

import model.Cell;
import model.ObjectInCell;
import model.measures.Direction;
import model.properties.ObjectKeeper;

public abstract class MovableObject extends ObjectInCell {

    /**
     * Направление, в котором движется объект
     */
    private Direction _direction;

    public Direction getDirection(){
        return _direction;
    }

    protected void setDirection(Direction direction){
        _direction = direction;
    }

    /**
     * Перемещение объекта в соседнюю ячейку в текущем направлении
     * @return true если побъект перемещен в новую ячейку
     */
    protected boolean move(){
        Cell nextCell = getCell().getNeighbour(getDirection());
        return moveAt(nextCell);
    }

    protected boolean moveAt(Cell newCell){
        if (newCell == null){
            return false;
        }
        return  newCell.addObject(this);
    }

    @Override
    protected void setParent(ObjectKeeper<? extends ObjectInCell> parent){
        super.setParent(parent);
    }
}
