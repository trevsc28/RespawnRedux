package net.njfc.respawnredux.client.impl;

import javafx.scene.image.Image;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.util.Loader;
import net.njfc.respawnredux.client.util.Position;
import net.njfc.respawnredux.client.util.Rectangle;

public class Platform {

    public Position position;

    public Image texture;

    public Platform(GameRuntime runtime, Position p) {
        this.position = p;
        this.texture = Loader.image("img/platform.png");
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
        this.position = p;
    }

    public Rectangle getBounds() {
        return new Rectangle(
                new Position(position.x - (texture.getWidth() / 2), position.y - texture.getHeight() / 2),
                new Position(position.x + (texture.getWidth() / 2), position.y + texture.getHeight() / 2)
        );
    }
}
