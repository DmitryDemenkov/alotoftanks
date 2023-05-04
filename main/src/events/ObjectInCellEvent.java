package events;

import model.ObjectInCell;

/**
 * События, генерируемы игровыми объектами
 */
public class ObjectInCellEvent {

    public enum EventType{
        NEED_UPDATE,
        DESTROYED,
        MOVED,
        DAMAGED
    }

    private ObjectInCell _object;

    public ObjectInCell getObject(){
        return _object;
    }

    private EventType _type;

    public EventType getType() { return _type; }

    public ObjectInCellEvent(ObjectInCell object, EventType type){
        _object = object;
        _type = type;
    }
}
