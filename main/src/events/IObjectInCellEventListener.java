package events;

import java.util.EventListener;

public interface IObjectInCellEventListener extends EventListener {

    void onObjectInCellAction(ObjectInCellEvent event);
}
