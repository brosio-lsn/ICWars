package ch.epfl.cs107.play.game.icwars.area.level;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.units.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.units.Tank;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Level0 extends ICWarsArea {
    /**
     * This returns that faction's units on this level
     *
     * @param faction
     * @return
     */
    public List<Unit> factionUnits(ICWarsActor.Faction faction) {
        // TODO comments

        return switch (faction) {
            case ALLY -> new ArrayList<>(List.of(new Unit[]{
                new Tank(
                    this,
                    new DiscreteCoordinates(2, 5),
                    ICWarsActor.Faction.ALLY
                ),
                new Soldier(
                    this,
                    new DiscreteCoordinates(3, 5),
                    ICWarsActor.Faction.ALLY
                )})
            );
            case NEUTRAL -> null;
            case ENEMY -> new ArrayList<>(List.of(new Unit[]{
                new Tank(
                    this,
                    new DiscreteCoordinates(3, 8),
                    ICWarsActor.Faction.ENEMY
                ),
                new Soldier(
                    this,
                    new DiscreteCoordinates(9, 5),
                    ICWarsActor.Faction.ENEMY
                )})
            );
        };
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public DiscreteCoordinates getFactionCenter(ICWarsActor.Faction faction) {
        // TODO comments

        return switch (faction) {
            case ALLY -> new DiscreteCoordinates(0, 0);
            case ENEMY -> new DiscreteCoordinates(7, 4);
            default -> null ;
        };
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    /**
     * TODO
     */
    protected void createArea() {
        // Base
        registerActor(new Background(this));
        //System.out.println("level 0 width"+this.getWidth());
    }
}
