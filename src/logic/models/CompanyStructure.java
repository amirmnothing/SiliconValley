package logic.models;

import java.io.Serializable;

public abstract class CompanyStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Player owner;

    public CompanyStructure(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }


    public abstract void produce(Sector sector);

    public abstract int getVictoryPoints();
}
