package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.measures.Position;
import model.testprefabs.BulletForTest;
import model.testprefabs.TankForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class WallTest {

    private Wall wall;

    private class WallObserver implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    @BeforeEach
    public void testConfiguration(){
        wall = new Wall();
        events.clear();
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new TankForTest();

        boolean result = wall.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = wall.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = wall.canFaceWith(wall);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = wall.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void faceWith_collisionWithTank(){
        Tank tank = new TankForTest();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                wall.faceWith(tank));
    }

    @Test
    public void faceWith_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                wall.faceWith(wall));
    }

    @Test
    public void faceWith_collisionWithBullet() {
        Bullet bullet = new BulletForTest();

        wall.addListener(new WallObserver());

        wall.faceWith(bullet);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void update_destroying() {
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();

        wall.addListener(new WallObserver());

        cell.addObject(wall);
        cell.addObject(bullet);
        wall.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.DESTROYED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertNull(wall.getCell());
    }
}
