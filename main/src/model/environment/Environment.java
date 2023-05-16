package model.environment;

import model.Field;
import model.measures.Size;

/**
 * Окружение, определяющее размеры поля и наполняющее его объектами
 */
public abstract class Environment {

    public abstract Size fieldSize();

    public abstract void fillField(Field field);
}
