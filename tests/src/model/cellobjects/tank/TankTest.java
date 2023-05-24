package model.cellobjects.tank;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.Cell;
import model.Field;
import model.cellobjects.damaging.Bullet;
import model.cellobjects.Obstacle;
import model.cellobjects.Thicket;
import model.cellobjects.Wall;
import model.environment.Environment;
import model.measures.Direction;
import model.measures.Position;
import model.measures.Size;
import model.testprefabs.BulletForTest;
import model.testprefabs.TankForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TankTest {

    private Tank tank;

    protected Field field;

    private class TankListener implements IObjectInCellEventListener {
        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    @BeforeEach
    public void testConfiguration(){
        field = new Field(new Environment() {
            @Override
            public Size fieldSize() {
                return new Size(1, 2);
            }

            @Override
            public void fillField(Field field) {

            }
        });

        tank = new Tank(Direction.NORTH, 3);
    }

    @Test
    public void constructor_tankCreation(){
        tank = new Tank(Direction.NORTH, 3);

        Assertions.assertNull(tank.getCell());
        Assertions.assertNull(tank.getHeadquarters());
        Assertions.assertEquals(Direction.NORTH, tank.getDirection());
        Assertions.assertEquals(3, tank.getHealth());
    }

    @Test
    public void canFaceWith_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = tank.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = tank.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = tank.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank otherTank = new TankForTest();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tank.faceWith(otherTank));
    }

    @Test
    public void canFaceWith_collisionWithThicket(){
        Thicket thicket = new Thicket();

        boolean result = tank.canFaceWith(thicket);

        Assertions.assertTrue(result);
    }

    @Test
    public void faceWith_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tank.faceWith(wall));
    }

    @Test
    public void faceWith_collisionWithBullet() {
        Bullet bullet = new BulletForTest();

        tank.addListener(new TankListener());

        tank.faceWith(bullet);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void rotate_rotateTank(){
        tank.addListener(new TankListener());

        tank.rotate(Direction.SOUTH);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.MOVED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }

        Assertions.assertEquals(Direction.SOUTH, tank.getDirection());
    }

    @Test
    public void rotate_rotateTankOnSameDirection(){
        tank.addListener(new TankListener());

        tank.rotate(Direction.NORTH);

        Assertions.assertEquals(Direction.NORTH, tank.getDirection());
        Assertions.assertEquals(0, events.size());
    }

    @Test
    public void move_movingInEmptyCell(){
        Cell startCell = field.getCell(new Position(0, 1));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new TankListener());

        boolean result = tank.move();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.MOVED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertTrue(result);
    }

    @Test
    public void move_movingInNotEmptyCell(){
        Cell startCell = field.getCell(new Position(0, 1));
        Cell targetCell = field.getCell(new Position(0, 0));
        Wall wall = new Wall();

        startCell.addObject(tank);
        targetCell.addObject(wall);
        tank.rotate(Direction.NORTH);

        tank.addListener(new TankListener());

        boolean result = tank.move();

        Assertions.assertFalse(result);
        Assertions.assertSame(startCell, tank.getCell());
        Assertions.assertEquals(0, events.size());
    }

    @Test
    public void move_noCellInMovingDirection(){
        Cell startCell = new Cell(new Position(0, 0));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new TankListener());

        boolean result = tank.move();

        Assertions.assertFalse(result);
        Assertions.assertSame(startCell, tank.getCell());
        Assertions.assertEquals(0, events.size());
    }

    @Test
    public void shoot_shootInEmptyCell(){
        Cell startCell = field.getCell(new Position(0, 1));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new TankListener());

        boolean result = tank.shoot();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(null, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(Bullet.class, events.get(i).getObject().getClass());
        }

        Assertions.assertTrue(result);
    }

    @Test
    public void shoot_noCellInShootingDirection(){
        Cell startCell = new Cell(new Position(0, 0));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new TankListener());

        boolean result = tank.shoot();

        Assertions.assertFalse(result);
        Assertions.assertEquals(0, events.size());
    }

    @Test
    public void pass_skipStep(){
        tank.addListener(new TankListener());

        tank.pass();

        Assertions.assertEquals(0, events.size());
    }

    @Test
    public void update_getDamage() {
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();

        tank.addListener(new TankListener());

        int expectedHealth = tank.getHealth() - 1;

        cell.addObject(tank);
        cell.addObject(bullet);
        tank.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.DAMAGED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertEquals(expectedHealth, tank.getHealth());
    }
}
