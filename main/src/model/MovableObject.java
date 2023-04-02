package model;

public abstract class MovableObject extends ObjectInCell{

    private Direction _direction;

    public Direction getDirection(){
        return _direction;
    }

    void setDirection(Direction direction){
        _direction = direction;
    }

    public boolean move(){
        Cell nextCell = getCell().getNeighbour(getDirection());
        if (nextCell == null){
            return false;
        }

        return nextCell.addObject(this);
    }
}
