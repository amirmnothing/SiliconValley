package logic.models;

import logic.engine.AIBrain;
import logic.engine.GameEngine;
import logic.enums.PlayerRole;
import ui.controller.GameBoardController;

import java.util.ArrayList;

public class AIHackerCEOPlayer extends HackerCEOPlayer implements PlayableAI  {
  private final AIBrain brain;
    public AIHackerCEOPlayer(int id, GameBoardController controller) {
        super("AI Player"+id,new ArrayList<>());
        this.brain = new AIBrain( controller);
        this.playerRole=PlayerRole.THE_HACKER_CEO;

    }

    @Override
    public void playTurn(GameEngine engine,Runnable onComplete) {
        brain.executeTurn(this, engine, onComplete);
    }

    public AIBrain getBrain() {
        return brain;
    }
}
