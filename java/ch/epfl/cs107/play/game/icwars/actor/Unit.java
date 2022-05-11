package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.actions.Action;
import ch.epfl.cs107.play.game.icwars.actor.actions.Attack;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A Unit is equivalent to a piece on a chess board. It's an entity that can be moved and interacts with other Units.
 */
abstract public class Unit extends ICWarsActor implements Interactor {

    /**
     * handler for the interactions
     */
    private final Unit.ICWarsUnitInteractionHandler handler = new Unit.ICWarsUnitInteractionHandler(this);
    /**
     * The {@link Unit}'s current health points. When this is less than or equal to {@code 0} the {@link Unit} is dead.
     */
    protected int current_HP;
    /**
     * The maximum health points of the {@link Unit}.
     */
    protected int maxHP;
    /**
     * The amount that the {@link Unit}'s {@link #current_HP} recovers at the end of a turn.
     */
    protected int repair;
    /**
     * The cumulative vertical and horizontal distance that the {@link Unit} can move in one turn.
     */
    protected int radius;
    /**
     * the effectiveRadius of the unit, which depends on day/night mode of the ownerArea
     */
    protected int effectiveRadius;
    /**
     * The damage this {@link Unit} deals when it {@link #attack(int) attacks}.
     */
    protected int damage;
    /**
     * If this is {@code true} then the {@link Unit} has already been moved this turn.
     */
    protected boolean hasAlreadyMoved;
    /**
     * The list of {@link Action actions} the {@link Unit} can take.
     */
    protected ArrayList<Action> actions;
    /**
     * The name of the {@link Unit}.
     */
    protected String name;
    /**
     * The {@link Unit}'s visual representation in the game.
     */
    protected Sprite sprite;
    /**
     * The collection of tiles that are in the {@link Unit}'s range and that it can move to. See {@link ICWarsRange}.
     */
    ICWarsRange range;
    /**
     * TODO
     */
    private int numberOfStarsOfCurrentCell;
    /**
     * TODO
     */
    private boolean isOnACity;
    /**
     * TODO
     */
    protected City cityOnCell;

    /**
     * TODO
     */
    private boolean hasTakenACity;

    /**
     * Initialises a Unit class with full health.
     * <p>
     * Calls {@link #Unit(Area, DiscreteCoordinates, Faction, int, int, int, int, int, String) Unit constructor} with
     * {@link #current_HP} equal to {@link #maxHP}.
     *
     * @param area     The {@link Area} in which the {@link Unit} acts. Passed straight to the
     *                 {@link ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param position The position that the {@link Unit} starts at in the {@link Area}.  Passed straight to the
     *                 {@link ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param faction  The {@link Faction Faction} to which the
     *                 {@link Unit} belongs to. Passed straight to the
     *                 {@link ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param repair   The amount that the {@link Unit}'s {@link #current_HP} recovers at the end of a turn. Passed to
     *                 {@link #repair}.
     * @param radius   The {@link Unit}'s movement. Passed to {@link #radius}.
     * @param maxHP    The maximum health points that the {@link Unit} has. Passed to {@link #maxHP}.
     * @param damage   The damage that this {@link Unit} deals. Passed to {@link #damage}
     */
    public Unit(Area area,
                DiscreteCoordinates position,
                Faction faction,
                int repair,
                int radius,
                int maxHP,
                int damage,
                String name) {
        this(area, position, faction, repair, radius, maxHP, maxHP, damage, name);
    }

    /**
     * Initialises a Unit class.
     *
     * @param area       The {@link Area} in which the {@link Unit} acts. Passed straight to the
     *                   {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param position   The position that the {@link Unit} starts at in the {@link Area}.  Passed straight to the
     *                   {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param faction    The {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.Faction Faction} to which the
     *                   {@link Unit} belongs to. Passed straight to the
     *                   {@link ch.epfl.cs107.play.game.icwars.actor.ICWarsActor#ICWarsActor(Area, DiscreteCoordinates, Faction) ICWarsActor constructor}.
     * @param repair     The amount that the {@link Unit}'s {@link #current_HP} recovers at the end of a turn. Passed to
     *                   {@link #repair}.
     * @param radius     The {@link Unit}'s movement. Passed to {@link #radius}.
     * @param current_HP The health points the {@link Unit} starts with. Passed to {@link #current_HP}.
     * @param maxHP      The maximum health points that the {@link Unit} has. Passed to {@link #maxHP}.
     * @param damage     The damage that this {@link Unit} deals. Passed to {@link #damage}
     */
    public Unit(Area area,
                DiscreteCoordinates position,
                Faction faction,
                int repair,
                int radius,
                int current_HP,
                int maxHP,
                int damage,
                String name) {
        // TODO comments

        super(area, position, faction);
        this.repair = repair;
        this.radius = radius;
        this.effectiveRadius=radius;
        this.maxHP = maxHP; // maxHP *must* be set before current_HP
        this.setHp(current_HP);
        this.damage = damage;
        this.range = new ICWarsRange();
        this.name = name;
        completeUnitsRange();
        this.hasAlreadyMoved = false;
        this.actions = new ArrayList<>();
    }

