package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.gui.NightPannel;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

/**
 * TODO
 */
public abstract class ICWarsArea extends Area {
    /**
     * true if the area is in night mode, false otherwise
     */
    protected boolean night;
    /**
     * tells if the area is in night mode
     */
    public boolean isNight(){
        return night;
    }
    /**
     * sets the attribute night to the given value
     */
    public void setNight(boolean night){
        this.night=night;
    }

    public ICWarsArea(){

    }
    /**
     * Create the area by adding it all actors. Called by {@link #begin(Window, FileSystem)}.
     * <p>
     * Note: call {@link #setBehavior(AreaBehavior)} as needed too!
     */
    protected abstract void createArea();

    /**
     * Get the center coordinate of the {@link ICWarsArea} for the input faction.
     * <p>
     * This is used as the spawn coordinates for the player.
     *
     * @param faction The {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.Faction Faction} that the center point
     *                is returned for.
     */
    public abstract DiscreteCoordinates getFactionCenter(ICWarsActor.Faction faction);

    /**
     * Get a {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.Faction Faction's} {@link Unit Units} for this {@link Area}.
     *
     * @param faction The {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.Faction Faction} for which the
     *                {@link Unit Units} should be returned.
     * @return The list of {@link Unit Units}
     */
    public abstract List<Unit> factionUnits(ICWarsActor.Faction faction);

    /**
     * Returns the default scale factor of the camera when viewing this area
     *
     * @return {@code 10.f}
     */
    @Override
    public final float getCameraScaleFactor() {
        return 10.f;
    }


    /**
     * Start the area's simulation by:
     * <ul>
     * <li>call {@link Area#begin(Window, FileSystem)} and if successful continue;</li>
     * <li>call {@link #setBehavior(AreaBehavior)} with a new {@link ICWarsBehavior};</li>
     * <li>call {@link #createArea()}.</li>
     * </ul>
     *
     * @param window     The {@link Window} that the game is displayed in.
     * @param fileSystem The {@link FileSystem} that the game gets its visual resources from.
     * @return {@code true} if the game successfully started.
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        // TODO comments

        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            ICWarsBehavior icWarsBehavior = new ICWarsBehavior(window, getTitle());
            setBehavior(new ICWarsBehavior(window, getTitle()));
            createArea();
            icWarsBehavior.registerCities(this);
            night=false;
            this.registerActor(new NightPannel(this));
            return true;
        } else return false;
    }
}
