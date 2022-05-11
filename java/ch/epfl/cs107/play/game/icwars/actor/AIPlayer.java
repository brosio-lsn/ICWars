package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.actions.Attack;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

/**
 * TODO
 */
public class AIPlayer extends ICWarsPlayer {
    float waitForValue;
    float counter;
    boolean counting;
    private Attack attack;

    /**
     * TODO
     *  @param area
     *
     * @param position
     * @param faction
     * @param units
     */
    public AIPlayer(Area area, DiscreteCoordinates position, Faction faction, Unit... units) {
        super(area, position, faction, units);
        this.sprite = new Sprite(this.getSpriteName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        this.counter = 0.f;
        counting = false;
        waitForValue = 100000000.0f;
    }

    public void update(float deltaTime) {
        this.playerCurrentState = switch (playerCurrentState) {
            case IDLE -> playerCurrentState;
            case NORMAL -> {
                this.getOwnerArea().setViewCandidate(this);
                //TODO improve this
                if (attack != null) {
                    attack.IndexOfUnitToAttackCanBeSetToZero(true);
                }
                yield States.ACTION;
            }
            case SELECT_CELL, MOVE_UNIT, ACTION_SELECTION -> {
                yield playerCurrentState;
            }
            case ACTION -> {
                this.makeAllUnitAttack(deltaTime);
                yield States.IDLE;
            }
            // TODO
        };
        super.update(deltaTime);
    }

    /**
     * @param dt passed to doAutoAction method
     *           makes sure that all the unit perform an autoAttack
     */
    private void makeAllUnitAttack(float dt) {
        for (Unit unit : units) {
            //ask the unit to to the position change so that it doesn t have to share its coordinates
            unit.changePositionOfAiPlayer(this);
            this.counting = true;
            waitFor(waitForValue, dt);
            attack = unit.getAttackAction();
            if (this.attack != null) {
                attack.doAutoAction(dt, this);
            }
        }
    }

    /**
     * Ensures that value time elapsed before returning true
     *
     * @param dt    elapsed time
     * @param value waiting time (in seconds )
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value, float dt) {
        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0f;
            counting = true;
        }
        return false;
    }
}
