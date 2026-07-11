package logic.save;

import logic.engine.GameEngine;

import java.io.*;

public class SaveManager {
    public static void save(GameEngine gameEngine, File file) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            out.writeObject(gameEngine);
        }
    }

    public static GameEngine load(File file)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (GameEngine) in.readObject();
        }
    }
}
