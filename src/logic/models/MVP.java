package logic.models;

import logic.enums.ResourceType;

public class MVP extends CompanyStructure {
    public MVP(Player owner) {
        super(owner);
    }


    @Override
    public void produce(Sector sector) {
        if (sector.isInspector()) return;
        ResourceType producedResource = sector.getResourceType();

        if (producedResource == null) return;

        this.getOwner().addResource(producedResource, 1);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }
}
