package view;

import model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Пул виджетов, расположенных на поле
 */
public class WidgetPool {

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

        ObjectInCellWidget widget = null;
        if (object instanceof Tank tank){
            widget = new TankWidget(tank);
        } else if (object instanceof Wall wall){
            widget = new WallWidget(wall);
        } else if (object instanceof Headquarters headquarters){
            widget = new HeadquartersWidget(headquarters);
        } else if (object instanceof Water water){
            widget = new WaterWidget(water);
        } else if (object instanceof Bullet bullet){
            widget = new BulletWidget(bullet);
        }

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

        CellWidget widget = new CellWidget(cell, this);
        _cellWidgets.put(cell, widget);
        return widget;
    }
}
