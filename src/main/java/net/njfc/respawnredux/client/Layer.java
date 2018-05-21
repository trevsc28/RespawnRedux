package net.njfc.respawnredux.client;

import javafx.scene.canvas.GraphicsContext;

/**
 * Game Loop class for updating the state and render
 *
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/21/2018
 */
public interface Layer {

    /**
     * Update the game state
     */
    void update(GameRuntime runtime);

    /**
     * Render the current state
     */
    void render(GameRuntime runtime, GraphicsContext gfx);

    /**
     * Called when the layer is inserted into the layer manager
     */
    void register(GameRuntime runtime);

    /**
     * Called when the layer is ejected from the layer manager
     */
    void unregister(GameRuntime runtime);

}
