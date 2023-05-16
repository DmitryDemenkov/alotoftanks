package model;

import model.measures.Direction;
import model.measures.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionTest {

    private Position position = new Position(0, 0);

    @Test
    public void positionCreation(){
        position = new Position(1, 2);

        Assertions.assertEquals(1, position.getX());
        Assertions.assertEquals(2, position.getY());
    }

    @Test
    public void shift_shiftToNorthOnPositiveDelta(){
        Position newPosition = position.shift(Direction.NORTH, 1);

        Position expectedPosition = new Position(0, -1);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToSouthOnPositiveDelta(){
        Position newPosition = position.shift(Direction.SOUTH, 1);

        Position expectedPosition = new Position(0, 1);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToEastOnPositiveDelta(){
        Position newPosition = position.shift(Direction.EAST, 1);

        Position expectedPosition = new Position(1, 0);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToWestOnPositiveDelta(){
        Position newPosition = position.shift(Direction.WEST, 1);

        Position expectedPosition = new Position(-1, 0);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToNorthOnNegativeDelta(){
        Position newPosition = position.shift(Direction.NORTH, -1);

        Position expectedPosition = new Position(0, 1);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToSouthOnNegativeDelta(){
        Position newPosition = position.shift(Direction.SOUTH, -1);

        Position expectedPosition = new Position(0, -1);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToEastOnNegativeDelta(){
        Position newPosition = position.shift(Direction.EAST, -1);

        Position expectedPosition = new Position(-1, 0);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToWestOnNegativeDelta(){
        Position newPosition = position.shift(Direction.WEST, -1);

        Position expectedPosition = new Position(1, 0);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }

    @Test
    public void shift_shiftToZeroDelta(){
        Position newPosition = position.shift(Direction.NORTH, 0);

        Position expectedPosition = new Position(0, 0);

        Assertions.assertNotNull(newPosition);

        Assertions.assertEquals(expectedPosition.getX(), newPosition.getX());
        Assertions.assertEquals(expectedPosition.getY(), newPosition.getY());

        Assertions.assertEquals(0, position.getX());
        Assertions.assertEquals(0, position.getY());
    }
}