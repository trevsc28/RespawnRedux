package net.njfc.respawnredux.client.manager;

import javafx.scene.canvas.GraphicsContext;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.Layer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Layer stack implementation
 *
 * @author John Siyaga
 * @version 1.0.0
 * @since 6/11/2017
 */
public class LayerManager {

    private GameRuntime runtime;
    private Stack<Layer> layers;

    public LayerManager(GameRuntime runtime) {
        this.runtime = runtime;
        this.layers = new Stack<>();
    }

    /**
     * Tick this layer and call the update and render handlers
     */
    public void tick(GraphicsContext gfx) {
        new ArrayList<>(layers).forEach(layer -> {
            layer.update(runtime);
            layer.render(runtime, gfx);
        });
    }

    /**
     * Push a new layer onto the stack
     *
     * @param layer Layer
     */
    public void push(Layer layer) {
        layer.register(runtime);
        this.layers.push(layer);
    }

    /**
     * Pop the top layer off the stack
     *
     * @return Popped layer
     */
    public Layer pop() {
        Layer ejected = this.layers.pop();
        ejected.unregister(runtime);
        return ejected;
    }

    /**
     * Replace the topmost layer with a new layer
     *
     * @param layer New layer
     * @return Previous top layer
     */
    public Layer replace(Layer layer) {
        Layer ejected = pop();
        push(layer);
        return ejected;
    }

}
