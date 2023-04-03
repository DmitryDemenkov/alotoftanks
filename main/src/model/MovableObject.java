package model;

public abstract class MovableObject extends ObjectInCell{

    /**
     * Направление, в котором движется объект
     */
    private Direction _direction;

    public Direction getDirection(){
        return _direction;
    }

    void setDirection(Direction direction){
        _direction = direction;
    }

    /**
     * Перемещение объекта в соседнюю ячейку в текущем направлении
     * @return true если побъект перемещен в новую ячейку
     */
    public boolean move(){
        Cell nextCell = getCell().getNeighbour(getDirection());
        if (nextCell == null){
            return false;
        }

        return nextCell.addObject(this);
    }
}
