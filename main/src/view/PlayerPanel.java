package view;

import model.Tank;
import view.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerPanel extends JPanel {

    private final Tank _tank;

    private final int _maxHeath;

    private final Color _color;

    public PlayerPanel(TankWidget tankWidget){
        _tank = (Tank) tankWidget.getObject();
        _maxHeath = _tank.getHealth();
        _color = tankWidget.getColor();

        setPreferredSize(new Dimension(150, 600));
        setLayout(new GridLayout(3, 1, 0, 50));
        setBackground(Color.BLACK);

        update();
    }

    public void update(){
        removeAll();
        add(getFlagBar());
        add(getHeathBar());
        add(getReloadGunBar());
        repaint();
        revalidate();
    }

    private JPanel getHeathBar(){
        JPanel heathBar = new JPanel();
        heathBar.setLayout(new GridLayout(_maxHeath, 1));
        heathBar.setBackground(Color.BLACK);

        for (int i = 1; i <= _maxHeath; i++){
            boolean heathActive = i <= _tank.getHealth();
            heathBar.add(new JLabel(new ImageIcon(getHeartImage(heathActive))));
        }

        return heathBar;
    }

    private BufferedImage getHeartImage(boolean isActive){
        String path = isActive ? "resources/red_heart.png" : "resources/grey_heart.png";

        return getImage(new File(path), new Dimension(50, 50));
    }

    private JPanel getFlagBar(){
        JPanel flagBar = new JPanel();
        flagBar.setLayout(new GridBagLayout());
        flagBar.setBackground(Color.BLACK);
        flagBar.add(new JLabel(new ImageIcon(getFlagImage())));
        return flagBar;
    }

    private BufferedImage getFlagImage(){
        String path = _color == Color.BLUE ? "resources/blue_flag.png" : "resources/orange_flag.png";

        return getImage(new File(path), new Dimension(100, 100));
    }

    private JPanel getReloadGunBar(){
        JPanel reloadPanel = new JPanel();
        reloadPanel.setLayout(new GridBagLayout());
        reloadPanel.setBackground(Color.BLACK);

        JLabel label = null;
        if (_tank.getCurrentReloadTime() > 0){
            label = new JLabel(Integer.toString(_tank.getCurrentReloadTime()),
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

    private BufferedImage getReloadImage(boolean isActive){
        String path = isActive ? "resources/bullet.png" : "resources/grey_bullet.png";

        return getImage(new File(path), new Dimension(50, 50));
    }

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
}
