package ch.epfl.cs107.play.game.icwars.gui;//package ch.epfl.cs107.play.game.icwars.gui;
//

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.icwars.actor.actions.Action;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Shape;
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


/**
 * TODO
 */
public class ICWarsActionsPanel implements Graphics {

    /**
     * TODO
     */
    private final float fontSize;

    /// Sprite and text graphics line
    /**
     * TODO
     */
    private final ShapeGraphics background;

    /**
     * TODO
     */
    private List<Action> actions;

    /**
     * TODO
     */
    private TextGraphics[] actionsText;

    /**
     * TODO
     * Default Dialog Constructor
     *
     * @param cameraScaleFactor
     */
    public ICWarsActionsPanel(float cameraScaleFactor) {
        // TODO comments

        final float height = cameraScaleFactor / 4;
        final float width = cameraScaleFactor / 4;

        fontSize = cameraScaleFactor / ICWarsPlayerGUI.FONT_SIZE;

        Shape rect = new Polygon(0, 0, 0, height, width, height, width, 0);
        background = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, 0.7f, 3000f);
    }

    /**
     * TODO
     */
    private void createActionsText() {
        actionsText = new TextGraphics[actions.size()];
        IntStream.range(0, actions.size())
            .forEach(i -> {
                TextGraphics text = new TextGraphics(
                    actions.get(i).getName(),
                    fontSize,
                    Color.WHITE,
                    null,
                    0.0f,
                    false,
                    false,
                    new Vector(0, -i * 1.25f * fontSize - 0.35f),
                    TextAlign.Horizontal.LEFT,
                    TextAlign.Vertical.MIDDLE,
                    1.0f,
                    3001f
                );
                text.setFontName("Kenney Pixel");
                actionsText[i] = text;
            });
        actionsText[0] = new TextGraphics(
            actions.get(0).getName(),
            fontSize,
            Color.WHITE,
            null,
            0.0f,
            false,
            false,
            new Vector(0, -0 * 1.25f * fontSize - 0.35f),
            TextAlign.Horizontal.LEFT,
            TextAlign.Vertical.MIDDLE,
            1.0f,
            3001f
        );
    }

    /**
     * TODO
     *
     * @param actions
     */
    public void setActions(List<Action> actions) {
        this.actions = actions;
        createActionsText();
    }

    /**
     * TODO
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        // TODO comments

        // Compute width, height and anchor
        float width = canvas.getXScale();
        float height = canvas.getYScale();

        final Transform transform = Transform.I.translated(canvas.getPosition().add(width / 4, height / 4));
        background.setRelativeTransform(transform);
        background.draw(canvas);

        final Transform textTransform = Transform.I.translated(canvas.getPosition().add(width / 4 + .1f, height / 2));
        Arrays.stream(actionsText).forEach(text -> {
            text.setRelativeTransform(textTransform);
            text.draw(canvas);
        });
    }
}