    /**
     * @return This {@link Unit}'s {@link #damage}.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * TODO
     *
     * @return the unit's hp
     */
    public int getHp() {
        return this.current_HP;
    }

    /**
     * Set's the {@link Unit}'s {@link #current_HP} whilst making sure that it's always greater than {@code 0} and not
     * more than {@link #maxHP}.
     *
     * @param HP The new HP value of this {@link Unit} (not necessarily the resulting HP value).
     */
    public void setHp(int HP) {
        this.current_HP = HP < 0 ? 0 : Math.min(HP, maxHP);
    }

    /**
     * @param isAlreadyMoved Sets {@link #hasAlreadyMoved} to the input.
     */
    public void setIsAlreadyMoved(boolean isAlreadyMoved) {
        this.hasAlreadyMoved = isAlreadyMoved;
    }

    /**
     * Tells you if the {@link Unit} has not moved this turn.
     *
     * @return {@code true} if the {@link Unit} hasn't moved on this turn yet, {@code false} if it has.
     */
    public boolean hasNotAlreadyMoved() {
        return !hasAlreadyMoved;
    }

    /**
     * Tells you if the {@link Unit} is alive.
     *
     * @return {@code true} if the {@link Unit}'s HP is positive.
     */
    public boolean isAlive() {
        return this.current_HP > 0;
    }

    /**
     * Tells you if the {@link Unit} is dead.
     * <p>
     * Is the boolean opposite of {@link #isAlive()}.
     *
     * @return {@code true} if the {@link Unit}'s {@link #current_HP} isn't positive.
     */
    public boolean isDead() {
        return !isAlive();
    }

    /**
     * Draws the {@link Unit} by calling {@link Sprite#draw(Canvas)} on its {@link #sprite} with the input
     * {@link Canvas}.
     * <p>
     * Implements {@link ch.epfl.cs107.play.game.actor.Graphics Graphics}.
     *
     * @param canvas The {@link Canvas} that the {@link #sprite} is drawn on.
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * @return The {@link Unit}'s {@link #name}.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Fills the {@link Unit}'s {@link #range} with the nodes that are both within the surrounding square in a distance of
     * {@link #radius} and in the {@link Area}.
     */
    private void completeUnitsRange() {
        // TODO comments

        this.range = new ICWarsRange();
        int widthIndex = this.getOwnerArea().getWidth() - 1;
        int heightIndex = this.getOwnerArea().getHeight() - 1;
        IntStream.rangeClosed(-effectiveRadius, effectiveRadius)
            .map(x -> x + this.getCurrentMainCellCoordinates().x)
            .filter(x -> x <= widthIndex)
            .filter(x -> x >= 0)
            .forEach(x -> IntStream.rangeClosed(-effectiveRadius, effectiveRadius)
                .map(y -> y + this.getCurrentMainCellCoordinates().y)
                .filter(y -> y <= heightIndex)
                .filter(y -> y >= 0)
                .forEach(y -> range.addNode(
                    new DiscreteCoordinates(x, y), // NodeCoordinates
                    x >= 0, // hasLeftNeighbour
                    y >= 0, // hasTopNeighbour
                    x <= widthIndex, // hasRightNeighbour
                    y <= heightIndex // hasUnderNeighbour
                )));
    }

    /**
     * Does this {@link Unit} take up space on the cell it's on.
     * <p>
     * Implements {@link ch.epfl.cs107.play.game.areagame.actor.Interactable Interactable}.
     *
     * @return {@code false} because this {@link Unit} doesn't take up space on the cell it's on.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Is this {@link Unit} {@code viewInteractable}.
     * <p>
     * Implements {@link ch.epfl.cs107.play.game.areagame.actor.Interactable Interactable}.
     *
     * @return {@code false} because this {@link Unit} isn't {@code viewInteractable}.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * a unit is CellInteractable
     * <p>
     * Implements {@link ch.epfl.cs107.play.game.areagame.actor.Interactable Interactable}.
     *
     * @return
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * TODO
     * <p>
     * Draw the unit's range and a path from the unit position to
     * destination
     *
     * @param destination path destination
     * @param canvas      canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {
        // TODO comments

        range.draw(canvas);
        var path =
            range.shortestPath(getCurrentMainCellCoordinates(), destination);
        // Draw path only if it exists (destination inside the range)
        if (path != null)
            new Path(getCurrentMainCellCoordinates().toVector(), path).draw(canvas);
    }

    /**
     * TODO
     *
     * @return The indexes of attackable units in the area's list of units.
     */
    public ArrayList<Integer> getIndexOfAttackableEnemies() {
        return this.getOwnerArea().getIndexOfAttackableEnemies(this.faction, this.range);
    }

