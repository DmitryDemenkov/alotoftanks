package model;

import events.IObjectInCellEventListener;
import model.cellobjects.tank.Tank;
import model.environment.Environment;
import model.measures.Direction;
import model.measures.Position;
import model.measures.Size;

import java.util.ArrayList;

/**
 * Игровое поле, состоящее из ячеек
 */
public class Field {

    private final Size _size;

    public Size getSize(){
        return _size;
    }

    public Field(Environment environment){
        _size = environment.fieldSize();
        createCells(environment.fieldSize().width(), environment.fieldSize().height());
        environment.fillField(this);
    }

    /* -------------------- Ячейки ------------------- */

    /**
     * Ячейки из которых состоит поле
     */
    private final ArrayList<Cell> _cells = new ArrayList<>();

    /**
     * Получить ячейку по заданной позиции
     * @param position позиция ячейки
     * @return ячейка с заданной позиции, null если такой ячейки нет
     */
    public Cell getCell(Position position){
        return _cells.stream().
                filter(cell -> cell.getPosition().equals(position)).findAny().orElse(null);
    }

    /**
     * Создание ячеек и формирование из них поля
     * @param width длина поля
     * @param height высота поля
     */
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

    /**
     * Задать объектам на поле указанного слушателя
     * @param listener слушатель объектов на поле
     */
    void addObjectInCellListener(IObjectInCellEventListener listener){
        for (Cell cell : _cells){
            for (ObjectInCell object : cell.getObjects()){
                object.addListener(listener);
            }
        }
    }

    /**
     * Получить все танк, находящиеся на поле
     * @return список танков
     */
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
