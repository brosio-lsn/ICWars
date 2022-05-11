package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/**
 * TODO
 * <p>
 * A drawable AreaGraph
 */
public class ICWarsRange extends AreaGraph implements Graphics {

    /**
     * TODO
     *
     * @param coordinates  (DiscreteCoordinate): Position in the graph of the node to add, used as key for the map, not null
     * @param hasLeftEdge  (boolean): indicate if directed edge to the left direction exists
     * @param hasUpEdge    (boolean): indicate if directed edge to the up direction exists
     * @param hasRightEdge (boolean): indicate if directed edge to the right direction exists
     * @param hasDownEdge  (boolean): indicate if directed edge to the down direction exists
     */
    @Override
    public void addNode(DiscreteCoordinates coordinates, boolean hasLeftEdge, boolean hasUpEdge, boolean hasRightEdge, boolean hasDownEdge) {
        getNodes().putIfAbsent(
            coordinates,
            new RangeNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge)
        );
    }

    /**
     * TODO
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        getNodes().values()
            .forEach(node -> ((RangeNode) node).draw(canvas));
    }

    /**
     * TODO
     */
    private class RangeNode extends AreaNode implements Graphics {
        /**
         * TODO
         */
        private final ImageGraphics nodeSprite;

        /**
         * TODO
         *
         * @param coordinates
         * @param hasLeftEdge
         * @param hasUpEdge
         * @param hasRightEdge
         * @param hasDownEdge
         */
        private RangeNode(DiscreteCoordinates coordinates, boolean hasLeftEdge, boolean hasUpEdge, boolean hasRightEdge, boolean hasDownEdge) {
            // TODO comments

            super(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);

            if (!hasUpEdge && !hasRightEdge && !hasDownEdge && !hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(3 * 18, 5 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (!hasUpEdge && hasRightEdge && hasDownEdge && !hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(0 * 18, 5 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (!hasUpEdge && hasRightEdge && hasDownEdge && hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(1 * 18, 5 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (!hasUpEdge && !hasRightEdge && hasDownEdge && hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(2 * 18, 5 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (hasUpEdge && !hasRightEdge && hasDownEdge && hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(2 * 18, 6 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (hasUpEdge && !hasRightEdge && !hasDownEdge && hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(2 * 18, 7 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (hasUpEdge && hasRightEdge && !hasDownEdge && hasLeftEdge) {
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(1 * 18, 7 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500);
            } else if (hasUpEdge && hasRightEdge && !hasDownEdge && !hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(0 * 18, 7 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else if (hasUpEdge && hasRightEdge && hasDownEdge && !hasLeftEdge) {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(0 * 18, 6 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            } else {
                nodeSprite = new ImageGraphics(
                    ResourcePath.getSprite("icwars/UIpackSheet"),
                    1f,
                    1f,
                    new RegionOfInterest(1 * 18, 6 * 18, 16, 16),
                    coordinates.toVector(),
                    0.6f,
                    500
                );
            }
        }

        /**
         * TODO
         *
         * @param canvas target, not null
         */
        @Override
        public void draw(Canvas canvas) {
            nodeSprite.draw(canvas);
        }
    }
}
