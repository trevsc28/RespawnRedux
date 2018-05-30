package net.njfc.respawnredux.client;

import javafx.scene.media.MediaPlayer;

/**
 * @author John Siyaga
 * @version 1.0.0
 * @since 5/21/2018
 */
public class Soundtrack {

	private static final double IDLE_VOLUME = 0.4;
	private static final double GAME_VOLUME = 0.8;
	private static final double FADE_REDUCTION = 0.005;

	private MediaPlayer soundTrackPlayer;
	private boolean active = false;

	/**
	 * Create a new Soundtrack playing the specified MediaPlayer
	 *
	 * @param soundTrackPlayer MediaPlayer
	 */
	public Soundtrack(MediaPlayer soundTrackPlayer) {
		this.soundTrackPlayer = soundTrackPlayer;

		// The soundtrack will start idle
		setIdle();
	}

	public void setIdle() {
		this.soundTrackPlayer.setVolume(IDLE_VOLUME);
	}

	public void setActive() {
		this.soundTrackPlayer.setVolume(GAME_VOLUME);
	}

	public void play() {
		this.soundTrackPlayer.play();
		this.active = true;
	}

	public void loop() {
		this.soundTrackPlayer.setCycleCount(-1);
	}

	public void stop() {
		this.soundTrackPlayer.stop();
		this.active = false;
	}

	/**
	 * Fade out the soundtrack
	 */
	public void fadeOut() {
		double currentVolume = this.soundTrackPlayer.getVolume();

		currentVolume -= FADE_REDUCTION;

		if(currentVolume > 0) {
			this.soundTrackPlayer.setVolume(currentVolume);
			Util.setTimeout(10, this::fadeOut);
		} else {
			stop();
			this.soundTrackPlayer.setVolume(GAME_VOLUME);
		}
	}

	public boolean isActive() {
		return active;
	}
}
