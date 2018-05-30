package net.njfc.respawnredux.client.impl;

import javafx.scene.image.Image;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.util.Loader;
import net.njfc.respawnredux.client.util.Position;
import net.njfc.respawnredux.client.util.Rectangle;

/**
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/22/2018
 */
public class Player implements AbstractPlayer {

    public double motion, gravity, velocity;
    private Position position;

    private Image texture;

    public Player(GameRuntime runtime, Position p) {
        // Standard Movement Values
        this.motion = 0;
        this.gravity = -.5;
        this.velocity = 0;
        this.position = p;
        this.texture = Loader.image("img/player.png"); // TODO: Add player image
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
        this.position = p;
    }

    @Override
    public Image getTexture() {
        return texture;
    }

    public void kill() {
        // TODO: Leave body platform object behind at position
    }

    public Rectangle getBounds() {
    	return new Rectangle(
                new Position(position.x, position.y),
                new Position(position.x + (texture.getWidth()), position.y + texture.getHeight())
        );
    }
}