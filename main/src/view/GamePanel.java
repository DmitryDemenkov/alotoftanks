package view;

import events.IGameEventListener;
import events.ObjectInCellEvent;
import model.measures.Direction;
import model.Game;
import model.Tank;
import model.environment.SimpleEnvironment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Представление игры
 */
public class GamePanel extends JFrame {

    /**
     * Пул виджетов, содержащий виджеты ячеек и объектов на поле
     */
    private final WidgetPool _pool = new WidgetPool();

    /**
     * Панели игроков
     */
    private final List<PlayerPanel> _playerPanels = new ArrayList<>();

    /**
     * Модель игры
     */
    private Game _game;

    public GamePanel(){
        create();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        start();
    }

    /**
     * Создать игру и ее представление
     */
    private void create(){
        _game = new Game(new SimpleEnvironment());
        FieldWidget fieldWidget = new FieldWidget(_game.getField(), _pool);
        _game.addListener(new GameListener());

        JPanel content = (JPanel) getContentPane();
        content.removeAll();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        for (Tank tank : _game.getField().getTanks()){
            PlayerPanel panel = new PlayerPanel((TankWidget)_pool.getWidget(tank));
            content.add(panel);
            _playerPanels.add(panel);
        }

        content.add(fieldWidget, 1);
        addKeyListener(new KeyListener());

        setFocusable(true);
        requestFocus();
        repaint();
        revalidate();
    }

    /**
     * Запустить игру
     */
    private void start(){
        _game.start();
        ((TankWidget) _pool.getWidget(_game.activePlayer().getTank())).setActive(true);
    }

    /**
     * Перезапустить игру
     */
    private void restart(){
        create();
        start();
    }

    /**
     * Слушатель игровых событий
     */
    private class GameListener implements IGameEventListener {

        @Override
        public void onObjectChanged(ObjectInCellEvent event) {
            ObjectInCellWidget widget = _pool.getWidget(event.getObject());
            if (event.getType() == ObjectInCellEvent.EventType.MOVED && widget instanceof MovableObjectWidget movableWidget){
                CellWidget cellWidget = event.getObject().getCell() != null ? _pool.getWidget(event.getObject().getCell()) : null;
                movableWidget.moveTo(cellWidget);
            } else if (event.getType() == ObjectInCellEvent.EventType.DAMAGED) {
                widget.getDamage();
            } else {
                widget.destroy();
                _pool.removeWidget(event.getObject());
            }
        }

        @Override
        public void onGameStateChanged(Game.State state) {
            for (PlayerPanel panel: _playerPanels ) {
                panel.update();
            }

            if (state == Game.State.GAME_IS_ON){
                ((TankWidget) _pool.getWidget(_game.activePlayer().getTank())).setActive(true);
            } else if (state == Game.State.WINNER_FOUND){
                String winnerName = getColorName(((TankWidget)_pool.getWidget(_game.winner().getTank())).getColor());
                JOptionPane.showMessageDialog(GamePanel.this, winnerName + " танк победил");
                restart();
            }
        }

        private String getColorName(Color color){
            if (color == Color.BLUE){
                return "Синий";
            } else {
                return "Оранжевый";
            }
        }
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
            ((TankWidget) _pool.getWidget(_game.activePlayer().getTank())).setActive(false);
            int key = e.getKeyCode();
            move(key);
            pass(key);
            shoot(key);
            rotate(key);
            ((TankWidget) _pool.getWidget(_game.activePlayer().getTank())).setActive(_game.activePlayer().isActive());
        }

        private void move(int keyCode){
            if (keyCode == KeyEvent.VK_SPACE){
                _game.activePlayer().move();
            }
        }

        private void shoot(int keyCode){
            if (keyCode == KeyEvent.VK_ENTER){
                _game.activePlayer().shoot();
            }
        }

        private void pass(int keyCode){
            if (keyCode == KeyEvent.VK_BACK_SPACE){
                _game.activePlayer().pass();
            }
        }

        private void rotate(int keyCode){
            Map<Integer, Direction> keyCodeToDirection = new HashMap<>();
            keyCodeToDirection.put(KeyEvent.VK_W, Direction.NORTH);
            keyCodeToDirection.put(KeyEvent.VK_A, Direction.WEST);
            keyCodeToDirection.put(KeyEvent.VK_S, Direction.SOUTH);
            keyCodeToDirection.put(KeyEvent.VK_D, Direction.EAST);

            if (keyCodeToDirection.containsKey(keyCode)){
                _game.activePlayer().rotate(keyCodeToDirection.get(keyCode));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
