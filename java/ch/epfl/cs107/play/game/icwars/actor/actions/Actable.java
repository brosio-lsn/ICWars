package ch.epfl.cs107.play.game.icwars.actor.actions;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsPlayer;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * TODO
 */
public interface Actable extends Graphics {
    /**
     * TODO
     *
     * @param dt
     * @param player
     * @param keyboard
     */
    void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);

    //actions for the AIPlayer does nothing by default
    default void doAutoAction(float dt, AIPlayer player) {
    }
}
