package net.njfc.respawnredux.client.util;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Static utility class for loading various information from disk
 *
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/27/2017
 */
public class Loader {

    /**
     * Returns the URL to the resource on the specified path
     *
     * @param path Resouce path
     * @return URL
     */
    public static URL resource(String path) {
        return Loader.class.getClassLoader().getResource(path);
    }

    /**
     * Returns an InputStream leading to the specified resource
     *
     * @param path Resouce path
     * @return InputStream to resource
     */
    public static InputStream resourceStream(String path) {
        return Loader.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * Load the specified path as an Image
     *
     * @param path Resource path
     * @return Image
     */
    public static Image image(String path) {
        return new Image(resourceStream(path));
    }

    /**
     * Load the specified path as an Image
     *
     * @param path Resource path
     * @return Image
     */
    public static Media sound(String path) {
        try {
            return new Media(resource(path).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the MediaPlayer for the specified resource
     *
     * @param path Path
     * @return MediaPlayer Promise
     */
    public static Promise<MediaPlayer> audioBig(String path) {
        return new Promise<>(prom -> {
            try {
                MediaPlayer player = new MediaPlayer(new Media(resource(path).toURI().toString()));

                player.setOnReady(() -> prom.success(player));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Returns the AudioClip for the specified resource
     *
     * @param path Path
     * @return AudioClip
     */
    public static AudioClip audioSmall(String path) {
        try {
            return new AudioClip(resource(path).toURI().toString());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

}
