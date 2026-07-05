package logic.models;

import logic.enums.PlayerRole;

import java.util.List;

public class TechGuruPlayer extends Player {

    public TechGuruPlayer(String playerName,List<CompanyStructure> companies) {
        super(playerName,companies);
        this.playerRole=PlayerRole.THE_TECH_GURU_CTO;
    }
    @Override
    public int getUpgradeCloudDiscount() {
        return 1; // ۱ واحد تخفیف در Cloud برای ارتقا به Unicorn
    }
    @Override
    public int getRolePenalty() {
        return 1;
    }

}
