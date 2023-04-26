package view;

import model.Direction;
import model.Tank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Прежставление танка
 */
public class TankWidget extends MovableObjectWidget {

    public TankWidget(Tank tank){
        super(tank);
        addKeyListener(new KeyListener());
    }

    /**
     * Получить модель танка
     * @return модель танка
     */
    private Tank getTank(){
        return (Tank) getObject();
    }

    /**
     * Установить активность виджета
     * @param active активность виджета
     */
    public void setActive(boolean active){
        setFocusable(active);
        getCellWidget().setActive(active);
        requestFocus();
        repaint();
    }

    /**
     * Цвет танка
     */
    private Color _color;

    /**
     * Задать цвет
     * @param color задаваемый цвет
     */
    void setColor(Color color){
        _color = color;
    }

    /**
     * Получить цвет танка
     * @return цвет танка
     */
    Color getColor(){
        return _color;
    }

    /**
     * Отрисовать получение урона
     */
    @Override
    public void getDamage(){
        if (getTank().getHealth() <= 0){
            super.getDamage();
        }
    }

    @Override
    public Dimension getDimension(){
        return new Dimension(60, 60);
    }

    @Override
    protected File getImageFile() {
        return getImageFileByColor(getColor());
    }

    /**
     * Получить файл с изображением танка, соответствующее его цвету
     * @param color цвет танка
     * @return файл с изоборажением танка
     */
    private File getImageFileByColor(Color color){
        String path = color == Color.BLUE ? "resources/blue_tank.png" : "resources/orange_tank.png";
        return new File(path);
    }

    /**
     * Слушатель пользовательского ввода
     */
    private class KeyListener implements java.awt.event.KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            move(key);
            pass(key);
            shoot(key);
            rotate(key);
            setActive(getTank().isActive());
        }

        private void move(int keyCode){
            if (keyCode == KeyEvent.VK_SPACE){
                getTank().move();
            }
        }

        private void shoot(int keyCode){
            if (keyCode == KeyEvent.VK_ENTER){
                getTank().shoot();
            }
        }

        private void pass(int keyCode){
            if (keyCode == KeyEvent.VK_BACK_SPACE){
                getTank().pass();
            }
        }

        private void rotate(int keyCode){
            Map<Integer, Direction> keyCodeToDirection = new HashMap<>();
            keyCodeToDirection.put(KeyEvent.VK_W, Direction.NORTH);
            keyCodeToDirection.put(KeyEvent.VK_A, Direction.WEST);
            keyCodeToDirection.put(KeyEvent.VK_S, Direction.SOUTH);
            keyCodeToDirection.put(KeyEvent.VK_D, Direction.EAST);

            if (keyCodeToDirection.containsKey(keyCode)){
                getTank().rotate(keyCodeToDirection.get(keyCode));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
