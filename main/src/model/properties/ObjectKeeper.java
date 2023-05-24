package model.properties;

import model.Cell;
import model.ObjectInCell;

public interface ObjectKeeper<T extends ObjectInCell> {

    Cell getCell();

    T getObject();

    Class<T> getObjectClass();
}