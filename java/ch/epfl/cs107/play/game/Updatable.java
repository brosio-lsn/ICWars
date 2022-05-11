package ch.epfl.cs107.play.game;


/**
 * Represents an updatable element (which can be updated)
 */
public interface Updatable {

    /**
     * Simulates a single time step.
     * Note: Need to be Overridden
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    void update(float deltaTime);
}
