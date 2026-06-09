package logic.models;

import logic.enums.ResourceType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MVP extends CompanyStructure {
    public static final Map<ResourceType, Integer> CONSTRUCTION_COST;
    static {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.CAPITAL, 1);
        cost.put(ResourceType.TALENT, 1);
        cost.put(ResourceType.CLOUD, 1);
        cost.put(ResourceType.DATA, 1);
        CONSTRUCTION_COST = Collections.unmodifiableMap(cost);
    }

    public MVP(Player owner) {
        super(owner);
    }

    @Override
    public void produce(Sector sector) {
        if (sector.isAuditor()) return;
        ResourceType producedResource = sector.getResourceType();
        if (producedResource == null) return;
        this.getOwner().addResource(producedResource, 1);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }
}
