package ch.epfl.cs107.play.game.icwars.actor.actions;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class TakeCity extends Action{

    /**
     * TODO
     *
     * @param unit
     * @param area
     */
    public TakeCity(Unit unit, Area area) {
        super(unit, area, "(T)ake", Keyboard.T);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        this.unit.takeCity();
        this.unit.setIsAlreadyMoved(true);
        unit.setHasTakenACity(true);
        player.setStateToNormal();
    }
}
