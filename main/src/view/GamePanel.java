package view;

import events.IGameEventListener;
import events.ObjectInCellEvent;
import model.cellobjects.tank.Player;
import model.Game;
import model.environment.SimpleEnvironment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
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
    private final Map<Player, PlayerPanel> _playerPanels = new HashMap<>();

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

        _playerPanels.clear();
        initializePlayers();
        for(PlayerPanel playerPanel : _playerPanels.values()){
            content.add(playerPanel);
        }
        content.add(fieldWidget, 1);

        repaint();
        revalidate();
    }

    /**
     * Присвоить игрокам их цвета
     */
    private void initializePlayers(){
        Color[] colors = new Color[] {Color.BLUE, Color.ORANGE};
        for (int i = 0; i < _game.getPlayers().size(); i++){
            Player player = _game.getPlayers().get(i);
            PlayerPanel playerPanel = new PlayerPanel(player, colors[i], _pool);
            _playerPanels.put(player, playerPanel);
        }
    }

    /**
     * Запустить игру
     */
    private void start(){
        _game.start();
        _playerPanels.get(_game.activePlayer()).setActive(true);
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
            for (PlayerPanel panel : _playerPanels.values()) {
                panel.update();
            }

            if (state == Game.State.GAME_IS_ON){
                _playerPanels.get(_game.activePlayer()).setActive(true);
            } else if (state == Game.State.WINNER_FOUND){
                String winnerName = getColorName(((TankWidget)_pool.getWidget(_game.winner().getTank())).getColor());
                JOptionPane.showMessageDialog(GamePanel.this, winnerName + " танк победил");
                restart();
            } else if (state == Game.State.DRAW) {
                JOptionPane.showMessageDialog(GamePanel.this, "Игра завершилась вничью");
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
