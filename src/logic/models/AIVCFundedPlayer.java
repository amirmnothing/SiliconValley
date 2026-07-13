package logic.models;

import logic.engine.AIBrain;
import logic.engine.GameEngine;
import logic.enums.PlayerRole;
import ui.controller.GameBoardController;

import java.util.ArrayList;

public class AIVCFundedPlayer extends VCFundedPlayer implements PlayableAI {
    private final AIBrain brain;

    public AIVCFundedPlayer(String name, GameBoardController controller) {
        super(name + " AI", new ArrayList<>());
        this.brain = new AIBrain(controller);
        this.playerRole = PlayerRole.THE_VC_FUNDED;
    }

    @Override
    public void playTurn(GameEngine engine, Runnable onComplete) {
        brain.executeTurn(this, engine, onComplete);
    }

    public AIBrain getBrain() {
        return brain;
    }

    public boolean isAI() {
        return true;
    }

    public void setController(GameBoardController controller) {
        this.brain.setController(controller);
    }
}
