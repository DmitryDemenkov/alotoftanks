package model;

import model.cellobjects.Wall;
import model.measures.Direction;
import model.measures.Position;
import model.testprefabs.BulletForTest;
import model.testprefabs.TankForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CellTest {

    private Cell cell;

    @BeforeEach
    public void testConfiguration(){
        cell = new Cell(new Position(0, 0));
    }

    @Test
    public void constructor_cellCreation(){
        cell = new Cell(new Position(1,2));

        Assertions.assertEquals(1, cell.getPosition().getX());
        Assertions.assertEquals(2, cell.getPosition().getY());
        Assertions.assertEquals(0, cell.getObjects().size());
        Assertions.assertNull(cell.getNeighbour(Direction.NORTH));
        Assertions.assertNull(cell.getNeighbour(Direction.SOUTH));
        Assertions.assertNull(cell.getNeighbour(Direction.EAST));
        Assertions.assertNull(cell.getNeighbour(Direction.WEST));
    }

    @Test
    public void addObject_addingInEmptyCell(){
        ObjectInCell object = new TankForTest();

        boolean result = cell.addObject(object);

        Assertions.assertTrue(result);
        Assertions.assertTrue(cell.getObjects().contains(object));
        Assertions.assertSame(cell, object.getCell());
    }

    @Test
    public void addObject_addingInCellContainsObjectWithRightCollision(){
        ObjectInCell tank = new TankForTest();
        ObjectInCell bullet = new BulletForTest();

        cell.addObject(tank);
        boolean result = cell.addObject(bullet);

        Assertions.assertTrue(result);
        Assertions.assertTrue(cell.getObjects().contains(tank));
        Assertions.assertTrue(cell.getObjects().contains(bullet));
        Assertions.assertSame(cell, tank.getCell());
        Assertions.assertSame(cell, bullet.getCell());
    }

    @Test
    public void addObject_addingInCellContainsObjectWithWrongCollision(){
        ObjectInCell tank = new TankForTest();
        ObjectInCell wall = new Wall();

        cell.addObject(tank);
        boolean result = cell.addObject(wall);

        Assertions.assertFalse(result);
        Assertions.assertTrue(cell.getObjects().contains(tank));
        Assertions.assertFalse(cell.getObjects().contains(wall));
        Assertions.assertSame(cell, tank.getCell());
        Assertions.assertNotSame(cell, wall.getCell());
    }

    @Test
    public void addObject_addingInCellContainsSameObject(){
        ObjectInCell tank = new TankForTest();

        cell.addObject(tank);
        boolean result = cell.addObject(tank);

        Assertions.assertFalse(result);
        Assertions.assertTrue(cell.getObjects().contains(tank));
        Assertions.assertSame(cell, tank.getCell());
        Assertions.assertEquals(1, cell.getObjects().size());
    }

    @Test
    public void takeObject_takingFromCellContainsThisObject(){
        ObjectInCell object = new TankForTest();
        cell.addObject(object);

        ObjectInCell actualObject = cell.takeObject(object);

        Assertions.assertSame(object, actualObject);
        Assertions.assertFalse(cell.getObjects().contains(object));
        Assertions.assertEquals(0, cell.getObjects().size());
        Assertions.assertNull(object.getCell());
    }

    @Test
    public void takeObject_takingFromEmptyCell(){
        ObjectInCell object = new TankForTest();

        ObjectInCell actualObject = cell.takeObject(object);

        Assertions.assertNull(actualObject);
        Assertions.assertFalse(cell.getObjects().contains(object));
        Assertions.assertEquals(0, cell.getObjects().size());
    }

    @Test
    public void takeObject_takingFromCellNotContainsThisObject(){
        ObjectInCell tank = new TankForTest();
        ObjectInCell wall = new Wall();

        cell.addObject(tank);

        ObjectInCell actualObject = cell.takeObject(wall);

        Assertions.assertNull(actualObject);
        Assertions.assertTrue(cell.getObjects().contains(tank));
        Assertions.assertEquals(1, cell.getObjects().size());
        Assertions.assertNull(wall.getCell());
    }

    @Test
    public void setNeighbour_northNeighbour(){
        Cell neighbour = new Cell(new Position(0, -1));

        cell.setNeighbour(neighbour);

        Assertions.assertSame(neighbour, cell.getNeighbour(Direction.NORTH));
        Assertions.assertSame(cell, neighbour.getNeighbour(Direction.SOUTH));
    }

    @Test
    public void setNeighbour_southNeighbour(){
        Cell neighbour = new Cell(new Position(0, 1));

        cell.setNeighbour(neighbour);

        Assertions.assertSame(neighbour, cell.getNeighbour(Direction.SOUTH));
        Assertions.assertSame(cell, neighbour.getNeighbour(Direction.NORTH));
    }

    @Test
    public void setNeighbour_eastNeighbour(){
        Cell neighbour = new Cell(new Position(1, 0));

        cell.setNeighbour(neighbour);

        Assertions.assertSame(neighbour, cell.getNeighbour(Direction.EAST));
        Assertions.assertSame(cell, neighbour.getNeighbour(Direction.WEST));
    }

    @Test
    public void setNeighbour_westNeighbour(){
        Cell neighbour = new Cell(new Position(-1, 0));

        cell.setNeighbour(neighbour);

        Assertions.assertSame(neighbour, cell.getNeighbour(Direction.WEST));
        Assertions.assertSame(cell, neighbour.getNeighbour(Direction.EAST));
    }

    @Test
    public void setNeighbour_FourNeighbours(){
        Cell northNeighbour = new Cell(new Position(0, -1));
        Cell southNeighbour = new Cell(new Position(0, 1));
        Cell eastNeighbour = new Cell(new Position(1, 0));
        Cell westNeighbour = new Cell(new Position(-1, 0));

        cell.setNeighbour(northNeighbour);
        cell.setNeighbour(southNeighbour);
        cell.setNeighbour(eastNeighbour);
        cell.setNeighbour(westNeighbour);

        Assertions.assertSame(northNeighbour, cell.getNeighbour(Direction.NORTH));
        Assertions.assertSame(southNeighbour, cell.getNeighbour(Direction.SOUTH));
        Assertions.assertSame(eastNeighbour, cell.getNeighbour(Direction.EAST));
        Assertions.assertSame(westNeighbour, cell.getNeighbour(Direction.WEST));
    }

    @Test
    public void setNeighbour_setSameCellAsNeighbour(){
        Throwable exception = Assertions.assertThrows(RuntimeException.class,
                () -> cell.setNeighbour(cell));

        Assertions.assertEquals("Illegal neighbour", exception.getMessage());
    }

    @Test
    public void setNeighbour_setIllegalNeighbour(){
        Cell neighbour = new Cell(new Position(1, 1));

        Throwable exception = Assertions.assertThrows(RuntimeException.class,
                () -> cell.setNeighbour(neighbour));

        Assertions.assertEquals("Illegal neighbour", exception.getMessage());
    }
}
