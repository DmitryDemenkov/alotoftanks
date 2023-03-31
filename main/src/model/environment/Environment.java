package model.environment;

import model.Field;
import model.Size;

public abstract class Environment {

    public abstract Size fieldSize();

    public abstract void fillField(Field field);
}
