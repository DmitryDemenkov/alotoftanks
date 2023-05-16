package model.testprefabs;

import model.Bullet;
import model.Cell;
import model.measures.Direction;
import model.measures.Position;

public class BulletForTest extends Bullet {

    public BulletForTest(){
        super(Direction.NORTH, new Cell(new Position(0, 0)));
    }
}
