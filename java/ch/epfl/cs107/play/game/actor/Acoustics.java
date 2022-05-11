package ch.epfl.cs107.play.game.actor;

import ch.epfl.cs107.play.window.Audio;


/**
 * TODO
 */
public interface Acoustics {

    /**
     * Play itself on specified Audio context.
     *
     * @param audio (Audio) target, not null
     */
    void bip(Audio audio);
}
