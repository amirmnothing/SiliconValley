package logic.models;
import logic.enums.CornerDirection;
import logic.enums.ResourceType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Sector implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public boolean isAuditor() {
        return isInspector;
    }

    public void setAuditor(boolean inspector) {
        isInspector = inspector;
    }

    public void setCorner(CornerDirection cornerDirection, Vertex vertex) {
        if (cornerDirection != null && vertex != null)
            this.getCorners().put(cornerDirection, vertex);
    }

    public boolean hasAnyCompanyOnSector(){
        for (CornerDirection cornerDirection : CornerDirection.values()){
            if (this.getCorner(cornerDirection) != null){
                if (this.getCorner(cornerDirection).getCompanyStructure() != null) return true;
            }
        }
        return false;
    }

    public Vertex getCorner(CornerDirection cornerDirection) {
        return this.getCorners().get(cornerDirection);
    }

    public Map<CornerDirection, Vertex> getCorners() {
        return corners;
    }
}
