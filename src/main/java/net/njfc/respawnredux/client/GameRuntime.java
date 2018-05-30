package net.njfc.respawnredux.client;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import net.njfc.respawnredux.client.manager.InputManager;
import net.njfc.respawnredux.client.manager.LayerManager;

/**
 * Runtime runner for running the game loop.
 *
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/21/2018
 */
public class GameRuntime extends AnimationTimer {

    private LayerManager layers;
    private InputManager input;
    private GraphicsContext ctx;
    private Soundtrack st;
    private Scene scene;
    private Stage window;

    private long tick = 0;

    public GameRuntime(Scene scene, Stage window, MediaPlayer player, GraphicsContext ctx) {
        this.scene = scene;
        this.window = window;
        this.st = new Soundtrack(player);
        this.layers = new LayerManager(this);
        this.input = new InputManager(this);
        this.ctx = ctx;
    }

    @Override
    public void start() {
        super.start();

        // Start the soundtrack as the game starts
        Util.setTimeout(500, () -> this.st.play());
        this.st.loop();
    }

    @Override
    public void handle(long now) {
        // Clear the canvas
        ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());

        // Render the game
        layers.tick(ctx);

        tick++;
    }

    public LayerManager getLayers() {
        return layers;
    }

    public InputManager getInput() {
        return input;
    }

    public GraphicsContext getContext() {
        return ctx;
    }

    public Canvas getCanvas() {
        return ctx.getCanvas();
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getWindow() {
        return window;
    }

    public Soundtrack getSoundtrack() {
        return st;
    }

    public long currentTick() {
        return tick;
    }
}
