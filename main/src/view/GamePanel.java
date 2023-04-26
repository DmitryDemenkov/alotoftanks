package view;

import events.IGameEventListener;
import events.ObjectInCellEvent;
import model.Game;
import model.Tank;
import model.environment.SimpleEnvironment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        repaint();
        revalidate();
    }

    /**
     * Запустить игру
     */
    private void start(){
        _game.start();
        ((TankWidget) _pool.getWidget(_game.activeTank())).setActive(true);
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
            if (event.getType() == ObjectInCellEvent.EventType.MOVING && widget instanceof MovableObjectWidget movableWidget){
                CellWidget cellWidget = event.getObject().getCell() != null ? _pool.getWidget(event.getObject().getCell()) : null;
                movableWidget.moveTo(cellWidget);
            } else{
                widget.getDamage();
                if (event.getType() == ObjectInCellEvent.EventType.DESTROYING){
                    _pool.removeWidget(event.getObject());
                }
            }
        }

        @Override
        public void onGameStateChanged(Game.State state) {
            for (PlayerPanel panel: _playerPanels ) {
                panel.update();
            }

            if (state == Game.State.GAME_IS_ON){
                ((TankWidget) _pool.getWidget(_game.activeTank())).setActive(true);
            } else if (state == Game.State.WINNER_FOUND){
                String winnerName = getColorName(((TankWidget)_pool.getWidget(_game.winner())).getColor());
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
}