    /**
     * Calls {@link Area#attack(int, int)} on the {@link Unit} pointed to by the {@code indexOfUnitToAttack}.
     *
     * @param indexOfUnitToAttack The index of the {@link Unit} in the {@link Area}'s units list that should be
     *                            attacked.
     */
    public void attack(int indexOfUnitToAttack) {
        // TODO comments

        this.getOwnerArea().attack(
            indexOfUnitToAttack,
            this.getDamage()
        );
    }

    /**
     * he unit attacks the attackable ennemy with loyest health
     *
     * @return what attackEnnemyWithLowestHealth returns
     */
    public int attackEnnemyWithLowestHealth(ArrayList<Integer> IndexOfAttackableEnemies) {
        return this.getOwnerArea().attackEnnemyWithLowestHealth(IndexOfAttackableEnemies, this.getDamage());
    }

    /**
     * @return The list of available {@link Action actions} that the {@link Unit} can take.
     */
    protected ArrayList<Action> getAvailableActions() {
        return this.actions;
    }

    /**
     * @return the attack action of the unit (by default it is null)
     */
    public Attack getAttackAction() {
        return null;
    }

    /**
     * If the node is in the {@link Area} and in the {@link Unit}'s {@link #range} it passes the new position on to
     * {@link ICWarsActor#changePosition(DiscreteCoordinates) super.changePosition}.
     * <p>
     * Then if fills the new range with {@link #completeUnitsRange()}.
     * <p>
     * Overrides
     * {@link ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity#changePosition(DiscreteCoordinates) MovableAreaEntity.changePosition(DiscreteCoordinates)}.
     *
     * @param newPosition The new position for the {@link Unit}.
     * @return Returns {@code true} if {@link ICWarsActor#changePosition(DiscreteCoordinates) super.changePosition} does
     * so, and if the newPosition coordinates exist in the {@link Unit}'s range.
     */
    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        // TODO comments

