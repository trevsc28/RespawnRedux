package net.njfc.respawnredux.client.impl.obstacles;

import javafx.scene.image.Image;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.impl.Obstacle;
import net.njfc.respawnredux.client.util.Loader;
import net.njfc.respawnredux.client.util.Position;

public class spikeObstacle extends Obstacle {

    public Image texture;

    public spikeObstacle(GameRuntime runtime, Position p) {
        super(runtime, p);
        this.texture = Loader.image("img/spikeObstacle.png");
    }

    public Image getTexture() { return texture; }

}
