package logic.models;
import logic.enums.CornerDirection;
import logic.enums.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class Sector {
    private ResourceType resourceType;
    private int activationNumber;
    private boolean isInspector;
    private final Map<CornerDirection, Vertex> corners;

    public Sector(ResourceType resourceType, int activationNumber, boolean isInspector) {
        this.resourceType = resourceType;
        this.activationNumber = activationNumber;
        this.isInspector = isInspector;
        this.corners = new HashMap<>();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getactivationNumber() {
        return activationNumber;
    }

    public void setactivationNumber(int activationNumber) {
        this.activationNumber = activationNumber;
    }

    public boolean isInspector() {
        return isInspector;
    }

    public void setInspector(boolean inspector) {
        isInspector = inspector;
    }

    public void setCorner(CornerDirection cornerDirection, Vertex vertex) {
        if (cornerDirection != null && vertex != null)
            this.corners.put(cornerDirection, vertex);
    }

    public Vertex getCorner(CornerDirection cornerDirection) {
        return this.corners.get(cornerDirection);
    }
}
