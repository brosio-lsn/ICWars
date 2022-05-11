package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.City;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.Arrays;

/**
 * TODO
 */
public class ICWarsBehavior extends AreaBehavior {
    /**
     * TODO
     * <p>
     * Default Tuto2Behavior Constructor
     *
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */
    public ICWarsBehavior(Window window, String name) {
        // TODO comments

        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                setCell(
                    x,
                    y,
                    new ICWarsCell(x, y, ICWarsCellType.toType(getRGB(height - 1 - y, x)))
                );
    }

    /**
     * TODO
     */
    public enum ICWarsCellType {
        // https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NONE(0x0, 0), // Should never be used except in the toType method
        ROAD(0xff_00_00_00, 0), // the second value is the number of defense stars
        PLAIN(0xff_28_a7_45, 1),
        WOOD(0xff_ff_00_00, 3),
        RIVER(0xff_00_00_ff, 0),
        MOUNTAIN(0xff_ff_ff_00, 4),
        CITY(0xff_ff_ff_ff, 2);

        /**
         * TODO
         */
        final int type;

        /**
         * TODO
         */
        final int numberOfStars;

        /**
         * TODO
         *
         * @param type
         * @param numberOfStars
         */
        ICWarsCellType(int type, int numberOfStars) {
            this.type = type;
            this.numberOfStars = numberOfStars;
        }


        /**
         * TODO
         *
         * @param type
         * @return
         */
        public static ICWarsBehavior.ICWarsCellType toType(int type) {
            // TODO comments

            return Arrays.stream(ICWarsCellType.values()) // for each cell type
                .filter(ict -> ict.type == type) // if it's the type we're looking for
                .findFirst() // get the first one, and return it
                .orElse(NONE); // if there isn't one, return `NONE`
        }

        /**
         * TODO
         *
         * @return
         */
        public int getDefenseStar() {
            return numberOfStars;
        }

        public String typeToString() {
            return String.valueOf(toType(this.type));
        }
    }

    /**
     * @param area
     */
    public void registerCities (Area area){
        for(int x = 0; x<this.getWidth();++x){
            for(int y = 0; y<this.getHeight();++y){
                Cell cell = this.getCell(x,y);
                if(((ICWarsCell) cell).getType().typeToString().equals("CITY")){
                    area.registerActor(new City(area, new DiscreteCoordinates(x,y), ICWarsActor.Faction.NEUTRAL));
                }
            }
        }
    }

    /**
     * TODO
     * <p>
     * Cell adapted to the Tuto2 game
     */
    public static class ICWarsCell extends AreaBehavior.Cell implements Interactable {

        /**
         * TODO
         */
        private final int numberOfStarsz;

        /**
         * TODO
         */
        private final ICWarsCellType type;

        /**
         * TODO
         * <p>
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (UnknownCellType), not null
         */
        public ICWarsCell(int x, int y, ICWarsBehavior.ICWarsCellType type) {
            super(x, y);
            /// Type of the cell following the enum
            this.type = type;
            this.numberOfStarsz = type.numberOfStars;
        }

        /**
         * TODO
         *
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         * TODO
         *
         * @return
         */
        public int getNumberOfStars() {
            return this.numberOfStarsz;

        }

        /**
         * TODO
         *
         * @return
         */
        public ICWarsCellType getType() {
            return this.type; // ROAD, NONE...
        }

        /**
         * TODO
         *
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            // TODO comments

            // if the entity takes Cell Space else it can enter
            // if another entity on the cell also takes the space entity can't enter
            return !entity.takeCellSpace()
                || entities.stream().noneMatch(Interactable::takeCellSpace);
        }


        /**
         * TODO
         *
         * @return
         */
        @Override
        public boolean isCellInteractable() {
            return true;
        }

        /**
         * TODO
         *
         * @return
         */
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
    }
}
