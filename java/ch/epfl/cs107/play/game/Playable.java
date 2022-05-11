package ch.epfl.cs107.play.game;

import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;


public interface Playable extends Updatable, AutoCloseable {

    /**
     * Initialises game state : display and controls
     * Note: Need to be Overridden
     *
     * @param window     (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return (boolean): whether the game was successfully started
     */
    boolean begin(Window window, FileSystem fileSystem);

    /**
     * Getter for game title
     * Note: Need to be Overridden
     *
     * @return (String) the game title
     */
    String getTitle();
}
