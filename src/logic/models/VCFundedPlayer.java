package logic.models;

import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.List;

public class VCFundedPlayer extends Player{

    public VCFundedPlayer(List<CompanyStructure> companies) {
        super(companies);
        this.playerRole=PlayerRole.THE_VC_FUNDED;
        this.addResource(ResourceType.CAPITAL, 2); //2 واحد سرمایه بیشتر در شروع بازی
    }

    @Override
    public int getCrisisModifierThreshold() {
        return 9; // سقف تعداد کارت برای فرار از مالیات از 7 به 9 رسید
    }
    @Override
    public int getRolePenalty() {
        return 1;
    }
}
