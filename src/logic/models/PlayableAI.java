package logic.models;

import logic.engine.AIBrain;
import logic.engine.GameEngine;

public interface PlayableAI {

    void playTurn(GameEngine engine,Runnable onComplete);

    AIBrain getBrain();
}