        if (this.range.nodeExists(newPosition) && super.changePosition(newPosition)) {
            completeUnitsRange();
            return true;
        } else return false;
    }

    public void changePositionOfAiPlayer(AIPlayer player) {
        player.changePosition(this.getCurrentMainCellCoordinates());
    }

    /**
     * TODO
     * <p>
     * Implements {@link ch.epfl.cs107.play.game.areagame.actor.Interactable Interactable}.
     *
     * @param areaInteractionVisitor The interaction
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor areaInteractionVisitor) {
        ((ICWarsInteractionVisitor) areaInteractionVisitor).interactWith(this);
    }

    /**
     * TODO
     *
     * @return {@link #numberOfStarsOfCurrentCell}
     */
    public int getNumberOfStarsOfCurrentCell() {
        return numberOfStarsOfCurrentCell;
    }

    /**
     * TODO
     *
     * @param numberOfStarsOfCurrentCell
     */
    public void setNumberOfStarsOfCurrentCell(int numberOfStarsOfCurrentCell) {
        this.numberOfStarsOfCurrentCell = numberOfStarsOfCurrentCell;
    }

    /**
     * @param enemies ennemie units on the map
     *                this method allows to move the unit at the node in it's range with closest position to the closest ennemy unit
     */
    public void moveTowardsClosestEnemy(ArrayList<Unit> enemies) {
        //find the Ennemy with closest coordinates
        //compute the equation of the line that goes through the ennemy and the attacking unit
        //let x and y be coordinates on this line, starting with the same value as the ennemy coordinates
        //while there exist no nodes at x and y coordinates in the attacking unit's range, x and y are changed towards the attacking unit
        if (!enemies.isEmpty()) {
            float UnitXCoordinate = this.getCurrentMainCellCoordinates().x;
            float UnitYCoordinate = this.getCurrentMainCellCoordinates().y;
            Unit closestEnnemy = enemies.get(0);
            double shortestDistance = Math.sqrt((closestEnnemy.getCurrentMainCellCoordinates().x - UnitXCoordinate) * (closestEnnemy.getCurrentMainCellCoordinates().x - UnitXCoordinate)
                + (closestEnnemy.getCurrentMainCellCoordinates().y - UnitYCoordinate) * (closestEnnemy.getCurrentMainCellCoordinates().y - UnitYCoordinate));
            for (Unit ennemyUnit : enemies) {
                double distance = Math.sqrt((ennemyUnit.getCurrentMainCellCoordinates().x - UnitXCoordinate) * (ennemyUnit.getCurrentMainCellCoordinates().x - UnitXCoordinate)
                    + (ennemyUnit.getCurrentMainCellCoordinates().y - UnitYCoordinate) * (ennemyUnit.getCurrentMainCellCoordinates().y - UnitYCoordinate));
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestEnnemy = ennemyUnit;
                }
            }

            //now compute the slope y=ax+b
            float slope;
            if (closestEnnemy.getCurrentMainCellCoordinates().x != UnitXCoordinate)
                slope = (closestEnnemy.getCurrentMainCellCoordinates().y - UnitYCoordinate) / (closestEnnemy.getCurrentMainCellCoordinates().x - UnitXCoordinate);
            else slope = (float) Double.POSITIVE_INFINITY;

            float constant = UnitYCoordinate - slope * UnitXCoordinate;
            //initiate x and y
            float x = closestEnnemy.getCurrentMainCellCoordinates().x;
            float y = closestEnnemy.getCurrentMainCellCoordinates().y;
            //change x and y until it is in the range
            boolean coordinatesToMoveToFound = this.range.nodeExists(new DiscreteCoordinates((int) x, (int) y));
            while (!coordinatesToMoveToFound) {
                if (slope != (float) Double.POSITIVE_INFINITY) {
                    x = (x > UnitXCoordinate) ? (x - 1) : (x + 1);
                    y = slope * x + constant;
                } else {
                    y = (y > UnitYCoordinate) ? (y - 1) : (y + 1);
                }
                coordinatesToMoveToFound = this.range.nodeExists(new DiscreteCoordinates((int) x, (int) y));
            }

            //move the unit towards the coordinates (x,y)
            this.changePosition(new DiscreteCoordinates((int) x, (int) y));
        }
    }

    /**
     * The way to make this {@link Unit} take damage (or otherwise reduce it's {@link #current_HP}).
     *
     * @param receivedDamage Subtracts this number from the {@link #current_HP}.
     */
    public void takeDamage(int receivedDamage) {
        this.setHp(current_HP - Math.max(receivedDamage - numberOfStarsOfCurrentCell, 0));
        //System.out.println(this.current_HP);
    }

    /**
     * Center the camera's focus on this {@link Unit}'s currently targeted enemy.
     *
     * @param indexOfUnitToAttack Passed to this {@link Unit unit's} {@link ICWarsPlayer owner's} {@link Area}'s
     *                            {@link Area#centerCameraOnTargetedEnemy(int) centerCameraOnTargetedEnemy} method.
     */
    public void centerCameraOnTargetedEnemy(int indexOfUnitToAttack) {
        this.getOwnerArea().centerCameraOnTargetedEnemy(indexOfUnitToAttack);
    }

    /**
     *
     */
    public void moveUnitTowarsClosestEnemy() {
        this.getOwnerArea()
            .moveUnitTowardsClosestEnnemy(this.faction, this);
    }

    /**
     *
     */
    public void takeCity(){
        this.cityOnCell.takeCity(this.faction);
    }

    protected boolean getIsOnACity(){
        return this.isOnACity;
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
     * @param other
     */
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
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
     * @param coordinates used for `super.onLeaving(coordinates)`
     *                    in addition, playerCurrentState is set to NORMAl so that the player is available for future interactions
     */
    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        this.isOnACity=false;
        cityOnCell=null;
    }

    /**
     * when it s night, the radius of the unit is decreased
     * the Unit's range is also updated
     */
    @Override
    public void update(float deltaTime) {
        if(((ICWarsArea)this.getOwnerArea()).isNight()){
            this.effectiveRadius=radius-1;
        }
        else{
            this.effectiveRadius=radius;
        }
        this.completeUnitsRange();
    }

    /**
     * at the end of the round, units on a city receive bonus HP
     */
    protected void updateHP(){
        if(this.hasTakenACity) {
            this.setHp(City.HPgivenToUnit + this.current_HP);
        }
    }

    public void setHasTakenACity(boolean hasTakenACity) {
        this.hasTakenACity = hasTakenACity;
    }

    /**
     * TODO
     */
    private static class ICWarsUnitInteractionHandler implements ICWarsInteractionVisitor {
        /**
         * The {@link Unit} that this {@link ICWarsUnitInteractionHandler interaction handler} handles interactions for.
         */
        Unit unit;

        /**
         * TODO
         *
         * @param unit
         */
        public ICWarsUnitInteractionHandler(Unit unit) {
            this.unit = unit;
        }

        /**
         * TODO
         * <p>
         * Implements {@link ICWarsInteractionVisitor}.
         *
         * @param icWarsCell
         */
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell icWarsCell) {
            unit.setNumberOfStarsOfCurrentCell(icWarsCell.getNumberOfStars());
        }

        /**
         * TODO
         * <p>
         * Implements {@link ICWarsInteractionVisitor}.
         *
         * @param city
         */
        @Override
        public void interactWith(City city) {
            unit.isOnACity=true;
            unit.cityOnCell=city;
        }
    }
}
