package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Shape;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class NightPannel implements Graphics, Actor {
    /**
     * the area in which the NightPannel is registered as an actor
     */
    ICWarsArea area;
    /**
     * the filter that will be applied to the area if it's in night mode
     */
    private ShapeGraphics filter;

    /**
     * @param canvas target, not null
     *  if the area is in night mode, a dark filter is applied
     */
    @Override
    public void draw(Canvas canvas) {
        if(this.area.isNight()){
            Shape rect = new Polygon(0, 0, 0, area.getCameraScaleFactor(), area.getCameraScaleFactor(), area.getCameraScaleFactor(), area.getCameraScaleFactor(), 0);
            filter = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, 0.7f, 3000f);
            filter.draw(canvas);
        }
    }

    /**
     * @param area is given to this.area
     */
    public NightPannel(ICWarsArea area){
        this.area=area;
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
