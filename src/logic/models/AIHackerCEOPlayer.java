package logic.models;

import logic.engine.AIBrain;
import logic.engine.MCTSAIBrain;
import logic.engine.SimpleAIBrain;
import logic.engine.GameEngine;
import logic.enums.PlayerRole;
import ui.controller.GameBoardController;

import java.util.ArrayList;

public class AIHackerCEOPlayer extends HackerCEOPlayer implements PlayableAI {
    private final AIBrain brain;

    public AIHackerCEOPlayer(String name, GameBoardController controller,boolean isHardMode) {
        super(name + " AI", new ArrayList<>());
        this.playerRole = PlayerRole.THE_HACKER_CEO;
        if(isHardMode) {
            this.brain = new MCTSAIBrain(controller);
        }else {
            this.brain = new SimpleAIBrain(controller);
        }
    }

    @Override
    public void playTurn(GameEngine engine, Runnable onComplete) {
        brain.executeTurn(this, engine, onComplete);
    }

    public AIBrain getBrain() {
        return  brain;
    }

    public boolean isAI() {
        return true;
    }

    public void setController(GameBoardController controller) {
        SimpleAIBrain.setController(controller);
    }
}
