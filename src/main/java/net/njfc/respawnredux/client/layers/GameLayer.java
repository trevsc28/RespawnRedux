package net.njfc.respawnredux.client.layers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.Layer;
import net.njfc.respawnredux.client.SpriteAnimation;
import net.njfc.respawnredux.client.impl.Obstacle;
import net.njfc.respawnredux.client.impl.Platform;
import net.njfc.respawnredux.client.impl.Player;
import net.njfc.respawnredux.client.impl.obstacles.spikeObstacle;
import net.njfc.respawnredux.client.util.Collision;
import net.njfc.respawnredux.client.util.Position;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLayer implements Layer {

    protected Player p;
    protected boolean jumped = false, applyGravity = false;
    protected int collided = 0;

    private SpriteAnimation animation;

    private Set<Obstacle> obstacles;
    private Set<Platform> platforms;

    public GameRuntime runtime;

    @Override
    public void update(GameRuntime runtime) {

        // Player control
        if(runtime.getInput().isKeyPressed(KeyCode.RIGHT)) {
            p.motion -= .9;
        }
        else if(runtime.getInput().isKeyPressed(KeyCode.LEFT)) {
            p.motion += .9;
        }

        if(runtime.getInput().isKeyPressed(KeyCode.UP)) {
            if(!jumped) {
                jump();
            }
        }

        // Collision

        // PLATFORM COLLISION
        for(Platform pl : platforms) {
            // TODO: Platform collision (needs separate collision for sides, add necessary methods in Platform class)

            // Temp collision
            if(!jumped) {
                if (p.getBounds().intersects(pl.getBounds())) {
                    collided++;
                    p.velocity = 0;
                }
            }
            else {
                if(p.getBounds().intersects(pl.getBounds())) {
                    jumped = false;
                    collided++;
                }
            }
        }

        if(collided == 0)
            p.velocity -= p.gravity;

        collided = 0;

        p.setPosition(new Position(p.getPosition().x, p.getPosition().y + p.velocity));


        // OBSTACLE COLLISION
        for(Obstacle o : obstacles) {
            if(Collision.squareCheck(o.getBounds(), p.getPosition())) {
                p.kill();
            }
        }

    }

    public void jump() {
        p.velocity = -20;
        jumped = true;
    }

    @Override
    public void render(GameRuntime runtime, GraphicsContext gfx) {

        // Player
        gfx.drawImage(p.getTexture(), p.getPosition().x, p.getPosition().y);

        // Platforms
        for(Platform pl : platforms) {
            gfx.drawImage(pl.texture, pl.position.x, pl.position.y);
        }

        // Obstacles
        for(Obstacle o : obstacles) {
            gfx.drawImage(o.getTexture(), o.position.x, o.position.y);
        }
    }

    @Override
    public void register(GameRuntime runtime) {
        this.p = new Player(runtime, new Position(50, 200));

        // Add platforms to the level
        platforms = Stream.of(
                new Platform(runtime, new Position(0, 600)),
                new Platform(runtime, new Position(200, 600))
        ).collect(Collectors.toSet());

        // Add obstacles to the level
        obstacles = Stream.of(
                new Obstacle(runtime, new Position(100, 625)),
                new spikeObstacle(runtime, new Position(300, 625))
        ).collect(Collectors.toSet());
    }

    @Override
    public void unregister(GameRuntime runtime) {

    }
}
