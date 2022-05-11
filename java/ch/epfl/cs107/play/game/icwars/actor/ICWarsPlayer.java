package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 */
public class ICWarsPlayer extends ICWarsActor implements Interactor {
    /**
     * TODO
     */
    public States playerCurrentState;

    /**
     * TODO
     */
    protected ArrayList<Unit> units = new ArrayList<>();

    /**
     * TODO
     */
    protected Sprite sprite;

    /**
     * TODO
     */
    protected Unit SelectedUnit;

    /**
     * TODO
     */
    protected boolean EnterWasReleased;

    /**
     * TODO
     */
    ICWarsPlayerGUI playerGUI = new ICWarsPlayerGUI(this.getOwnerArea().getCameraScaleFactor(), this);

    /**
     * TODO
     *
     * @param area
     * @param position
     * @param faction
     * @param units
     */
    public ICWarsPlayer(Area area, DiscreteCoordinates position, Faction faction, Unit... units) {
        // TODO comments

        super(area, position, faction);
        this.units.addAll(Arrays.asList(units));
        RegisterUnitsAsActors();
        this.getOwnerArea().fillUnits();
        this.playerCurrentState = States.IDLE;
        setEnterWasReleased(false);
    }

    /**
     * TODO
     */
    public void setStateToNormal() {
        this.playerCurrentState = States.NORMAL;
    }

    /**
     * TODO
     * <p>
     * register all the units of the player in the player's ownerArea
     */
    private void RegisterUnitsAsActors() {
        // TODO comments

        units.forEach(unit -> unit.enterArea(
            this.getOwnerArea(),
            new DiscreteCoordinates((int) unit.getPosition().x, (int) unit.getPosition().y)
        ));
    }

    /**
     * TODO
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        // TODO comments

        if (this.playerCurrentState != States.IDLE) {
            this.sprite.draw(canvas);
        }
    }

    /**
     * TODO
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        // TODO comments

        super.update(deltaTime);
        // removing all the units that have hp below zero from the units list of the player and unregister this unit form the ownerArea
        ArrayList<Unit> newUnits = new ArrayList<>();
        for (Unit unit : units)
            if (unit.isDead()) {
                unit.leaveArea();
                this.getOwnerArea().fillUnits();
            } else newUnits.add(unit);
        units = newUnits;
        drawOpacityOfUnits();
    }

    public void updatePlayersUnitsHP(){
        for(Unit unit : units){
            unit.updateHP();
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean isIdle() {
        return playerCurrentState.equals(States.IDLE);
    }

    /**
     * TODO
     */
    @Override
    public void leaveArea() {
        units.forEach(ICWarsActor::leaveArea);
        super.leaveArea();
    }

    /**
     * TODO
     * is called when the player couldn't attack an ennemy (he has another chance to do sth)
     */
    public void canSelectActionAgain() {
        getOwnerArea().setViewCandidate(this);
        playerCurrentState = States.ACTION_SELECTION;
    }

    /**
     * TODO
     *
     * @param area
     * @param position
     */
    @Override
    public void enterArea(Area area, DiscreteCoordinates position) {
        super.enterArea(area, position);
    }

    /**
     * TODO
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * TODO
     * <p>
     * a unit doesn't take spaceCellSpace
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * TODO
     * <p>
     * a unit is not ViewInteractable
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * TODO
     * <p>
     * a unit is CellInteractable
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * TODO
     *
     * @return the sprite name
     */
    protected String getSpriteName() {
        if (this.faction == Faction.ALLY)
            return "icwars/allyCursor";
        else return "icwars/enemyCursor";
    }

    /**
     * TODO
     *
     * @return true if the arraylist of units is empty
     */
    public boolean isDefeated() {
        return units.isEmpty();
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean isNotDefeated() {
        return !isDefeated();
    }

    /**
     * TODO
     *
     * @param index the index of the selected Unit in the player's units list
     *              SelectedUnit parameter is associated to the proper Unit
     *              SelectedUnit is also transmitter to the playerGUI with the setter
     */
    public void selectUnit(int index) {
        SelectedUnit = this.units.get(index);
        playerGUI.setPlayerSelectedUnit(this.SelectedUnit);
    }

    /**
     * TODO
     *
     * @param unit the unit the player wants to select
     *             SelectedUnit parameter is associated to the proper Unit
     *             SelectedUnit is also transmitter to the playerGUI with the setter
     */
    public void selectUnit(Unit unit) {
        SelectedUnit = unit;
        playerGUI.setPlayerSelectedUnit(this.SelectedUnit);
    }

    /**
     * TODO
     * <p>
     * if a unit `UNIT` has the same position as the player, and he hasn't already been moved, we call SelectedUnit(UNIT)
     * else SelectedUnit is set to null
     */
    public Unit selectUnit() {
        units.stream() // for all units
            .filter(u -> u.getPosition().equals(this.getPosition())) // if they are on the same square as the player
            .filter(Unit::hasNotAlreadyMoved) // and haven't already moved
            .findFirst() // find the first one
            .ifPresentOrElse( // if there is one that fits the criteria
                this::selectUnit, // select it
                () -> SelectedUnit = null); // else `selectedUnit` is null
        return this.SelectedUnit;
    }

    /**
     * TODO
     * <p>
     * SelectedUnit is set to null
     */
    public void unselectUnit() {
        SelectedUnit = null;
    }

    /**
     * TODO
     * <p>
     * when the turn starts for the player, he enters the NORMAL state,
     * the camera is centered on him,
     * all his units can be moved
     */
    public void startTurn() {
        this.playerCurrentState = States.NORMAL;
        this.getOwnerArea().setViewCandidate(this);
        this.units = units
            .stream()
            .filter(Unit::isAlive)
            .collect(Collectors.toCollection(ArrayList::new));
        units.forEach(unit -> unit.setIsAlreadyMoved(false));
    }

    /**
     * TODO
     */
    public void endTurn() {
        this.units.forEach(unit -> unit.setIsAlreadyMoved(false));
    }

    /**
     * TODO
     *
     * @param coordinates used for `super.onLeaving(coordinates)`
     *                    in addition, playerCurrentState is set to NORMAl so that the player is available for future interactions
     */
    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        this.playerCurrentState = switch (this.playerCurrentState) {
            case IDLE, ACTION, ACTION_SELECTION, MOVE_UNIT, NORMAL -> this.playerCurrentState;
            case SELECT_CELL -> States.NORMAL;
        };
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * TODO
     *
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {

    }

    /**
     * TODO
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        v.interactWith(this);
    }

    /**
     * TODO
     * <p>
     * the sprite of the already moved units has a smaller opacity
     * than the sprite of other  units
     */
    private void drawOpacityOfUnits() {
        units.forEach(unit -> unit.sprite.setAlpha(unit.hasAlreadyMoved ? 0.1f : 1.0f));
    }

    /**
     * TODO
     */
    protected boolean enterWasReleased() {
        return EnterWasReleased;
    }

    /**
     * TODO
     *
     * @param enterWasReleased
     */
    protected void setEnterWasReleased(boolean enterWasReleased) {
        EnterWasReleased = enterWasReleased;
    }

    /**
     * TODO
     * <p>
     * states that an `ICWarsPlayer` can be in
     */
    public enum States {
        /**
         * TODO
         */
        IDLE,

        /**
         * TODO
         */
        NORMAL,

        /**
         * TODO
         */
        SELECT_CELL,

        /**
         * TODO
         */
        MOVE_UNIT,

        /**
         * TODO
         */
        ACTION_SELECTION,

        /**
         * TODO
         */
        ACTION,
    }
}
