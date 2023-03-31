package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WaterTest {

    private Water water;

    @BeforeEach
    public void testConfiguration(){
        water = new Water();
    }

    @Test
    public void canHaveCollision_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = water.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = water.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithSameObject(){
        boolean result = water.canFaceWith(water);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = water.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void onCollision_collisionWithTank(){
        Tank tank = new Tank();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                water.faceWith(tank));
    }

    @Test
    public void onCollision_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                water.faceWith(wall));
    }

    @Test
    public void onCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        water.addListener(new IObjectInCellEventListener() {
            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                actualEvents[0] = event;
            }
        });

        water.faceWith(bullet);

        Assertions.assertNull(actualEvents[0]);
    }
}
