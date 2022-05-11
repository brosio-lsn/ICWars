package ch.epfl.cs107.play.game.areagame;


import ch.epfl.cs107.play.game.PauseMenu;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * TODO
 * <p>
 * AreaPauseMenu extends standard a Pause menu. It is a context pause menu. Pause menu not of the game
 * but of a specific area.
 * When a game is in pause mode, you can update this instead of the currentArea
 */
public abstract class AreaPauseMenu extends PauseMenu {

    /// Owner Area
    /**
     * TODO
     */
    private boolean isResumeRequested;

    /**
     * TODO
     * <p>
     * Setter for the resume request
     */
    protected void requestAreaResume() {
        getOwner().requestResume();
        isResumeRequested = true;
    }

    /**
     * TODO
     *
     * @return (boolean): true if the resume is requested
     */
    protected boolean isResumeRequested() {
        return isResumeRequested;
    }

    /**
     * TODO
     *
     * @param window
     * @param fileSystem
     * @return
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        isResumeRequested = false;
        return super.begin(window, fileSystem);
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public String getTitle() {
        return "Area Pause Menu";
    }
}
