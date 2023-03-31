package model;

public class Position {

    private final int _x;

    public int getX() {
        return _x;
    }

    private final int _y;

    public int getY(){
        return _y;
    }

    public Position(int x, int y){
        _x = x;
        _y = y;
    }

    public Position shift(Direction direction, int delta){
        int xDirection = 0;
        int yDirection = 0;

        switch (direction){
            case NORTH -> yDirection = -1;
            case SOUTH -> yDirection = 1;
            case WEST -> xDirection = -1;
            case EAST -> xDirection = 1;
        }

        int newX = getX() + xDirection * delta;
        int newY = getY() + yDirection * delta;

        return new Position(newX, newY);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position other)){
            return false;
        }

        return this.getX() == other.getX() && this.getY() == other.getY();
    }
}
