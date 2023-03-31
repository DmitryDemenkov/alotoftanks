package events;

public interface ITankEventListener extends IObjectInCellEventListener {

    void onTankMoved(ObjectInCellEvent event);

    void onTankShot(ObjectInCellEvent event);

    void onTankSkipStep(ObjectInCellEvent event);
}
