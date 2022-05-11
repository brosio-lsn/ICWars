package ch.epfl.cs107.play.game.icwars.actor.units;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.actions.Action;
import ch.epfl.cs107.play.game.icwars.actor.actions.Attack;
import ch.epfl.cs107.play.game.icwars.actor.actions.TakeCity;
import ch.epfl.cs107.play.game.icwars.actor.actions.Wait;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;

/**
 * TODO
 */
public class Tank extends Unit {


    /**
     * TODO
     */
    final Wait Tankwait;

    /**
     * TODO
     */
    final Attack Tankattack;

    /**
     * TODO
     *  @param area     the area in which the unit is
     *
     * @param position position of the unit in the area
     * @param faction  faction to which the units belong (eiter ALLY or ENEMY
     */
    public Tank(Area area, DiscreteCoordinates position, Faction faction) {
        this(area, position, faction, 5, 10);
    }

    /**
     * TODO
     *
     * @param area     the area in which the unit is
     * @param position position of the unit in the area
     * @param faction  faction to which the units belong (eiter ALLY or ENEMY
     * @param repair   the amount that the unit can increase its HP
     * @param hp       the number of HP a unit has
     *                 the sprite of the tank is also initiated
     */
    public Tank(Area area, DiscreteCoordinates position, Faction faction, int repair, int hp) {
        // TODO comments

        super(area, position, faction, repair, 4, hp, 10, 7, "Tank");
        this.sprite = new Sprite(
            this.getSpriteName(),
            1.5f,
            1.5f,
            this,
            null,
            new Vector(-0.25f, -0.25f)
        );
        this.Tankwait = new Wait(this, this.getOwnerArea());
        this.actions.add(Tankwait);
        this.Tankattack = new Attack(this, this.getOwnerArea());
        this.actions.add(Tankattack);

    }

    @Override
    public void update(float deltaTime) {
        //if the unit is on a city add to actions takeCity if there is no takeCity in actions already
        if(this.getIsOnACity()){
            boolean contTainsAttack=false;
            for(Action action: actions){
                if(action instanceof TakeCity) contTainsAttack=true;
            }
            if(!contTainsAttack) actions.add(new TakeCity(this, this.getOwnerArea()));
        }
        else{
            //remove all the takeCity actions from the actions list
            ArrayList<Action> newActions = new ArrayList<>();
            for (Action action : actions) {
                if (!(action instanceof TakeCity)) newActions.add(action);
            }
            this.actions=newActions;
        }
        super.update(deltaTime);
    }

    /**
     * TODO
     *
     * @return the name of the tank depending on its faction (it will be useful to draw the sprite in the constructor)
     */

    protected String getSpriteName() {
        if (this.faction.equals(Faction.ALLY)) return "icwars/friendlyTank";
        else return "icwars/enemyTank";
    }

    @Override
    public Attack getAttackAction() {
        return this.Tankattack;
    }
}
