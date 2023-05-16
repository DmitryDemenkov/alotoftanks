package model.measures;

/**
 * Размеры игрового поля
 */
public class Size {

    public Size(int width, int height){
        _width = width;
        _height = height;
    }

    private final int _width;

    public int width(){
        return _width;
    }

    private final int _height;

    public int height(){
        return _height;
    }
}
