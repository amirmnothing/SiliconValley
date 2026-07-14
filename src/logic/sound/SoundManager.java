package logic.sound;

import exception.MusicFileNotFoundException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    private static MediaPlayer backgroundMusic;
    public static void playBackgroundMusic(String fileName) {
        try {
            URL resource = SoundManager.class.getResource("/assets/Sounds/" + fileName);
            if (resource == null) {
                throw new MusicFileNotFoundException();
            }

            Media media = new Media(resource.toExternalForm());
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusic.setVolume(1.0);

            backgroundMusic.play();

        } catch (Exception e) {
            throw new MusicFileNotFoundException(e.getMessage());
        }
    }

    public static void setVolume(double volume) {
        if (backgroundMusic != null) {
            if (volume > 0.0 && volume <= 1.0) {
                backgroundMusic.setVolume(volume);
            } else if (volume == 0.0) {
                backgroundMusic.pause();
            }
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public static void pauseMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    public static void resumeMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }
}