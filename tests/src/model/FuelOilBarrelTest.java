package model;

import events.IObjectInCellEventListener;
import events.ObjectInCellEvent;
import model.measures.Direction;
import model.measures.Position;
import model.testprefabs.BulletForTest;
import model.testprefabs.TankForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FuelOilBarrelTest {

    private FuelOilBarrel barrel;

    private class ObjectObserver implements IObjectInCellEventListener {

        @Override
        public void onObjectInCellAction(ObjectInCellEvent event) {
            events.add(event);
        }
    }

    private final ArrayList<ObjectInCellEvent> events = new ArrayList<>();

    @BeforeEach
    public void testConfiguration(){
        barrel = new FuelOilBarrel();
        events.clear();
    }

    @Test
    public void canFaceWith_collisionWithTank(){
        Tank tank = new TankForTest();

        boolean result = barrel.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = barrel.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithSameObject(){
        boolean result = barrel.canFaceWith(barrel);

        Assertions.assertFalse(result);
    }

    @Test
    public void canFaceWith_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = barrel.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void faceWith_collisionWithTank(){
        Tank tank = new TankForTest();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                barrel.faceWith(tank));
    }

    @Test
    public void faceWith_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                barrel.faceWith(wall));
    }

    @Test
    public void faceWith_collisionWithBullet() {
        Bullet bullet = new BulletForTest();

        barrel.addListener(new ObjectObserver());

        barrel.faceWith(bullet);

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(barrel, ObjectInCellEvent.EventType.NEED_UPDATE));

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
            Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
        }
    }

    @Test
    public void update_detonating(){
        Cell cell = new Cell(new Position(0, 0));
        cell.setNeighbour(new Cell(new Position(0, 1)));
        cell.setNeighbour(new Cell(new Position(0, -1)));
        cell.setNeighbour(new Cell(new Position(1, 0)));
        cell.setNeighbour(new Cell(new Position(-1, 0)));

        barrel.addListener(new ObjectObserver());

        cell.addObject(barrel);
        cell.addObject(new BulletForTest());
        barrel.update();

        ArrayList<ObjectInCellEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new ObjectInCellEvent(barrel, ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(new Explosion(), ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(new Explosion(), ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(new Explosion(), ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(new Explosion(), ObjectInCellEvent.EventType.NEED_UPDATE));
        expectedEvents.add(new ObjectInCellEvent(barrel, ObjectInCellEvent.EventType.DAMAGED));
        expectedEvents.add(new ObjectInCellEvent(barrel, ObjectInCellEvent.EventType.NEED_UPDATE));

        ArrayList<Explosion> explosions = new ArrayList<>();

        Assertions.assertEquals(expectedEvents.size(), events.size());
        for(int i = 0; i < events.size(); i++){
            if (events.get(i).getObject() instanceof Explosion explosion){
                explosions.add(explosion);
            } else {
                Assertions.assertEquals(expectedEvents.get(i).getObject(), events.get(i).getObject());
            }
            Assertions.assertEquals(expectedEvents.get(i).getType(), events.get(i).getType());
        }
        Assertions.assertTrue(barrel.isDestroying());

        for (Direction direction : Direction.values()){
            Cell neighbour = cell.getNeighbour(direction);
            Assertions.assertTrue(neighbour.getObjects().contains(explosions.get(direction.ordinal())));
            Assertions.assertEquals(neighbour, explosions.get(direction.ordinal()).getCell());
        }
    }
}
