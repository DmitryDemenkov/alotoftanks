package view;

import model.Field;
import model.cellobjects.tank.Headquarters;
import model.measures.Position;
import model.cellobjects.tank.Tank;

import javax.swing.*;
import java.awt.*;

/**
 * Представление поля
 */
public class FieldWidget extends JPanel {

    /**
     * Модель поля
     */
    private final Field _field;

    public FieldWidget(Field field, WidgetPool pool){
        _field = field;
        setLayout(new GridLayout(_field.getSize().width(), _field.getSize().height(), 10, 10));
        setBackground(Color.BLACK);
        fill(pool);
        initializePlayers(pool);
    }

    /**
     * Заполнение поля
     * @param pool пул виджетов, содержащий виджеты ячейки
     */
    private void fill(WidgetPool pool){
        for (int y = 0; y < _field.getSize().height(); y++){
            for (int x = 0; x < _field.getSize().width(); x++){
                CellWidget cellWidget = pool.getWidget(_field.getCell(new Position(x, y)));
                add(cellWidget);
            }
        }
    }

    /**
     * Присвоить игрокам их цвета
     * @param pool, пул виджетов, содержащий виджеты танков и штабов
     */
    private void initializePlayers(WidgetPool pool){
        Color[] colors = new Color[] {Color.BLUE, Color.ORANGE};
        for (int i = 0; i < _field.getTanks().size(); i++){
            Tank tank = _field.getTanks().get(i);
            TankWidget tankWidget = (TankWidget) pool.getWidget(tank);
            tankWidget.setColor(colors[i]);

            Headquarters headquarters = tank.getHeadquarters();
            HeadquartersWidget headquartersWidget = (HeadquartersWidget) pool.getWidget(headquarters);
            headquartersWidget.setColor(colors[i]);
        }
    }
}
