package Logic.Models;
import Logic.Enums.CornerDirection;
import Logic.Enums.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sector {
    private ResourceType resourceType;
    private int diceNumber;
    private boolean isInspector;
    private final Map<CornerDirection, Vertex> corners;

    public Sector(ResourceType resourceType, int diceNumber, boolean isInspector) {
        this.resourceType = resourceType;
        this.diceNumber = diceNumber;
        this.isInspector = isInspector;
        this.corners = new HashMap<>();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
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
