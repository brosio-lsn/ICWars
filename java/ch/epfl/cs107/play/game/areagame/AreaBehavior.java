package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.game.actor.Draggable;
import ch.epfl.cs107.play.game.actor.Droppable;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * AreaBehavior is a basically a map made of Cells. Those cells are used for the game behavior
 * Note: implementation from Interactable.Listener not expected from students
 */

public abstract class AreaBehavior implements Interactable.Listener, Interactor.Listener {

    /// The behavior is an Image of size height x width
    /**
     * TODO
     */
    private final Image behaviorMap;

    /**
     * TODO
     */
    private final int width, height;

    /// We will convert the image into an array of cells
    /**
     * TODO
     */
    private final Cell[][] cells;

    /**
     * TODO
     * <p>
     * Default AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public AreaBehavior(Window window, String name) {
        // TODO comments

        // Load the image
        //System.out.println(ResourcePath.getBehavior(name));
        behaviorMap = window.getImage(ResourcePath.getBehavior(name), null, false);
        // Get the corresponding dimension and init the array
        height = behaviorMap.getHeight();
        width = behaviorMap.getWidth();
        cells = new Cell[width][height];
    }

    /**
     * TODO
     *
     * @param draggable
     * @param mouseCoordinates
     */
    public void dropInteractionOf(Draggable draggable, DiscreteCoordinates mouseCoordinates) {
        if (mouseCoordinates.x >= 0
            && mouseCoordinates.y >= 0
            && mouseCoordinates.x < width
            && mouseCoordinates.y < height
        ) cells[mouseCoordinates.x][mouseCoordinates.y].dropInteractionOf(draggable);
    }

    /// AreaBehavior implements Interactor.Listener

    /**
     * TODO
     *
     * @param interactor (Interactor). Not null
     */
    @Override
    public void cellInteractionOf(Interactor interactor) {
        // TODO comments

        interactor.getCurrentCells()
            .stream()
            .filter(dc -> dc.x >= 0)
            .filter(dc -> dc.y >= 0)
            .filter(dc -> dc.x < width)
            .filter(dc -> dc.y < height)
            .forEach(dc -> cells[dc.x][dc.y].cellInteractionOf(interactor));
    }

    /**
     * TODO
     *
     * @param interactor (Interactor). Not null
     */
    @Override
    public void viewInteractionOf(Interactor interactor) {
        // TODO comments

        interactor.getFieldOfViewCells()
            .stream()
            .filter(dc -> dc.x >= 0)
            .filter(dc -> dc.y >= 0)
            .filter(dc -> dc.x < width)
            .filter(dc -> dc.y < height)
            .forEach(dc -> cells[dc.x][dc.y].viewInteractionOf(interactor));
    }

    /**
     * TODO
     *
     * @param x
     * @param y
     * @param cell
     */
    protected void setCell(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    /**
     * TODO
     *
     * @param x
     * @param y
     * @return
     */
    protected Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * TODO
     *
     * @param r
     * @param c
     * @return
     */
    protected int getRGB(int r, int c) {
        return behaviorMap.getRGB(r, c);
    }

    /**
     * TODO
     *
     * @return
     */
    protected int getHeight() {
        return height;
    }

    /**
     * TODO
     *
     * @return
     */
    protected int getWidth() {
        return width;
    }


    /// AreaBehavior implements Interactable.Listener

    /**
     * TODO
     *
     * @param entity      (Interactable). Not null
     * @param coordinates (List of DiscreteCoordinates). Not null
     * @return
     */
    @Override
    public boolean canLeave(Interactable entity, List<DiscreteCoordinates> coordinates) {
        // TODO comments

        return coordinates.stream()
            .noneMatch(c -> (c.x < 0)
                || (c.y < 0)
                || (c.x >= width)
                || (c.y >= height)
                || !cells[c.x][c.y].canLeave(entity));
    }

    /**
     * TODO
     *
     * @param entity      (Interactable). Not null
     * @param coordinates (List of DiscreteCoordinates). Not null
     * @return
     */
    @Override
    public boolean canEnter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        // TODO comments

        return coordinates.stream()
            .noneMatch(c -> (c.x < 0)
                || (c.y < 0)
                || (c.x >= width)
                || (c.y >= height)
                || !cells[c.x][c.y].canEnter(entity));
    }

