package model;

import events.IGameEventListener;
import events.ObjectInCellEvent;
import model.measures.Direction;
import model.measures.Position;
import model.testprefabs.EnvironmentForTest;
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

    private void waitGame(){
        try {
            long sleepTime = 2000L;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        Player activePlayer = game.activePlayer();
        activePlayer.rotate(Direction.WEST);

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(activePlayer.getTank(), ObjectInCellEvent.EventType.MOVED));

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankRotation_notSuccess(){
        Player activePlayer = game.activePlayer();
        activePlayer.rotate(Direction.NORTH);

        Assertions.assertEquals(0, objectInCellEvents.size());
        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankMoving_success(){
        Player activePlayer = game.activePlayer();
        activePlayer.rotate(Direction.EAST);
        activePlayer.move();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(activePlayer.getTank(), ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(activePlayer.getTank(), ObjectInCellEvent.EventType.MOVED));

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.GAME_IS_ON);

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertNotSame(game.activePlayer(), activePlayer);
    }

    @Test
    public void tankMoving_notSuccess(){
        Player activePlayer = game.activePlayer();
        activePlayer.move();

        Assertions.assertEquals(0, objectInCellEvents.size());
        Assertions.assertEquals(0, gameStates.size());
    }

    @Test
    public void tankSkipStep(){
        Player activePlayer = game.activePlayer();
        activePlayer.pass();

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.GAME_IS_ON);

        Assertions.assertEquals(0, objectInCellEvents.size());

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertNotSame(game.activePlayer(), activePlayer);
    }

    @Test
    public void tankShootingEnemyTank(){
        Bullet testBullet = new Bullet(Direction.NORTH, new Cell(new Position(0, 0)));
        Player player1 = game.activePlayer();
        Tank tank1 = player1.getTank();
        player1.rotate(Direction.EAST);
        player1.move();

        Player player2 = game.activePlayer();
        Tank tank2 = player2.getTank();
        player2.rotate(Direction.WEST);
        player2.move();

        player1.rotate(Direction.SOUTH);
        player1.shoot();
        waitGame();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(tank1, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(tank1, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(tank1, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(tank2, ObjectInCellEvent.EventType.DAMAGED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.DESTROYED));

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
        Bullet testBullet = new Bullet(Direction.NORTH, new Cell(new Position(0, 0)));
        Player activePlayer = game.activePlayer();
        activePlayer.rotate(Direction.EAST);
        activePlayer.shoot();
        waitGame();

        ArrayList<ObjectInCellEvent> expectedObjectEvents = new ArrayList<>();
        expectedObjectEvents.add(new ObjectInCellEvent(activePlayer.getTank(), ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.MOVED));
        expectedObjectEvents.add(new ObjectInCellEvent(new Headquarters(), ObjectInCellEvent.EventType.DESTROYED));
        expectedObjectEvents.add(new ObjectInCellEvent(testBullet, ObjectInCellEvent.EventType.DESTROYED));

        ArrayList<Game.State> expectedGameStates = new ArrayList<>();
        expectedGameStates.add(Game.State.WINNER_FOUND);

        Assertions.assertEquals(expectedObjectEvents.size(), objectInCellEvents.size());
        for (int i = 0; i < expectedObjectEvents.size(); i++){
            Assertions.assertEquals(expectedObjectEvents.get(i).getObject().getClass(), objectInCellEvents.get(i).getObject().getClass());
            Assertions.assertEquals(expectedObjectEvents.get(i).getType(), objectInCellEvents.get(i).getType());
        }

        Assertions.assertEquals(expectedGameStates.size(), gameStates.size());
        Assertions.assertEquals(expectedGameStates.get(0), gameStates.get(0));
        Assertions.assertSame(game.winner(), activePlayer);
    }
}
