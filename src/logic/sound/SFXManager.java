package logic.sound;

import exception.SFXNotFoundException;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SFXManager {

    private static final Map<String, AudioClip> sfxCache = new HashMap<>();

    private static double sfxVolume = 1.0;

    public static void play(String fileName) {
        AudioClip clip = sfxCache.get(fileName);

        if (clip == null) {
            URL resource = SFXManager.class.getResource("/assets/Sounds/SFX Sounds/" + fileName);

            if (resource == null) {
                throw new SFXNotFoundException(fileName);
            }

            try {
                clip = new AudioClip(resource.toExternalForm());
                sfxCache.put(fileName, clip);
            } catch (Exception e) {
                System.err.println("[SFX ERROR] " + fileName + " -> " + e.getMessage());
                return;
            }
        }

        clip.play(sfxVolume);
    }

    public static void preload(String... fileNames) {
        for (String fileName : fileNames) {
            if (sfxCache.containsKey(fileName)) continue;
            URL resource = SFXManager.class.getResource("/assets/Sounds/SFX Sounds/" + fileName);
            if (resource != null) {
                try {
                    sfxCache.put(fileName, new AudioClip(resource.toExternalForm()));
                } catch (Exception ignored) {
                }
            } else {
                throw new SFXNotFoundException(fileName);
            }
        }
    }

    public static void setVolume(double volume) {
        if (volume >= 0.0 && volume <= 1.0) {
            sfxVolume = volume;
        }
    }

    public static double getVolume() {
        return sfxVolume;
    }
}