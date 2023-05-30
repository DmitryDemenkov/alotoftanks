package view;

import model.Cell;
import model.ObjectInCell;
import model.cellobjects.FuelOilBarrel;
import model.cellobjects.Thicket;
import model.cellobjects.Wall;
import model.cellobjects.Water;
import model.cellobjects.damaging.Bullet;
import model.cellobjects.damaging.Explosion;
import model.cellobjects.tank.Headquarters;
import model.cellobjects.tank.Tank;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class WidgetFactory {

    private static final Map<Class<? extends ObjectInCell>, Class<? extends ObjectInCellWidget>> widgetMap = Map.ofEntries(
            Map.entry(Bullet.class, BulletWidget.class),
            Map.entry(Explosion.class, ExplosionWidget.class),
            Map.entry(FuelOilBarrel.class, FuelOilBarrelWidget.class),
            Map.entry(Headquarters.class, HeadquartersWidget.class),
            Map.entry(Tank.class, TankWidget.class),
            Map.entry(Thicket.class, ThicketWidget.class),
            Map.entry(Wall.class, WallWidget.class),
            Map.entry(Water.class, WaterWidget.class)
    );

    public ObjectInCellWidget createWidget(ObjectInCell objectInCell){
        ObjectInCellWidget widget = null;
        try {
            Constructor<?> constructor = widgetMap.get(objectInCell.getClass()).getConstructor(objectInCell.getClass());
            widget = (ObjectInCellWidget) constructor.newInstance(objectInCell);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return widget;
    }

    public CellWidget createWidget(Cell cell){
        return new CellWidget(cell);
    }
}