package Logic.Models;
import Logic.Enums.ResourceType;

public class Sector {
    private ResourceType resources;
    private int diceNumber;
    private boolean isInspector;

    public Sector(ResourceType resources, int diceNumber, boolean isInspector) {
        this.resources = resources;
        this.diceNumber = diceNumber;
        this.isInspector = isInspector;
    }

    public ResourceType getResources() {
        return resources;
    }

    public void setResources(ResourceType resources) {
        this.resources = resources;
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
}
