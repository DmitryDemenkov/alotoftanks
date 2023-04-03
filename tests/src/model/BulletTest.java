package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BulletTest {

    Bullet bullet;
    Cell startCell;
    Cell targetCell;

    @BeforeEach
    public void createTestConfiguration(){
        startCell = new Cell(new Position(0, 0));
        targetCell = new Cell(new Position(0, 1));
        startCell.setNeighbour(targetCell);
        bullet = new Bullet(Direction.SOUTH, startCell);
    }

    @Test
    public void constructor_creationBullet(){
        bullet = new Bullet(Direction.NORTH, startCell);

        Assertions.assertSame(startCell, bullet.getCell());
        Assertions.assertEquals(Direction.NORTH, bullet.getDirection());
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = bullet.canFaceWith(tank);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithWall(){
        Obstacle wall = new Wall();

        boolean result = bullet.canFaceWith(wall);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithWater(){
        Obstacle water = new Water();

        boolean result = bullet.canFaceWith(water);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet otherBullet = new BulletForTest();

        boolean result = bullet.canFaceWith(otherBullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = bullet.canFaceWith(bullet);

        Assertions.assertFalse(result);
    }

    @Test
    public void faceWith_collisionWithTank(){
        ObjectInCellEvent[] actualEvents = {null};

        Tank tank = new Tank();

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                actualEvents[0] = event;
            }
        });

        bullet.faceWith(tank);

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void faceWith_collisionWithWall(){
        ObjectInCellEvent[] actualEvents = {null};

        Wall wall = new Wall();

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                actualEvents[0] = event;
            }
        });

        bullet.faceWith(wall);

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void faceWith_collisionWithWater(){
        ObjectInCellEvent[] actualEvents = {null};

        Water water = new Water();

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                actualEvents[0] = event;
            }
        });

        bullet.faceWith(water);

        Assertions.assertNull(actualEvents[0]);
    }

    @Test
    public void faceWith_collisionWithHeadquarters(){
        ObjectInCellEvent[] actualEvents = {null};

        Headquarters headquarters = new Headquarters();

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                actualEvents[0] = event;
            }
        });

        bullet.faceWith(headquarters);

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void move_movingInEmptyCell(){
        ObjectInCellEvent[] actualEvents = {null};

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                Assertions.assertSame(targetCell, bullet.getCell());
                actualEvents[0] = event;
            }
        });

        bullet.move();

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void move_movingInCellWithWall(){
        List<ObjectInCellEvent> events = new ArrayList<>();

        targetCell.addObject(new Wall());

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertSame(targetCell, bullet.getCell());
                events.add(event);
            }
        });

        bullet.move();

        List<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.DESTROYING));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for (int i = 0; i < expectedEvents.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
        }
    }

    @Test void update_moving(){
        ObjectInCellEvent[] actualEvents = {null};

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                Assertions.assertSame(targetCell, bullet.getCell());
                actualEvents[0] = event;
            }
        });

        bullet.update();

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void update_detonating(){
        ObjectInCellEvent[] actualEvents = {null};

        targetCell.addObject(new Wall());

        bullet.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(bullet, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                Assertions.assertSame(targetCell, bullet.getCell());
                actualEvents[0] = event;
            }
        });

        targetCell.addObject(bullet);
        bullet.update();

        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertNull(bullet.getCell());
    }
}
