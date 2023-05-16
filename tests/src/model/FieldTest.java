package model;

import model.measures.Position;
import model.environment.Environment;
import model.testprefabs.EnvironmentForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FieldTest {

    @Test
    public void constructor_fieldCreation(){
        Environment environment = new EnvironmentForTest();
        Field field = new Field(environment);

        for (int i = 0; i < environment.fieldSize().height(); i++){
            for (int j = 0; j < environment.fieldSize().width(); j++){
                Assertions.assertNotNull(field.getCell(new Position(j, i)));
            }
        }

        Assertions.assertEquals(2, field.getTanks().size());
        Assertions.assertNotNull(field.getTanks().get(0));
        Assertions.assertNotNull(field.getTanks().get(1));
    }
}
