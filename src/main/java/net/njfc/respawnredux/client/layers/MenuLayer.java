package net.njfc.respawnredux.client.layers;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.njfc.respawnredux.client.GameRuntime;
import net.njfc.respawnredux.client.Layer;
import net.njfc.respawnredux.client.util.Loader;


/**
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/22/2018
 * 
 */
public class MenuLayer implements Layer {

    private static final int BUTTONS = 3;

    private Image logo, play, play_selected, quit, quit_selected, settings, settings_selected; // Maybe replace selected with Continue button?
    private int selectedButton = 0;
    private EventHandler<KeyEvent> keyEvent;

    public MenuLayer(){
        this.logo = Loader.image("img/logo.png");
        this.play = Loader.image("img/play_1.png");
        this.play_selected = Loader.image("img/play_2.png");
        this.quit = Loader.image("img/quit_1.png");
        this.quit_selected = Loader.image("img/quit_2.png");
        this.settings = Loader.image("img/settings_1.png");
        this.settings_selected = Loader.image("img/settings_2.png");
    }

    @Override
    public void update(GameRuntime runtime) {

    }

    @Override
    public void render(GameRuntime runtime, GraphicsContext gfx) {
        // Placeholder background color TODO: Replace with background image
        gfx.setFill(Color.BLACK);
        gfx.fillRect(0, 0, gfx.getCanvas().getWidth(), gfx.getCanvas().getHeight());
        gfx.setFill(Color.SKYBLUE);
        gfx.fillRect(10, 10, gfx.getCanvas().getWidth() - 20, gfx.getCanvas().getHeight() - 20);

        gfx.drawImage(logo, (runtime.getCanvas().getWidth() / 2)- (logo.getWidth() / 2), 90);

        if(selectedButton == 0) {
            gfx.drawImage(play_selected, 283, 400);
        } else {
            gfx.drawImage(play, 283, 400);
        }

        if(selectedButton == 1) {
            gfx.drawImage(settings_selected, 283, 400 + 80);
        } else {
            gfx.drawImage(settings, 283, 400 + 80);
        }

        if(selectedButton == 2) {
            gfx.drawImage(quit_selected, 283, 400 + 160);
        } else {
            gfx.drawImage(quit, 283, 400 + 160);
        }
    }

    @Override
    public void register(GameRuntime runtime) {
        this.keyEvent = e -> {

            if(e.getCode() == KeyCode.UP) {
                if(selectedButton <= 0) selectedButton = 2;
                else selectedButton--;

                buttonBeep();
            }
            if(e.getCode() == KeyCode.DOWN) {
                if(selectedButton >= 2) selectedButton = 0;
                else selectedButton++;

                buttonBeep();
            }
            if(e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
                // TODO: Add layer switching for each button
                if(selectedButton == 0) {
                    runtime.getLayers().replace(new BackgroundLayer());
                    runtime.getLayers().push(new GameLayer());
                }
                if(selectedButton == 1) {
                    //runtime.getLayers().replace(new SettingsLayer());
                }
                if(selectedButton == 2) {
                    runtime.getWindow().close();
                }
                System.out.println("Enter");

                buttonBeep();
            }
        };

        runtime.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent);
    }

    @Override
    public void unregister(GameRuntime runtime) {
        runtime.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyEvent);
    }

    public static void buttonBeep() {
	    Loader.audioSmall("sound/beep.mp3").play();
    }

}
