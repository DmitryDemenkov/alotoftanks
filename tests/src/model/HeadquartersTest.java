package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class HeadquartersTest {

    private Headquarters headquarters;

    private class HeadquartersListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    @BeforeEach
    public void testConfiguration(){
        headquarters = new Headquarters();
        events.clear();
    }

    @Test
    public void constructor_headquartersCreation(){
        headquarters = new Headquarters();

        Assertions.assertNull(headquarters.getCell());
        Assertions.assertNull(headquarters.getTank());
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = headquarters.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = headquarters.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = headquarters.canFaceWith(headquarters);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = headquarters.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void faceWith_collisionWithTank(){
        Tank tank = new Tank();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                headquarters.faceWith(tank));
    }

    @Test
    public void faceWith_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                headquarters.faceWith(wall));
    }

    @Test
    public void faceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        headquarters.addListener(new HeadquartersListener());

        headquarters.faceWith(bullet);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(headquarters, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void update_destroying(){
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();

        headquarters.addListener(new HeadquartersListener());

        cell.addObject(headquarters);
        cell.addObject(bullet);
        headquarters.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(headquarters, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(headquarters, ObjectInCellEvent.EventType.DESTROYED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertNull(headquarters.getCell());
    }

    @Test
    public void setTank_addingTank(){
        Tank tank = new Tank();

        headquarters.setTank(tank);

        Assertions.assertSame(tank, headquarters.getTank());
    }
}
