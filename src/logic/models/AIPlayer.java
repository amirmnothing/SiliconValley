package logic.models;

import logic.engine.AIBrain;
import logic.engine.GameEngine;
import logic.enums.PlayerRole;
import ui.controller.GameBoardController;

import java.util.ArrayList;

public class AIPlayer extends Player implements PlayableAI {
    private final AIBrain brain;

    public AIPlayer(int id, GameBoardController controller) {
        super("AI Player" + id, new ArrayList<>());
        this.playerRole = null;
        this.brain = new AIBrain(controller);
    }

    @Override
    public void playTurn(GameEngine engine, Runnable onComplete) {
        brain.executeTurn(this, engine, onComplete);
    }
    public AIBrain getBrain() {
        return brain;
    }
}
