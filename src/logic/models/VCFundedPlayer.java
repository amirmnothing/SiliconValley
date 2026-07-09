package logic.models;

import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.List;

public class VCFundedPlayer extends Player{
    public static final int DEFAULT_CRISIS_THRESHOLD = 9;
    public VCFundedPlayer(String playerName,List<CompanyStructure> companies) {
        super(playerName,companies);
        this.playerRole=PlayerRole.THE_VC_FUNDED;
        this.addResource(ResourceType.CAPITAL, 2);
    }

    @Override
    public int getCrisisModifierThreshold() {
        return DEFAULT_CRISIS_THRESHOLD;
    }
    @Override
    public int getRolePenalty() {
        return 1;
    }
}
