package exception;

import logic.enums.ResourceType;
import logic.models.Player;
import java.util.Map;

public class InsufficientResourcesException extends RuntimeException {
    private final Player player;
    private final Map<ResourceType, Integer> resources;

    public InsufficientResourcesException(Player player,String message, Map<ResourceType, Integer> resources) {
        super(message);
        this.player=player;
        this.resources=resources;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }



}
