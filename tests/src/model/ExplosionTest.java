package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.measures.Position;
import model.testprefabs.TankForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ExplosionTest {

    private Explosion explosion;

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    private class ObjectListener implements IObjectInCellEventListener {

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    @BeforeEach
    public void createTestConfiguration(){
        explosion = new Explosion();
        events.clear();
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new TankForTest();

        boolean result = explosion.canFaceWith(tank);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithWall(){
        Obstacle wall = new Wall();

        boolean result = explosion.canFaceWith(wall);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithWater(){
        Obstacle water = new Water();

        boolean result = explosion.canFaceWith(water);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Explosion otherExplosion = new Explosion();

        boolean result = explosion.canFaceWith(otherExplosion);

        Assertions.assertTrue(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = explosion.canFaceWith(explosion);

        Assertions.assertFalse(result);
    }

    @Test
    public void setCell_addingToCell(){
        Cell cell = new Cell(new Position(0, 0));
        explosion.addListener(new ObjectListener());

        explosion.setCell(cell);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertFalse(explosion.isDestroying());
    }

    @Test
    public void update_addingToCell(){
        Cell cell = new Cell(new Position(0, 0));
        explosion.addListener(new ObjectListener());

        cell.addObject(explosion);
        explosion.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.DAMAGED));
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertTrue(explosion.isDestroying());
        Assertions.assertEquals(cell, explosion.getCell());
    }

    @Test
    public void update_destroying(){
        Cell cell = new Cell(new Position(0, 0));
        explosion.addListener(new ObjectListener());

        cell.addObject(explosion);
        explosion.update();
        explosion.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.DAMAGED));
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(explosion, ObjectInCellEvent.EventType.DESTROYED));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
        Assertions.assertNull(explosion.getCell());
    }

    @Test
    public void faceWith_destroyingWall(){
        Wall wall = new Wall();
        wall.addListener(new ObjectListener());

        wall.faceWith(explosion);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(wall, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void update_destroyingWall(){
        Cell cell = new Cell(new Position(0, 0));
        Wall wall = new Wall();
        wall.addListener(new ObjectListener());

        cell.addObject(wall);
        cell.addObject(explosion);
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
