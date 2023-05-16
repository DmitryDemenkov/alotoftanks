package model.environment;

import model.*;
import model.measures.Direction;
import model.measures.Position;
import model.measures.Size;

import java.util.ArrayList;
import java.util.List;

public class SimpleEnvironment extends Environment{

    private List<Position> wallsPositions(){
        ArrayList<Position> positions = new ArrayList<>();

        positions.add(new Position(1, 0));
        positions.add(new Position(3, 0));

        positions.add(new Position(1, 1));
        positions.add(new Position(2, 1));
        positions.add(new Position(3, 1));

        positions.add(new Position(5, 2));

        positions.add(new Position(0, 3));
        positions.add(new Position(1, 3));
        positions.add(new Position(2, 3));
        positions.add(new Position(5, 3));

        positions.add(new Position(4, 6));
        positions.add(new Position(5, 6));
        positions.add(new Position(6, 6));

        positions.add(new Position(4, 7));
        positions.add(new Position(6, 7));

        return positions;
    }

    private List<Position> waterPositions(){
        ArrayList<Position> positions = new ArrayList<>();

        positions.add(new Position(7, 0));

        positions.add(new Position(6, 1));
        positions.add(new Position(7, 1));

        positions.add(new Position(3, 4));
        positions.add(new Position(4, 4));

        positions.add(new Position(1, 5));

        positions.add(new Position(0, 6));

        return positions;
    }

    @Override
    public Size fieldSize() {
        return new Size(8, 8);
    }

    @Override
    public void fillField(Field field) {

        Tank tank1 = new Tank(Direction.SOUTH, 3);
        Headquarters headquarters1 = new Headquarters();
        tank1.setHeadquarters(headquarters1);

        Tank tank2 = new Tank(Direction.NORTH, 3);
        Headquarters headquarters2 = new Headquarters();
        tank2.setHeadquarters(headquarters2);

        field.getCell(new Position(6, 0)).addObject(tank1);
        field.getCell(new Position(2, 0)).addObject(headquarters1);
        field.getCell(new Position(2, 7)).addObject(tank2);
        field.getCell(new Position(5, 7)).addObject(headquarters2);

        for (Position position : wallsPositions()){
            field.getCell(position).addObject(new Wall());
        }

        for (Position position : waterPositions()){
            field.getCell(position).addObject(new Water());
        }
    }
}
