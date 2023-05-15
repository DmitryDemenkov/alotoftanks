package view;

import model.Cell;
import model.ObjectInCell;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Представление ячейки
 */
public class CellWidget extends JPanel {

    /**
     * Размер ячейки
     */
    private static final int CELL_SIZE = 80;

    /**
     * Цвет ячейки
     */
    private static final Color BACKGROUND = Color.decode("#585553");

    /**
     * Модель ячейки
     */
    private final Cell _cell;

    public CellWidget(Cell cell, WidgetPool pool){
        _cell = cell;
        setPreferredSize(getDimension());
        setBackground(BACKGROUND);
        setLayout(new GridBagLayout());

        for (ObjectInCell object : cell.getObjects()){
            ObjectInCellWidget widget = pool.getWidget(object);
            addObjectWidget(widget);
        }
    }

    /**
     * Изменить активность, если ячейка активна, она подцвечивается зеленым
     * @param active активность ячейки
     */
    void setActive(boolean active){
        if (active){
            setBorder(BorderFactory.createLineBorder(Color.GREEN));
        } else {
            setBorder(BorderFactory.createEmptyBorder());
        }
        repaintLayers();
    }

    /**
     * Получить размер ячейки
     * @return размер ячейки
     */
    public Dimension getDimension(){
        return new Dimension(CELL_SIZE, CELL_SIZE);
    }

    /**
     * Дочерние виджеты, упорядоченные по слоям
     */
    private final List<ObjectInCellWidget> _layers = new ArrayList<>();

    /**
     * Добавить виджет объекта
     * @param widget виджет добавляемого объекта
     */
    void addObjectWidget(ObjectInCellWidget widget){
        widget.setCellWidget(this);
        _layers.add(widget);
        repaintLayers();
    }

    /**
     * Удалить виджет объекта
     * @param widget удаляемый виджет
     */
    void removeObjectWidget(ObjectInCellWidget widget){
        widget.setCellWidget(null);
        _layers.remove(widget);
        repaintLayers();
    }

    /**
     * Переписовать ячейку с учетом порядка дочерних элементов
     */
    private void repaintLayers(){
        removeAll();

        for (ObjectInCellWidget layer : _layers) {
            layer.removeAll();
        }

        _layers.sort(Comparator.comparing(ObjectInCellWidget::getLayer));

        if (_layers.size() > 0){
            add(_layers.get(0));
        }

        for (int i = 0; i < _layers.size() - 1; i++){
            _layers.get(i).add(_layers.get(i + 1));
        }

        repaint();
    }
}
