package model.environment;

import model.Cell;
import model.Field;
import model.cellobjects.*;
import model.cellobjects.tank.Headquarters;
import model.cellobjects.tank.Tank;
import model.measures.Direction;
import model.measures.Position;
import model.measures.Size;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEnvironment extends Environment{

    private final List<Class<? extends Obstacle>> obstacles = List.of(
            FuelOilBarrel.class,
            Thicket.class,
            Wall.class,
            Water.class
    );

    private Obstacle getObstacle(int index){
        Obstacle obstacle = null;
        try {
            obstacle = obstacles.get(index).getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obstacle;
    }

    @Override
    public Size fieldSize() {
        return new Size(8, 8);
    }

    @Override
    public void fillField(Field field) {
        setTanksWithHeadquarters(field);

        for (int y = 0; y < fieldSize().height(); y++){
            for (int x = 0; x < fieldSize().width(); x++){
                int objectIndex = ThreadLocalRandom.current().nextInt(0, 6);
                Cell cell = field.getCell(new Position(x, y));
                if (objectIndex < obstacles.size() && cell.getObjects().isEmpty()){
                    field.getCell(new Position(x, y)).addObject(getObstacle(objectIndex));
                }
            }
        }
    }

    private void setTanksWithHeadquarters(Field field){
        Tank firstTank = new Tank(Direction.SOUTH, 3);
        Headquarters firstHeadquarters = new Headquarters();
        firstTank.setHeadquarters(firstHeadquarters);

        int firstHeadquartersPosition = ThreadLocalRandom.current().nextInt(0, fieldSize().width());
        field.getCell(new Position(firstHeadquartersPosition, 0)).addObject(firstHeadquarters);

        int firstTankPosition = ThreadLocalRandom.current().nextInt(0, fieldSize().width());
        field.getCell(new Position(firstTankPosition, 1)).addObject(firstTank);

        Tank secondTank = new Tank(Direction.NORTH, 3);
        Headquarters secondHeadquarters = new Headquarters();
        secondTank.setHeadquarters(secondHeadquarters);

        int secondHeadquartersPosition = ThreadLocalRandom.current().nextInt(0, fieldSize().width());
        field.getCell(new Position(secondHeadquartersPosition, fieldSize().height() - 1)).addObject(secondHeadquarters);

        int secondTankPosition = ThreadLocalRandom.current().nextInt(0, fieldSize().width());
        field.getCell(new Position(secondTankPosition, fieldSize().height() - 2)).addObject(secondTank);
    }
}
