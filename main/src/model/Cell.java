package model;

import java.util.*;

/**
 * Фрагмент поля, который может содержать в себе объекты
 */
public class Cell {

    private final Position _position;

    public Position getPosition(){
        return _position;
    }

    public Cell(Position position){
        _position = position;
    }

    /* -------------- Объекты в ячейке ---------------- */

    private final Set<ObjectInCell> _objects = new HashSet<>();

    public boolean addObject(ObjectInCell newObject){
        if (!canAdd(newObject)){
            return false;
        }

        _objects.add(newObject);
        newObject.setCell(this);
        faceObjectsWith(newObject);

        return true;
    }

    private boolean canAdd(ObjectInCell newObject){
        return _objects.stream().allMatch(object -> object.canFaceWith(newObject));
    }

    private void faceObjectsWith(ObjectInCell newObject){
        _objects.forEach(object -> {
            if (object != newObject){
                object.faceWith(newObject);
                newObject.faceWith(object);
            }
        });
    }

    public List<ObjectInCell> getObjects(){
        return _objects.stream().toList();
    }

    public ObjectInCell takeObject(ObjectInCell object){
        ObjectInCell removedObject = null;

        boolean removed = _objects.remove(object);
        if (removed){
            object.unsetCell();
            removedObject = object;
        }

        return removedObject;
    }

    /* ------------------ Соседи ---------------- */

    private final Set<Cell> _neighbours = new HashSet<>();

    public Cell getNeighbour(Direction direction){
        Position neighbourPosition = getPosition().shift(direction, 1);

        return _neighbours.stream().
                filter(n -> n.getPosition().equals(neighbourPosition)).findAny().orElse(null);
    }

    void setNeighbour(Cell newNeighbour){
        if (!canBeNeighbours(newNeighbour)){
            return;
        }

        _neighbours.add(newNeighbour);
        newNeighbour.setNeighbour(this);
    }

    private boolean canBeNeighbours(Cell newNeighbour){
        if (_neighbours.stream().anyMatch(n -> n.getPosition().equals(newNeighbour.getPosition()))){
            return false;
        }

        int deltaX = Math.abs(getPosition().getX() - newNeighbour.getPosition().getX());
        int deltaY = Math.abs(getPosition().getY() - newNeighbour.getPosition().getY());

        int deltaValid = 1;
        boolean isDeltaXValid = deltaX == deltaValid;
        boolean isDeltaYValid = deltaY == deltaValid;

        boolean isNeighbourValid = !isDeltaXValid && isDeltaYValid || isDeltaXValid && !isDeltaYValid;
        if (!isNeighbourValid){
            throw new RuntimeException("Illegal neighbour");
        }

        return true;
    }
}
