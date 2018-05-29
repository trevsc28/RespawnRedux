package net.njfc.respawnredux.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/21/2018
 */
public class Util {

	/**
	 * Schedule code to run after the specified amount of time
	 *
	 * @param duration Timeout duration
	 * @param cb Callback
	 */
	public static void setTimeout(long duration, Runnable cb) {
		Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(duration),
			ae -> cb.run()));
		timeline.play();
	}

}
