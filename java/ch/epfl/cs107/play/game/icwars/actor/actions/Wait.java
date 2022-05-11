package ch.epfl.cs107.play.game.icwars.actor.actions;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * TODO
 */
public class Wait extends Action {
    /**
     * TODO
     *
     * @param unit
     * @param area
     */
    public Wait(Unit unit, Area area) {
        super(unit, area, "(W)ait", Keyboard.W);
    }

    /**
     * TODO
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {

    }

    /**
     * TODO
     *
     * @param dt
     * @param player
     * @param keyboard
     */
    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        this.unit.setIsAlreadyMoved(true);
        player.setStateToNormal();
    }

}
