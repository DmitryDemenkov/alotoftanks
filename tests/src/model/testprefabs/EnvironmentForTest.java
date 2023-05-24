package model.testprefabs;

import model.*;
import model.cellobjects.FuelOilBarrel;
import model.cellobjects.tank.Headquarters;
import model.cellobjects.tank.Tank;
import model.environment.Environment;
import model.measures.Direction;
import model.measures.Position;
import model.measures.Size;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentForTest extends Environment {

    private final Map<Position, ObjectInCell> _objectPositions = new HashMap<>();

    public EnvironmentForTest(){
        Tank tank1 = new Tank(Direction.NORTH, 3);
        Tank tank2 = new Tank(Direction.NORTH, 1);

        Headquarters headquarters1 = new Headquarters();
        Headquarters headquarters2 = new Headquarters();

        tank1.setHeadquarters(headquarters1);
        tank2.setHeadquarters(headquarters2);

        _objectPositions.put(new Position(0, 0), tank1);
        _objectPositions.put(new Position(2, 2), tank2);
        _objectPositions.put(new Position(0, 2), headquarters1);
        _objectPositions.put(new Position(2, 0), headquarters2);
        _objectPositions.put(new Position(1, 2), new FuelOilBarrel());
    }

    @Override
    public Size fieldSize() {
        return new Size(3, 3);
    }

    @Override
    public void fillField(Field field) {
        for (Map.Entry<Position, ObjectInCell> objectPosition: _objectPositions.entrySet()){
            field.getCell(objectPosition.getKey()).addObject(objectPosition.getValue());
        }
    }
}
