package model.cellobjects;

/**
 * Вода, мешающая танку и неуязвима для снаряла
 */
public class Water extends Obstacle{

    @Override
    public boolean isDestroying() {
        return false;
    }
}
