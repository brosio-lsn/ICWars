package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.PauseMenu;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * TODO
 * <p>
 * AreaGames are a concept of Game which is displayed in a (MxN) Grid which is called an Area
 * An AreaGame has multiple Areas
 */
abstract public class AreaGame implements Game, PauseMenu.Pausable {

    /**
     * TODO
     */
    protected List<ICWarsPlayer> players;

    /**
     * TODO
     */
    protected List<ICWarsPlayer> PlayersWaitingForNextTurn = new ArrayList<>();

    /**
     * TODO
     */
    protected List<ICWarsPlayer> PlayersWaitingForCurrentTurn = new ArrayList<>();

    /**
     * TODO
     */
    protected ICWarsPlayer activePlayer;

    /**
     * TODO
     * <p>
     * The current area the game is in
     */
    protected Area currentArea;

    /// Context objects

    /**
     * TODO
     */
    private Window window;

    /**
     * TODO
     */
    private FileSystem fileSystem;

    /// A map containing all the Area of the Game

    /**
     * TODO
     */
    private ArrayList<Area> areas;

    /// pause mechanics and menu to display. May be null

    /**
     * TODO
     */
    private boolean paused;

    /**
     * TODO
     */
    private boolean requestPause;

    /**
     * TODO
     */
    private PauseMenu menu;

    public AreaGame() {
        players = new ArrayList<>();
        PlayersWaitingForNextTurn = new ArrayList<>();
        PlayersWaitingForCurrentTurn = new ArrayList<>();
        areas = new ArrayList<>();
    }

    /**
     * TODO
     */
    protected final void resetPlayers() {
        players = new ArrayList<>();
        PlayersWaitingForNextTurn = new ArrayList<>();
        PlayersWaitingForCurrentTurn = new ArrayList<>();
    }

    /**
     * TODO
     * <p>
     * Add an Area to the AreaGame list
     *
     * @param a (Area): The area to add, not null
     */
    protected final void addArea(Area a) {
        areas.add(a);
    }

    /**
     * @param a if  the area a is in areas, return its index in areas
     */
    protected final int getIndexOfArea(Area a) {
        if (areas.contains(a)) {
            return areas.indexOf(a);
        } else {
            return -1;
        }
    }

    /**
     * @return size of areas
     */
    protected final int AreasSize() {
        return areas.size();
    }

    /**
     * TODO
     */
    protected final void resetAreas() {
        areas = new ArrayList<>();
    }

    /**
     * TODO
     *
     * @return
     */
    protected boolean nextArea() {
        // TODO comments

        int index = areas.indexOf(currentArea);
        if (index >= 0) {
            players.forEach(ICWarsPlayer::leaveArea);
            if (index < areas.size() - 1) {
                setCurrentArea(areas.get(index + 1), true);
                return true;
            } else {
                return false;
            }
        } else {
            setCurrentArea(areas.get(0), true);
            return true;
            //return false;
        }
    }

    /**
     * TODO
     *
     * @param area
     * @param forceBegin
     */
    protected final void setCurrentArea(Area area, boolean forceBegin) {
        // TODO comments

        var newArea = areas.stream()
            .filter(a -> Objects.equals(a.getTitle(), area.getTitle()))
            .findFirst();
        try {
            if (newArea.isEmpty()) {
                System.out.println("New Area not found, keep previous one");
            } else {
                // Stop previous area if it exists
                if (currentArea != null) {
                    currentArea.suspend();
                    currentArea.purgeRegistration(); // Is useful?
                }

                currentArea = newArea.get();

                // Start/Resume the new one
                if (forceBegin || !currentArea.isStarted())
                    currentArea.begin(window, fileSystem);
                else currentArea.resume(window, fileSystem);
            }
        } finally {
            newArea.ifPresent(Area::close);
        }
    }

    /**
     * TODO
     * <p>
     * Set the pause menu
     *
     * @param menu (PauseMenu) : the new pause menu, not null
     * @return (PauseMenu): the new pause menu, not null
     */
    protected final PauseMenu setPauseMenu(PauseMenu menu) {
        // TODO comments

        this.menu = menu;
        this.menu.begin(window, fileSystem);
        this.menu.setOwner(this);
        return menu;
    }

    /**
     * TODO
     *
     * @return (Window) : the Graphic and Audio context
     */
    protected final Window getWindow() {
        return window;
    }

    /**
     * TODO
     *
     * @return (FIleSystem): the linked file system
     */
    protected final FileSystem getFileSystem() {
        return fileSystem;
    }

    /**
     * TODO
     * <p>
     * Getter for the current area
     *
     * @return (Area)
     */
    protected final Area getCurrentArea() {
        return this.currentArea;
    }

    /**
     * Sets the {@link #window} and {@link #fileSystem}.
     * <p>
     * Initialises {@link #areas} and {@link #players} as new {@link ArrayList}, and {@link #paused} as {@code false}.
     *
     * @param window     The {@link Window} that the game is displayed in.
     * @param fileSystem The {@link FileSystem} that the game gets its visual resources from.
     * @return {@code true} if the game successfully started.
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        // TODO comments

        // Keep context
        this.window = window;
        this.fileSystem = fileSystem;

        areas = new ArrayList<>();
        players = new ArrayList<>();
        paused = false;
        return true;
    }

    /**
     * Updates the game, called every frame.
     * <p>
     * Either calls {@link PauseMenu#update(float) update} on the {@link #menu} or calls this function on
     * {@link #currentArea}.
     *
     * @param deltaTime The elapsed time since the last call to this function, in seconds, non-negative.
     */
    @Override
    public void update(float deltaTime) {
        if (paused && menu != null) // if the game is paused and there is a menu, run the menu
            menu.update(deltaTime);
        else currentArea.update(deltaTime); // else just update the game
        paused = requestPause;
    }

    /**
     * Sets {@link #requestPause} to {@code true}.
     * <p>
     * Implements {@link PauseMenu.Pausable}.
     */
    @Override
    public void requestPause() {
        requestPause = true;
    }

    /**
     * Sets {@link #requestPause} to {@code false}.
     * <p>
     * Implements {@link PauseMenu.Pausable}.
     */
    @Override
    public void requestResume() {
        requestPause = false;
    }

    /**
     * Tells you if this {@link AreaGame} is paused.
     * <p>
     * Implements {@link PauseMenu.Pausable}.
     *
     * @return {@link #paused}
     */
    @Override
    public boolean isPaused() {
        return paused;
    }
}
