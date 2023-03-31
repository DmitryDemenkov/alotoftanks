package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HeadquartersTest {

    private Headquarters headquarters;

    @BeforeEach
    public void testConfiguration(){
        headquarters = new Headquarters();
    }

    @Test
    public void constructor_headquartersCreation(){
        headquarters = new Headquarters();

        Assertions.assertNull(headquarters.getCell());
        Assertions.assertNull(headquarters.getTank());
    }

    @Test
    public void canHaveCollision_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = headquarters.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = headquarters.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithSameObject(){
        boolean result = headquarters.canFaceWith(headquarters);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = headquarters.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void onCollision_collisionWithTank(){
        Tank tank = new Tank();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                headquarters.faceWith(tank));
    }

    @Test
    public void onCollision_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                headquarters.faceWith(wall));
    }

    @Test
    public void onCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        headquarters.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(headquarters, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(headquarters, ObjectInCellEvent.EventType.DESTROYING);

        headquarters.faceWith(bullet);

        Assertions.assertNull(headquarters.getCell());
        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }

    @Test
    public void update_destroying(){
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        headquarters.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(headquarters, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DESTROYING, event.getType());
                Assertions.assertSame(cell, headquarters.getCell());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(headquarters, ObjectInCellEvent.EventType.DESTROYING);

        cell.addObject(headquarters);
        cell.addObject(bullet);
        headquarters.update();

        Assertions.assertNull(headquarters.getCell());
        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }

    @Test
    public void setTank_addingTank(){
        Tank tank = new Tank();

        headquarters.setTank(tank);

        Assertions.assertSame(tank, headquarters.getTank());
    }
}
