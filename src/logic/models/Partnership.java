package logic.models;

import logic.enums.ResourceType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Partnership {
    private final Player owner;

    public static final Map<ResourceType, Integer> CONSTRUCTION_COST;
    static {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.CAPITAL, 1);
        cost.put(ResourceType.PATENT, 1);
        CONSTRUCTION_COST = Collections.unmodifiableMap(cost);
    }

    public Partnership(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
