package model.properties;

import model.Cell;
import model.ObjectInCell;

/**
 * Интерфейс объекта, который может содержать другие объекты
 * @param <T> тип объекта, который может содержать хранитель
 */
public interface ObjectKeeper<T extends ObjectInCell> {

    /**
     * Получить ячейку, в которой находится объект
     * @return ячейка, в которой находится объект
     */
    Cell getCell();

    /**
     * Получить объект, который находится внутри
     * @return объект, который находится внутри
     */
    T getObject();

    /**
     * Получить тип объекта внутри
     * @return тип объекта внутри
     */
    Class<T> getObjectClass();
}