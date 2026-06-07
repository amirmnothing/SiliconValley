package logic.models;

import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.List;

public class HackerCEOPlayer extends  Player {

    public HackerCEOPlayer(List<CompanyStructure> companies) {
        super(companies);
        this.playerRole=PlayerRole.THE_HACKER_CEO;
    }

    @Override
    public int calculateMarketPrice(ResourceType resource, int currentMarketPrice) {

        return Math.max(currentMarketPrice-1, 2);
    }
    @Override
    public int getRolePenalty() {
        return 1;
    }

}
