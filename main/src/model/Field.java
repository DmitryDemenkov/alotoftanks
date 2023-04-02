package model;

import events.IObjectInCellEventListener;
import model.environment.Environment;

import java.util.ArrayList;

/**
 * Игровое поле, состоящее из ячеек
 */
public class Field {

    public Field(Environment environment){
        createCells(environment.fieldSize().width(), environment.fieldSize().height());
        environment.fillField(this);
    }

    /* -------------------- Ячейки ------------------- */

    private final ArrayList<Cell> _cells = new ArrayList<>();

    public Cell getCell(Position position){
        return _cells.stream().
                filter(cell -> cell.getPosition().equals(position)).findAny().orElse(null);
    }

    private void createCells(int width, int height){
        for (int yCoordinate = 0; yCoordinate < height; yCoordinate++){
            for (int xCoordinate = 0; xCoordinate < width; xCoordinate++){
                Cell newCell = new Cell(new Position(xCoordinate, yCoordinate));

                Cell northNeighbour = getCell(newCell.getPosition().shift(Direction.NORTH, 1));
                Cell westNeighbour = getCell(newCell.getPosition().shift(Direction.WEST, 1));

                if (northNeighbour != null){
                    newCell.setNeighbour(northNeighbour);
                }

                if (westNeighbour != null){
                    newCell.setNeighbour(westNeighbour);
                }

                _cells.add(newCell);
            }
        }
    }

    /* ----------------- Объекты на поле --------------------- */

    void addObjectInCellListener(IObjectInCellEventListener listener){
        for (Cell cell : _cells){
            for (ObjectInCell object : cell.getObjects()){
                if (!(object instanceof Tank)){
                    object.addListener(listener);
                }
            }
        }
    }

    public ArrayList<Tank> getTanks(){
        ArrayList<Tank> tanks = new ArrayList<>();
        for (Cell cell : _cells){
            Tank tank = (Tank)cell.getObjects().stream().filter(o -> o instanceof Tank).findAny().orElse(null);
            if (tank != null){
                tanks.add(tank);
            }
        }
        return tanks;
    }
}
