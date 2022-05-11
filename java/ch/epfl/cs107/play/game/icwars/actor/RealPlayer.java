package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.actions.Action;
import ch.epfl.cs107.play.game.icwars.actor.actions.Attack;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.EnumSet;

/**
 * TODO
 */
public class RealPlayer extends ICWarsPlayer {

    /**
     * TODO
     * <p>
     * Animation duration in frame number
     */
    private final static int MOVE_DURATION = 6;

    /**
     * TODO
     */
    private final ICWarsPlayerInteractionHandler handler = new ICWarsPlayerInteractionHandler(this);

    /**
     * TODO
     */
    private Action ActionToExecute;

    /**
     * TODO
     *
     * @param area
     * @param position
     * @param faction
     * @param units
     */
    public RealPlayer(Area area, DiscreteCoordinates position, Faction faction, Unit... units) {
        super(area, position, faction, units);
        this.sprite = new Sprite(
            this.getSpriteName(),
            1.5f,
            1.5f,
            this,
            null,
            new Vector(-0.25f, -0.25f)
        );
        this.playerGUI = new ICWarsPlayerGUI(this.getOwnerArea().getCameraScaleFactor(), this);
    }

    /**
     * TODO
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        // TODO comments

        Keyboard keyboard = getOwnerArea().getKeyboard();
        //make sure the player can't move when it isn't his turn
        if (this.playerCurrentState != States.IDLE && RealPlayerCanMove()) {
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        if (keyboard.get(Keyboard.Q).isDown()) getOwnerArea().close();
        this.playerCurrentState = switch (playerCurrentState) {
            case IDLE -> playerCurrentState;
            case NORMAL -> {
                this.getOwnerArea().setViewCandidate(this);
                //TODO improve this
                if (ActionToExecute != null)
                    if (ActionToExecute instanceof Attack)
                        ((Attack) ActionToExecute).IndexOfUnitToAttackCanBeSetToZero(true);
                if (!keyboard.get(Keyboard.ENTER).isReleased())
                    setEnterWasReleased(false);
                else if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    yield States.SELECT_CELL;
                }
                if (keyboard.get(Keyboard.TAB).isReleased()) {
                    setEnterWasReleased(false);
                    yield States.IDLE;
                } else yield !enterWasReleased()
                    ? keyboard.get(Keyboard.ENTER).isReleased() ? States.SELECT_CELL : playerCurrentState
                    : playerCurrentState;
            }
            case SELECT_CELL -> {
                yield this.selectUnit() != null
                    ? States.MOVE_UNIT
                    : playerCurrentState;
            }
            case MOVE_UNIT -> {
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    var pos = this.getPosition();
                    if (this.SelectedUnit.changePosition(new DiscreteCoordinates((int) pos.x, (int) pos.y))) {
                        SelectedUnit.setIsAlreadyMoved(true);
                        setEnterWasReleased(true);
                        yield States.ACTION_SELECTION;
                    } else yield States.NORMAL;
                } else yield playerCurrentState;
            }
            case ACTION_SELECTION -> {
                for (Action action : this.SelectedUnit.getAvailableActions()) {
                    if (keyboard.get(action.getKey()).isReleased()) {
                        this.ActionToExecute = action;
                        yield (States.ACTION);
                    }
                }
                yield playerCurrentState;
            }
            case ACTION -> {
                ActionToExecute.doAction(deltaTime, this, keyboard);
                //TODO improve this
                if (ActionToExecute instanceof Attack)
                    ((Attack) ActionToExecute).IndexOfUnitToAttackCanBeSetToZero(false);
                yield playerCurrentState;
            }
            // TODO
        };
        super.update(deltaTime);
    }

    /**
     * TODO
     * <p>
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        // TODO comments

        if (b.isDown() && isNoDisplacementOccurs()) {
            orientate(orientation);
            move(MOVE_DURATION);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (playerCurrentState) {
            case MOVE_UNIT -> playerGUI.draw(canvas);
            case ACTION_SELECTION -> playerGUI.drawActionsPanel(this.SelectedUnit.getAvailableActions(), canvas);
            case NORMAL, SELECT_CELL -> playerGUI.drawInfoPanel(canvas);
            case ACTION -> this.ActionToExecute.draw(canvas);
            default -> {
            }
        }
    }

    /**
     * TODO
     *
     * @param other
     */
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    /**
     * TODO
     *
     * @return true only if playerCurrentState = NORMAL, SELECT_UNIT or MOVE_UNIT
     */
    private boolean RealPlayerCanMove() {
        final var movableStates = EnumSet.of(States.NORMAL, States.SELECT_CELL, States.MOVE_UNIT);
        return movableStates.contains(this.playerCurrentState);
    }

    /**
     * TODO
     */
    private static class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        /**
         * TODO
         */
        RealPlayer player;

        /**
         * TODO
         *
         * @param player
         */
        public ICWarsPlayerInteractionHandler(RealPlayer player) {
            this.player = player;
        }

        /**
         * TODO
         *
         * @param unit
         */
        @Override
        public void interactWith(Unit unit) {
            player.playerGUI.setUnitOnCell(unit);
        }

        /**
         * TODO
         *
         * @param icWarsCell
         */
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell icWarsCell) {
            player.playerGUI.setNumberOfStarsOfCurrentCell(icWarsCell.getNumberOfStars());
            player.playerGUI.setTypeOfCurrentCell(icWarsCell.getType());
            player.playerGUI.setUnitOnCell(null);//when the player is not on a unit anymore, the playerGUI's OnCellUnit is set to null
        }
    }
}
