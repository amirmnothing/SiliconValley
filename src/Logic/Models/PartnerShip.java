package Logic.Models;

public class PartnerShip extends CompanyStructure {
    public PartnerShip(Player owner) {
        super(owner);
    }

    @Override
    public void produce(Sector sector) {
        
    }

    @Override
    public int getVictoryPoints() {
        return 0;
    }
}