    /**
     * TODO
     *
     * @param entity      (Interactable). Not null
     * @param coordinates (List of DiscreteCoordinates). Not null
     */
    @Override
    public void leave(Interactable entity, List<DiscreteCoordinates> coordinates) {
        coordinates.forEach(c -> cells[c.x][c.y].leave(entity));
    }

    /**
     * TODO
     *
     * @param entity      (Interactable). Not null
     * @param coordinates (List of DiscreteCoordinates). Not null
     */
    @Override
    public void enter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        coordinates.forEach(c -> cells[c.x][c.y].enter(entity));
    }

    /**
     * TODO
     * <p>
     * Each AreaGame will have its own Cell extension.
     * At minimum a cell is linked to its content
     */
    public abstract static class Cell implements Interactable {

        /// Content of the cell as a set of Interactable
        /**
         * TODO
         */
        protected Set<Interactable> entities;
        /**
         * TODO
         */
        protected DiscreteCoordinates coordinates;


        /**
         * TODO
         * <p>
         * Default Cell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        public Cell(int x, int y) {
            entities = new HashSet<>();
            coordinates = new DiscreteCoordinates(x, y);
        }

        /**
         * TODO
         * <p>
         * Do the given draggableAreaEntity interacts with all Droppable sharing the same cell
         * // @param interactor (Interactor), not null
         */
        private void dropInteractionOf(Draggable draggable) {
            // TODO comments

            entities.stream()
                .filter(interactable -> (interactable instanceof Droppable droppable) && droppable.canDrop())
                .map(Droppable.class::cast)
                .forEach(droppable -> droppable.receiveDropFrom(draggable));
            if ((this instanceof Droppable droppable) && droppable.canDrop())
                droppable.receiveDropFrom(draggable);

        }

        /**
         * TODO
         * <p>
         * Do the given interactor interacts with all Interactable sharing the same cell
         *
         * @param interactor (Interactor), not null
         */
        private void cellInteractionOf(Interactor interactor) {
            // TODO comments

            interactor.interactWith(this);
            entities.stream()
                .filter(Interactable::isCellInteractable)
                .forEach(interactor::interactWith);
        }

        /**
         * TODO
         * <p>
         * Do the given interactor interacts with all Interactable sharing the same cell
         *
         * @param interactor (Interactor), not null
         */
        private void viewInteractionOf(Interactor interactor) {
            // TODO comments

            interactor.interactWith(this);
            entities.stream()
                .filter(Interactable::isViewInteractable)
                .forEach(interactor::interactWith);
        }

        /**
         * TODO
         * <p>
         * Do the given interactable enter into this Cell
         *
         * @param entity (Interactable), not null
         */
        protected void enter(Interactable entity) {
            entities.add(entity);
        }

        /**
         * TODO
         * <p>
         * Do the given Interactable leave this Cell
         *
         * @param entity (Interactable), not null
         */
        protected void leave(Interactable entity) {
            entities.remove(entity);
        }

        /**
         * TODO
         * <p>
         * Indicate if the given Interactable can leave this Cell
         *
         * @param entity (Interactable), not null
         * @return (boolean): true if entity can leave
         */
        protected abstract boolean canLeave(Interactable entity);

        /**
         * TODO
         * <p>
         * Indicate if the given Interactable can enter this Cell
         *
         * @param entity (Interactable), not null
         * @return (boolean): true if entity can enter
         */
        protected abstract boolean canEnter(Interactable entity);

        /// Cell implements Interactable

        /**
         * TODO
         *
         * @return
         */
        @Override
        public boolean takeCellSpace() {
            return false;
        }

        /**
         * TODO
         *
         * @param coordinates left cell coordinates
         */
        @Override
        public void onLeaving(List<DiscreteCoordinates> coordinates) {
        }

        /**
         * TODO
         *
         * @param coordinates entered cell coordinates
         */
        @Override
        public void onEntering(List<DiscreteCoordinates> coordinates) {
        }

        /**
         * TODO
         *
         * @return
         */
        @Override
        public List<DiscreteCoordinates> getCurrentCells() {
            return Collections.singletonList(coordinates);
        }
    }
}
