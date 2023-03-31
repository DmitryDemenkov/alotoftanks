package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WallTest {

    private Wall wall;

    @BeforeEach
    public void testConfiguration(){
        wall = new Wall();
    }

    @Test
    public void canHaveCollision_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = wall.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = wall.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithSameObject(){
        boolean result = wall.canFaceWith(wall);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = wall.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void onCollision_collisionWithTank(){
        Tank tank = new Tank();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                wall.faceWith(tank));
    }

    @Test
    public void onCollision_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                wall.faceWith(wall));
    }

    @Test
    public void onCollision_collisionWithBullet() {
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        wall.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(wall, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.DESTROYING);

        wall.faceWith(bullet);

        Assertions.assertNull(wall.getCell());
        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }

    @Test
    public void update_destroying() {
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        wall.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(wall, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                Assertions.assertSame(cell, wall.getCell());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.DESTROYING);

        cell.addObject(wall);
        cell.addObject(bullet);
        wall.update();

        Assertions.assertNull(wall.getCell());
        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }
}
