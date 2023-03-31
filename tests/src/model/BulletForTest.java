package model;

import model.Bullet;
import model.Cell;
import model.Direction;
import model.Position;

public class BulletForTest extends Bullet {

    public BulletForTest(){
        super(Direction.NORTH, new Cell(new Position(0, 0)));
    }
}
