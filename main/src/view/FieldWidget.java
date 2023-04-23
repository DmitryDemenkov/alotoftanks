package view;

import model.Field;
import model.Headquarters;
import model.Position;
import model.Tank;

import javax.swing.*;
import java.awt.*;

public class FieldWidget extends JPanel {
    private final Field _field;

    public FieldWidget(Field field, WidgetPool pool){
        _field = field;
        setLayout(new GridLayout(_field.getSize().width(), _field.getSize().height(), 10, 10));
        setBackground(Color.BLACK);
        fill(pool);
        initializePlayers(pool);
    }

    private void fill(WidgetPool pool){
        for (int y = 0; y < _field.getSize().height(); y++){
            for (int x = 0; x < _field.getSize().width(); x++){
                CellWidget cellWidget = pool.getWidget(_field.getCell(new Position(x, y)));
                add(cellWidget);
            }
        }
    }

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
