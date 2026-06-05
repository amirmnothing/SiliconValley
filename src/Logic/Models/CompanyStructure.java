package Logic.Models;

public abstract class CompanyStructure {
    private final Player owner;
    public CompanyStructure(Player owner){
        this.owner=owner;
    }

    public Player getOwner() {
        return owner;
    }


    public abstract void produce(Sector sector);
    public abstract int getVictoryPoints();
}
