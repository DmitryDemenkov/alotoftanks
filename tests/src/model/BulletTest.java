package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BulletTest {

    Bullet bullet;
    Cell startCell;
    Cell targetCell;

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    private class BulletListener implements IObjectInCellEventListener{

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    @BeforeEach
    public void createTestConfiguration(){
        startCell = new Cell(new Position(0, 0));
        targetCell = new Cell(new Position(0, 1));
        startCell.setNeighbour(targetCell);
        bullet = new Bullet(Direction.SOUTH, startCell);
        events.clear();
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
        Tank tank = new Tank();

        bullet.addListener(new BulletListener());

        bullet.faceWith(tank);

        Assertions.assertTrue(bullet.isDetonating());
    }

    @Test
    public void faceWith_collisionWithWall(){
        Wall wall = new Wall();

        bullet.addListener(new BulletListener());

        bullet.faceWith(wall);

        Assertions.assertTrue(bullet.isDetonating());
    }

    @Test
    public void faceWith_collisionWithWater(){
        Water water = new Water();

        bullet.addListener(new BulletListener());

        bullet.faceWith(water);

        Assertions.assertFalse(bullet.isDetonating());
    }

    @Test
    public void faceWith_collisionWithHeadquarters(){
        Headquarters headquarters = new Headquarters();

        bullet.addListener(new BulletListener());

        bullet.faceWith(headquarters);

        Assertions.assertTrue(bullet.isDetonating());
    }

    @Test
    public void move_movingInEmptyCell(){
        bullet.addListener(new BulletListener());

        bullet.move();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.MOVED));
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void move_movingInCellWithWall(){
        targetCell.addObject(new Wall());

        bullet.addListener(new BulletListener());

        bullet.move();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.MOVED));
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertTrue(bullet.isDetonating());
    }

    @Test void update_moving(){
        bullet.addListener(new BulletListener());

        bullet.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.MOVED));
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void update_detonating(){
        targetCell.addObject(new Wall());

        bullet.addListener(new BulletListener());

        targetCell.addObject(bullet);
        bullet.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(bullet, ObjectInCellEvent.EventType.DESTROYED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertNull(bullet.getCell());
    }
}
