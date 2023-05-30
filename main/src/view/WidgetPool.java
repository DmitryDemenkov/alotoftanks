package view;

import model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Пул виджетов, расположенных на поле
 */
public class WidgetPool {

    private final WidgetFactory _factory = new WidgetFactory();

    private final Map<ObjectInCell, ObjectInCellWidget> _objectWidgets = new HashMap<>();

    private final Map<Cell, CellWidget> _cellWidgets = new HashMap<>();

    public ObjectInCellWidget getWidget(ObjectInCell object){
        ObjectInCellWidget widget = _objectWidgets.get(object);
        if (widget == null){
            widget = createWidget(object);
        }
        return widget;
    }

    public ObjectInCellWidget removeWidget(ObjectInCell object){
        return _objectWidgets.remove(object);
    }

    private ObjectInCellWidget createWidget(ObjectInCell object){
        if (_objectWidgets.containsKey(object)){
            throw new IllegalArgumentException();
        }

        ObjectInCellWidget widget = _factory.createWidget(object);
        getWidget(object.getCell()).addObjectWidget(widget);
        _objectWidgets.put(object, widget);
        return widget;
    }

    public CellWidget getWidget(Cell cell){
        CellWidget widget = _cellWidgets.get(cell);
        if (widget == null){
            widget = createWidget(cell);
        }
        return widget;
    }

    private CellWidget createWidget(Cell cell){
        if (_cellWidgets.containsKey(cell)){
            throw new IllegalArgumentException();
        }

        CellWidget widget = _factory.createWidget(cell);
        _cellWidgets.put(cell, widget);
        for (ObjectInCell object : cell.getObjects()) {
            createWidget(object);
        }
        return widget;
    }
}
