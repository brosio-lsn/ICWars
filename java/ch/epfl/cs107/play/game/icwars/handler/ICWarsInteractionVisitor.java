package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.City;
import ch.epfl.cs107.play.game.icwars.actor.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;

/**
 * TODO
 */
public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {
    /**
     * TODO
     *
     * @param unit
     */
    default void interactWith(Unit unit) {
        //empty by default
    }

    /**
     * TODO
     *
     * @param player
     */
    default void interactWith(RealPlayer player) {
        //empty by default
    }

    /**
     * TODO
     *
     * @param icWarsCell
     */
    default void interactWith(ICWarsBehavior.ICWarsCell icWarsCell) {
        //empty by default
    }

    /**
     * TODO
     *
     * @param city
     */
    default void interactWith(City city) {
        //empty by default
    }
}
