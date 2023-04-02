package events;

import java.util.EventListener;

/**
 * Слушатель игровых объектов
 */
public interface IObjectInCellEventListener extends EventListener {

    void onObjectInCellAction(ObjectInCellEvent event);
}
