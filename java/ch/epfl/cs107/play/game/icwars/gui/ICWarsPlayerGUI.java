package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.actions.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * TODO
 */
public class ICWarsPlayerGUI implements Graphics {

    /**
     * TODO
     */
    static final float FONT_SIZE = 20.f;

    /**
     * TODO
     */
    private final ICWarsActionsPanel actionsPanel;

    /**
     * TODO
     */
    ICWarsPlayer player;

    /**
     * TODO
     */
    Unit PlayerSelectedUnit;

    /**
     * TODO
     */
    float cameraScaleFactor;

    /**
     * TODO
     */
    ICWarsInfoPanel infoPanel;

    /**
     * TODO
     */
    private int NumberOfStarsOfCurrentCell;

    /**
     * TODO
     */
    private ICWarsBehavior.ICWarsCellType TypeOfCurrentCell;

    /**
     * TODO
     */
    private Unit unitOnCell;

    /**
     * TODO
     *
     * @param cameraScaleFactor
     * @param player
     */
    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        // TODO comments

        this.player = player;
        this.cameraScaleFactor = cameraScaleFactor;
        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
    }

    /**
     * TODO
     *
     * @param numberOfStarsOfCurrentCell
     */
    public void setNumberOfStarsOfCurrentCell(int numberOfStarsOfCurrentCell) {
        NumberOfStarsOfCurrentCell = numberOfStarsOfCurrentCell;
    }

    /**
     * TODO
     *
     * @param typeOfCurrentCell
     */
    public void setTypeOfCurrentCell(ICWarsBehavior.ICWarsCellType typeOfCurrentCell) {
        TypeOfCurrentCell = typeOfCurrentCell;
    }

    /**
     * TODO
     *
     * @param unitOnCell
     */
    public void setUnitOnCell(Unit unitOnCell) {
        this.unitOnCell = unitOnCell;
    }

    /**
     * TODO
     *
     * @param playerSelectedUnit is given as a value to the ICWarsPlayerGUI' playerSelectedUnit
     */
    public void setPlayerSelectedUnit(Unit playerSelectedUnit) {
        this.PlayerSelectedUnit = playerSelectedUnit;
    }

    /**
     * TODO
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        // TODO comments

        if (PlayerSelectedUnit != null && PlayerSelectedUnit.hasNotAlreadyMoved()) {
            PlayerSelectedUnit
                .drawRangeAndPathTo(
                    new DiscreteCoordinates((int) player.getPosition().x, (int) player.getPosition().y),
                    canvas
                );
        }
    }

    /**
     * TODO
     *
     * @param actions
     * @param canvas
     */
    public void drawActionsPanel(List<Action> actions, Canvas canvas) {
        this.actionsPanel.setActions(actions);
        this.actionsPanel.draw(canvas);
    }

    public void drawInfoPanel(Canvas canvas) {
        this.infoPanel.setCurrentCell(TypeOfCurrentCell);
        //System.out.println(unitOnCell);
        this.infoPanel.setUnit(this.unitOnCell);
        this.infoPanel.draw(canvas);
    }
}