package events;

/**
 * Слушатель танков
 */
public interface ITankEventListener extends IObjectInCellEventListener {

    void onTankMoved(ObjectInCellEvent event);

    void onTankShot(ObjectInCellEvent event);

    void onTankSkipStep(ObjectInCellEvent event);
}
