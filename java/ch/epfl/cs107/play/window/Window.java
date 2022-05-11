package ch.epfl.cs107.play.window;

import ch.epfl.cs107.play.math.Attachable;

/**
 * Represents a context frame, which can act as a canvas.
 * Moreover, the camera can be attached to any positionable entity.
 */
public interface Window extends Canvas, Audio, Attachable, AutoCloseable {

    /**
     * @return (Button): whether the windows is active
     */
    Button getFocus();

    /**
     * @return (Mouse): associated mouse controller
     */
    Mouse getMouse();

    /**
     * @return (Keyboard): associated keyboard controller
     */
    Keyboard getKeyboard();

    // TO-DO gamepads

    /**
     * @return (boolean): whether the user tried to close the window
     */
    boolean isCloseRequested();

    // TO-DO this may require delta time, e.g. for mouse interpolation
    void update();
}
