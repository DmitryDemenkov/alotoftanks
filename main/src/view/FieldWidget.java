package view;

import model.Field;
import model.measures.Position;

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
}
