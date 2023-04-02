package model;

import events.ITankEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TankTest {

    private Tank tank;

    @BeforeEach
    public void testConfiguration(){
        tank = new Tank();
    }

    @Test
    public void constructor_tankCreation(){
        tank = new Tank();

        Assertions.assertNull(tank.getCell());
        Assertions.assertNull(tank.getHeadquarters());
        Assertions.assertEquals(Direction.NORTH, tank.getDirection());
        Assertions.assertEquals(3, tank.getHealth());
    }

    @Test
    public void canHaveCollision_collisionWithObstacle(){
        Obstacle obstacle = new Wall();

        boolean result = tank.canFaceWith(obstacle);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithSameObject(){
        boolean result = tank.canFaceWith(tank);

        Assertions.assertFalse(result);
    }

    @Test
    public void canHaveCollision_collisionWithBullet(){
        Bullet bullet = new BulletForTest();

        boolean result = tank.canFaceWith(bullet);

        Assertions.assertTrue(result);
    }

    @Test
    public void onCollision_collisionWithTank(){
        Tank otherTank = new Tank();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tank.faceWith(otherTank));
    }

    @Test
    public void onCollision_collisionWithObstacle(){
        Obstacle wall = new Wall();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tank.faceWith(wall));
    }

    @Test
    public void onCollision_collisionWithBullet() {
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(tank, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DAMAGED, event.getType());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.DAMAGED);

        tank.faceWith(bullet);

        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }

    @Test
    public void rotate_rotateTank(){
        ObjectInCellEvent[] actualEvents = {null};

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(tank, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                actualEvents[0] = event;
            }
        });

        tank.rotate(Direction.SOUTH);

        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertEquals(Direction.SOUTH, tank.getDirection());
    }

    @Test
    public void rotate_rotateTankOnSameDirection(){
        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        tank.rotate(Direction.NORTH);

        Assertions.assertEquals(Direction.NORTH, tank.getDirection());
    }

    @Test
    public void move_movingInEmptyCell(){
        Cell startCell = new Cell(new Position(0, 1));
        Cell targetCell = new Cell(new Position(0, 0));
        startCell.setNeighbour(targetCell);
        ObjectInCellEvent[] actualEvents = {null};

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                Assertions.assertSame(tank, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                Assertions.assertSame(targetCell, tank.getCell());
                actualEvents[0] = event;
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        boolean result = tank.move();

        Assertions.assertTrue(result);
        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void move_movingInNotEmptyCell(){
        Cell startCell = new Cell(new Position(0, 0));
        Cell targetCell = new Cell(new Position(0, 1));
        startCell.setNeighbour(targetCell);
        Wall wall = new Wall();

        startCell.addObject(tank);
        targetCell.addObject(wall);
        tank.rotate(Direction.NORTH);

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        boolean result = tank.move();

        Assertions.assertFalse(result);
    }

    @Test
    public void move_noCellInMovingDirection(){
        Cell startCell = new Cell(new Position(0, 0));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        boolean result = tank.move();

        Assertions.assertFalse(result);
        Assertions.assertSame(startCell, tank.getCell());
    }

    @Test
    public void shoot_shootInEmptyCell(){
        Cell startCell = new Cell(new Position(0, 1));
        Cell targetCell = new Cell(new Position(0, 0));
        startCell.setNeighbour(targetCell);
        ObjectInCellEvent[] actualEvents = {null};

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                Assertions.assertEquals(Bullet.class, event.getObject().getClass());
                Assertions.assertEquals(ObjectInCellEvent.EventType.MOVING, event.getType());
                actualEvents[0] = event;
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        boolean result = tank.shoot();

        Assertions.assertTrue(result);
        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void shoot_noCellInShootingDirection(){
        Cell startCell = new Cell(new Position(0, 0));

        startCell.addObject(tank);
        tank.rotate(Direction.NORTH);

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        boolean result = tank.shoot();

        Assertions.assertFalse(result);
    }

    @Test
    public void pass_skipStep(){
        ObjectInCellEvent[] actualEvents = {null};

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                Assertions.assertSame(tank, event.getObject());
                actualEvents[0] = event;
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }
        });

        tank.pass();

        Assertions.assertNotNull(actualEvents[0]);
    }

    @Test
    public void update_getDamage() {
        Cell cell = new Cell(new Position(0, 0));
        Bullet bullet = new BulletForTest();
        ObjectInCellEvent[] actualEvents = {null};

        tank.addListener(new ITankEventListener() {
            @Override
            public void onTankMoved(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankShot(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onTankSkipStep(ObjectInCellEvent event) {
                throw new RuntimeException("Unexpected event");
            }

            @Override
            public void onObjectInCellAction(ObjectInCellEvent event) {
                Assertions.assertSame(tank, event.getObject());
                Assertions.assertEquals(ObjectInCellEvent.EventType.DAMAGED, event.getType());
                Assertions.assertSame(cell, tank.getCell());
                actualEvents[0] = event;
            }
        });

        ObjectInCellEvent expectedEvent = new ObjectInCellEvent(tank, ObjectInCellEvent.EventType.DAMAGED);
        int expectedHealth = tank.getHealth() - 1;

        cell.addObject(tank);
        cell.addObject(bullet);
        tank.update();

        Assertions.assertNotNull(actualEvents[0]);
        Assertions.assertEquals(expectedHealth, tank.getHealth());
        Assertions.assertSame(expectedEvent.getObject(), actualEvents[0].getObject());
    }
}
