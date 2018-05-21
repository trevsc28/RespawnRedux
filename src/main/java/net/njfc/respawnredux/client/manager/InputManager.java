package net.njfc.respawnredux.client.manager;

import javafx.scene.input.KeyCode;
import net.njfc.respawnredux.client.GameRuntime;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/21/2018
 */
public class InputManager {

    private Set<KeyCode> activeKeys;

    public InputManager(GameRuntime runtime) {
        this.activeKeys = new HashSet<>();

        // Register handlers
        runtime.getScene().setOnKeyPressed(e -> activeKeys.add(e.getCode()));

        runtime.getScene().setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    /**
     * Returns true when the specified KeyCode is currently pressed
     *
     * @param key KeyCode to check
     * @return boolean
     */
    public boolean isKeyPressed(KeyCode key) {
        return this.activeKeys.contains(key);
    }

}
