package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ThicketTest {

    private class ThicketListener implements IObjectInCellEventListener {
        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    private Thicket _thicket;

    @BeforeEach
    public void testConfiguration(){
        _thicket = new Thicket();
        _thicket.addListener(new ThicketListener());
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new Tank();

        boolean result = _thicket.canFaceWith(tank);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = _thicket.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = _thicket.canFaceWith(_thicket);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = _thicket.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_thicketWithTank(){
        Tank tank = new Tank();
        Tank anotherTank = new Tank();
        Cell cell = new Cell(new Position(0, 0));

        cell.addObject(_thicket);
        cell.addObject(tank);
        _thicket.update();

        boolean result = _thicket.canFaceWith(anotherTank);

        Assertions.assertFalse(result);
    }

    @Test
    public void faceWith_collisionWithTank(){
        Tank tank = new Tank();

        _thicket.faceWith(tank);

        Assertions.assertEquals(tank, _thicket.getTank());
    }

    @Test
    public void faceWith_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                _thicket.faceWith(wall));
    }

    @Test
    public void faceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        _thicket.addListener(event -> actualEvents[0] = event);

        _thicket.faceWith(bullet);

        Assertions.assertNull(actualEvents[0]);
    }

    @Test
    public void faceWith_addingTankInCellWithThicket(){
        Tank tank = new Tank();
        Cell cell = new Cell(new Position(0, 0));

        cell.addObject(_thicket);
        cell.addObject(tank);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(_thicket, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertEquals(tank, _thicket.getTank());

    }

    @Test
    public void update_addingTankInCellWithThicket(){
        Tank tank = new Tank();
        Cell cell = new Cell(new Position(0, 0));

        cell.addObject(_thicket);
        cell.addObject(tank);
        _thicket.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(_thicket, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertEquals(tank, _thicket.getTank());
        Assertions.assertFalse(cell.getObjects().contains(tank));
    }

    @Test
    public void saveTankFromBullet(){
        Tank tank = new Tank();
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();

        cell.addObject(_thicket);
        cell.addObject(tank);
        _thicket.update();
        cell.addObject(bullet);

        Assertions.assertFalse(bullet.isDestroying());
    }
}
