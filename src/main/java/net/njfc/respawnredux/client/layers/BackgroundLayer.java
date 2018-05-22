package net.njfc.respawnredux.client.layers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.Layer;
import net.njfc.respawnredux.client.util.Loader;

public class BackgroundLayer implements Layer {
    private Image background;


    @Override
    public void update(GameRuntime runtime) {

    }

    @Override
    public void render(GameRuntime runtime, GraphicsContext gfx) {
        gfx.drawImage(background, 0, 0);
    }

    @Override
    public void register(GameRuntime runtime) {
        this.background = Loader.image("img/background.png");
    }

    @Override
    public void unregister(GameRuntime runtime) {

    }
}
