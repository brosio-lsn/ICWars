package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class City extends ICWarsActor implements Interactable {

    public static final int HPgivenToUnit  = 1;
    Sprite sprite;

    public City (Area area, DiscreteCoordinates position, Faction faction){
        super(area, position, faction);
        this.sprite = new Sprite(
                this.getSpriteName(),
                1.5f,
                1.5f,
                this,
                null,
                new Vector(-0.25f, -0.25f));
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite = new Sprite(
                this.getSpriteName(),
                1.5f,
                1.5f,
                this,
                null,
                new Vector(-0.25f, -0.25f));
        this.sprite.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * TODO
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor) v).interactWith(this);
    }

    /**
     * @param faction the faction of the unit that interacts with the City is given to the cities's faction
     */
    protected void takeCity(Faction faction){
        this.faction=faction;
    }

    private String getSpriteName() {
        if (this.faction.equals(Faction.ALLY)) return "icwars/friendlyBuilding";
        else if (this.faction.equals(Faction.ENEMY)) return "icwars/enemyBuilding";
        else return "icwars/neutralBuilding";
    }
}
