package logic.models;

import logic.enums.ResourceType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Unicorn extends CompanyStructure {
    public static final Map<ResourceType, Integer> UPGRADE_COST;
    static {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.DATA, 3);
        cost.put(ResourceType.CLOUD, 2);
        UPGRADE_COST = Collections.unmodifiableMap(cost);
    }

    public Unicorn(Player owner) {
        super(owner);
    }


    @Override
    public void produce(Sector sector) {
        if (sector.isAuditor()) return;
        ResourceType producedResource = sector.getResourceType();
        if (producedResource == null) return;
        this.getOwner().addResource(producedResource, 2);
    }

    @Override
    public int getVictoryPoints() {
        return 2;
    }
}
