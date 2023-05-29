package view;

import model.cellobjects.tank.Player;
import model.measures.Direction;
import view.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Представление панели игрока
 */
public class PlayerPanel extends JPanel {

    /**
     * Игрок
     */
    private final Player _player;

    private final TankWidget _tankWidget;

    /**
     * Максимальное кол-во жизней игрока
     */
    private final int _maxHeath;

    /**
     * Цвет игрока
     */
    private final Color _color;

    public PlayerPanel(Player player, Color color, WidgetPool widgetPool){
        _player = player;
        _tankWidget = (TankWidget) widgetPool.getWidget(_player.getTank());
        HeadquartersWidget headquartersWidget = (HeadquartersWidget) widgetPool.getWidget(_player.getTank().getHeadquarters());

        _color = color;
        _tankWidget.setColor(color);
        headquartersWidget.setColor(color);

        _maxHeath = _player.getTank().getHealth();

        setPreferredSize(new Dimension(150, 600));
        setLayout(new GridLayout(3, 1, 0, 50));
        setBackground(Color.BLACK);
        addKeyListener(new KeyListener());

        update();
    }

    public void setActive(boolean active){
        _tankWidget.setActive(active);
        setFocusable(active);
        requestFocus();
    }

    /**
     * Обновить информацию об игроке
     */
    public void update(){
        removeAll();
        add(getFlagBar());
        add(getHeathBar());
        add(getReloadGunBar());
        repaint();
        revalidate();
    }

    /**
     * Получить панель с жизнями игрока
     * @return панель с жизнями игрока
     */
    private JPanel getHeathBar(){
        JPanel heathBar = new JPanel();
        heathBar.setLayout(new GridLayout(_maxHeath, 1));
        heathBar.setBackground(Color.BLACK);

        for (int i = 1; i <= _maxHeath; i++){
            boolean heathActive = i <= _player.getTank().getHealth();
            heathBar.add(new JLabel(new ImageIcon(getHeartImage(heathActive))));
        }

        return heathBar;
    }

    /**
     * Получить изображение сердца
     * @param isActive активное сердце - красное, неактивное - серое
     * @return изображения сердца
     */
    private BufferedImage getHeartImage(boolean isActive){
        String path = isActive ? "resources/red_heart.png" : "resources/grey_heart.png";

        return getImage(new File(path), new Dimension(50, 50));
    }

    /**
     * Получить панель с флагом игрока
     * @return панель с флагом
     */
    private JPanel getFlagBar(){
        JPanel flagBar = new JPanel();
        flagBar.setLayout(new GridBagLayout());
        flagBar.setBackground(Color.BLACK);
        flagBar.add(new JLabel(new ImageIcon(getFlagImage())));
        return flagBar;
    }

    /**
     * Получить изображение флага игрока, соотыетсвующее его цвету
     * @return изображение флага
     */
    private BufferedImage getFlagImage(){
        String path = _color == Color.BLUE ? "resources/blue_flag.png" : "resources/orange_flag.png";

        return getImage(new File(path), new Dimension(100, 100));
    }

    /**
     * Получить панель перезарядки орудия
     * @return панель перезарядки
     */
    private JPanel getReloadGunBar(){
        JPanel reloadPanel = new JPanel();
        reloadPanel.setLayout(new GridBagLayout());
        reloadPanel.setBackground(Color.BLACK);

        JLabel label;
        if (_player.getTank().getCurrentReloadTime() > 0){
            label = new JLabel(Integer.toString(_player.getTank().getCurrentReloadTime()),
                    new ImageIcon(getReloadImage(false)),
                    JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setFont(new Font("Serif", Font.PLAIN, 36));
            label.setForeground(Color.decode("#6d6d6d"));
        } else {
            label = new JLabel(new ImageIcon(getReloadImage(true)));
        }

        reloadPanel.add(label);
        return reloadPanel;
    }

    /**
     * Получить изображение перезарядки орудия
     * @param isActive активность орудия
     * @return изображение перезарядки
     */
    private BufferedImage getReloadImage(boolean isActive){
        String path = isActive ? "resources/bullet.png" : "resources/grey_bullet.png";

        return getImage(new File(path), new Dimension(50, 50));
    }

    /**
     * Получить изображение из файла
     * @param file файл с изображением
     * @param dimension размер изображения
     * @return изображение из файла
     */
    private BufferedImage getImage(File file, Dimension dimension){
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            image = ImageUtils.resizeImage(image, dimension.width, dimension.height);
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
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
            setActive(false);
            int key = e.getKeyCode();
            move(key);
            pass(key);
            shoot(key);
            rotate(key);
            setActive(_player.isActive());
        }

        private void move(int keyCode){
            if (keyCode == KeyEvent.VK_SPACE){
                _player.move();
            }
        }

        private void shoot(int keyCode){
            if (keyCode == KeyEvent.VK_ENTER){
                _player.shoot();
            }
        }

        private void pass(int keyCode){
            if (keyCode == KeyEvent.VK_BACK_SPACE){
                _player.pass();
            }
        }

        private void rotate(int keyCode){
            Map<Integer, Direction> keyCodeToDirection = new HashMap<>();
            keyCodeToDirection.put(KeyEvent.VK_W, Direction.NORTH);
            keyCodeToDirection.put(KeyEvent.VK_A, Direction.WEST);
            keyCodeToDirection.put(KeyEvent.VK_S, Direction.SOUTH);
            keyCodeToDirection.put(KeyEvent.VK_D, Direction.EAST);

            if (keyCodeToDirection.containsKey(keyCode)){
                _player.rotate(keyCodeToDirection.get(keyCode));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
