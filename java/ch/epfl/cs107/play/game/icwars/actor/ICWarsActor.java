package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

/**
 * TODO
 */
abstract public class ICWarsActor extends MovableAreaEntity {
    /**
     * TODO
     */
    public Faction faction;

    /**
     * TODO
     * <p>
     * Default MovableAreaEntity constructor
     *
     * @param area     (Area): Owner area. Not null
     * @param faction  (Orientation): Initial orientation of the entity. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public ICWarsActor(Area area, DiscreteCoordinates position, Faction faction) {
        super(area, Orientation.UP, position);
        this.faction = faction;
    }

    /**
     * TODO
     *
     * @param area
     * @param position
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        // TODO comments

        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }

    /**
     * TODO
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * TODO
     *
     * @return
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    // faction aly is associated to boolean true, enemy to false

    public enum Faction {
        /**
         * TODO
         */
        ALLY,

        NEUTRAL,

        /**
         * TODO
         */
        ENEMY;

        /**
         * TODO
         */
        //final boolean isAlly;

        /**
         * TODO
         *
         * @param isAlly
         */
       /* Faction(boolean isAlly) {
            this.isAlly = isAlly;
        }*/
    }
}
