package net.njfc.respawnredux.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.njfc.respawnredux.client.layers.BackgroundLayer;
import net.njfc.respawnredux.client.layers.MenuLayer;
import net.njfc.respawnredux.client.util.Loader;

/**
 * @author NJFC Team
 * @version 1.0.0
 * @since 5/21/2018
 */
public class RRClient extends Application {

    private static final String TITLE = "Respawn Redux - By NJFC";

    @FXML
    private Canvas cvs;

    private Stage window;
    private Scene view;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        FXMLLoader loader = new FXMLLoader(Loader.resource("window.fxml"));
        loader.setController(this);

        Parent content = loader.load();
        this.window = window;
        this.view = new Scene(content);
        window.setScene(view);

        window.setTitle(TITLE);

        window.initStyle(StageStyle.UNDECORATED);

        view.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                window.close();
            }
        });

        // Make sure to exit the game on close
        window.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        // Load all audio files and open the game when done
        Loader.audioBig("sound/soundtrack.mp3").then(player -> {
            window.show();

            startGame(player);
        });
    }

    public void startGame(MediaPlayer soundtrack) {
        // Start and run the game runtime
        GameRuntime runtime = new GameRuntime(view, window, soundtrack, cvs.getGraphicsContext2D());
        runtime.start();

        // Add essential layers to the stack
        runtime.getLayers().push(new BackgroundLayer());
        runtime.getLayers().push(new MenuLayer());
    }
}
