package model;

import events.IGameEventListener;
import events.ObjectInCellEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameTest {

    private ArrayList<Game.State> gameStates = new ArrayList<>();

    private ArrayList<ObjectInCellEvent> objectInCellEvents = new ArrayList<>();

    private Game game;

    private class GameListener implements IGameEventListener{

        @Override
        public void onObjectChanged(ObjectInCellEvent event) {
            objectInCellEvents.add(event);
        }

        @Override
        public void onGameStateChanged(Game.State state) {
            gameStates.add(state);
        }
    }

    @BeforeEach
    public void testConfiguration(){
        gameStates.clear();
        objectInCellEvents.clear();

        game = new Game(new EnvironmentForTest());
        game.addListener(new GameListener());
        game.start();
    }

    @Test
    public void tankRotation_success(){
        Tank activeTank = game.activeTank();
        activeTank.setDirection(Direction.WEST);

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(activeTank, ObjectInCellEvent.EventType.MOVING));

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankRotation_notSuccess(){
        Tank activeTank = game.activeTank();
        activeTank.setDirection(Direction.NORTH);

        Assertions.assertEquals(0, objectInCellEvents.size());
        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankMoving_success(){
        Tank activeTank = game.activeTank();
        activeTank.setDirection(Direction.WEST);
        activeTank.move();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(activeTank, ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(activeTank, ObjectInCellEvent.EventType.MOVING));

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.GAME_IS_ON);

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertNotSame(game.activeTank(), activeTank);
    }

    @Test
    public void tankMoving_notSuccess(){
        Tank activeTank = game.activeTank();
        activeTank.move();

        Assertions.assertEquals(0, objectInCellEvents.size());
        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankSkipStep(){
        Tank activeTank = game.activeTank();
        activeTank.pass();

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.GAME_IS_ON);

        Assertions.assertEquals(0, objectInCellEvents.size());

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertNotSame(game.activeTank(), activeTank);
    }

    @Test
    public void tankShootingEnemyTank(){
        Tank tank1 = game.activeTank();
        tank1.setDirection(Direction.WEST);
        tank1.move();

        Tank tank2 = game.activeTank();
        tank2.setDirection(Direction.EAST);
        tank2.move();

        tank1.setDirection(Direction.SOUTH);
        tank1.shoot();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(tank1, ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(tank1, ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.DAMAGED));
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.DESTROYING));

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.GAME_IS_ON);
        expectedGameStates.add(Game.State.GAME_IS_ON);
        expectedGameStates.add(Game.State.GAME_IS_ON);

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        for (int i = 0; i < expectedGameStates.size(); i++) {
            Assertions.assertEquals(expectedGameStates.get(i), gameStates.get(i));
        }
    }

    @Test
    public void gameFinished_tankDestroyEnemyHeadquarters(){
        Tank activeTank = game.activeTank();
        activeTank.shoot();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.MOVING));
        expectedObjectEvents.add(new ObjectInCellEvent(new Headquarters(), ObjectInCellEvent.EventType.DESTROYING));
        expectedObjectEvents.add(new ObjectInCellEvent(new BulletForTest(), ObjectInCellEvent.EventType.DESTROYING));

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.WINNER_FOUND);

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertSame(game.winner(), activeTank);
    }
}
