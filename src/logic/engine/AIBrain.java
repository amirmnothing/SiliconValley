package logic.engine;

import logic.models.Player;
import ui.controller.GameBoardController;

public interface AIBrain {
    void executeTurn(Player aiPlayer, GameEngine engine, Runnable onTurnComplete);

    public java.util.Map<logic.enums.ResourceType, Integer> calculateResourcesToDiscard(Player player);
}