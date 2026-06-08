package logic.models;

import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Player {
    protected Map<ResourceType, Integer> resources = new HashMap<>();
    protected List<CompanyStructure> companies;
    protected PlayerRole playerRole;
    protected boolean hasLongestNetwork = false;//یک flag برای بلندترین مسیر

    public Player(List<CompanyStructure> companies) {
        resources = new HashMap<>();
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
        this.companies = companies != null ? companies : new ArrayList<>();
        this.playerRole = null;
    }


    // محاسبه کل امتیاز برای پاداش بلند ترین شبکه
    public int calculateVictoryPoints() {
        int totalPoints = 0;
        if (companies != null) {
            for (CompanyStructure comp : companies) {
                totalPoints += comp.getVictoryPoints();
            }
        }
        if (hasLongestNetwork) totalPoints += 2;

        totalPoints -= getRolePenalty();

        return totalPoints;

    }

    public int calculateMarketPrice(ResourceType resource, int currentMarketPrice) {
        return currentMarketPrice;
    }

    //سقف تعداد کارت های منبع بازیکن برای فرار از مالیات
    public int getCrisisModifierThreshold() {
        return 7;
    }

    public int getUpgradeCloudDiscount() {
        return 0;
    }

    //جریمه برای انتخاب نقش
    public int getRolePenalty() {
        return 0; // بازیکن معمولی هیچ جریمه امتیازی ندارد
    }

    public void deductResourcesForUnicornUpgrade() {
        if (hasResourcesForUnicornUpgrade()) {
            deductResource(ResourceType.CLOUD, 2 - getUpgradeCloudDiscount());
            deductResource(ResourceType.DATA, 3);
        }
    }


    public void addResource(ResourceType type, int count) {
        resources.put(type, resources.getOrDefault(type, 0) + count);
    }

    public void deductResource(ResourceType type, int count) {
        int currentCount = resources.getOrDefault(type, 0);
        int newCount = currentCount - count;
        if (newCount < 0) {
            // TODO : Show error : not enough resources to deduct
            return;
        } else {
            resources.put(type, newCount);
        }
    }

    public void addCompanyStructure(CompanyStructure structure) {
        if (this.companies == null) this.companies = new ArrayList<>();
        this.companies.add(structure);
    }

    public void removeCompanyStructure(CompanyStructure structure) {
        if (this.companies != null) this.companies.remove(structure);
    }

    public boolean hasResourcesForPartnership() {
        return resources.getOrDefault(ResourceType.PATENT, 0) > 0 &&
                resources.getOrDefault(ResourceType.CAPITAL, 0) > 0;
    }

    public boolean hasResourcesForMVP() {
        return resources.getOrDefault(ResourceType.CAPITAL, 0) > 0 &&
                resources.getOrDefault(ResourceType.TALENT, 0) > 0 &&
                resources.getOrDefault(ResourceType.CLOUD, 0) > 0 &&
                resources.getOrDefault(ResourceType.DATA, 0) > 0;
    }

    public boolean hasResourcesForUnicornUpgrade() {
        return resources.getOrDefault(ResourceType.CLOUD, 0) >= 2 - getUpgradeCloudDiscount()
                && resources.getOrDefault(ResourceType.DATA, 0) >= 3;
    }

    public void deductResourcesForPartnership() {
        if (hasResourcesForPartnership()) {
            deductResource(ResourceType.CAPITAL, 1);
            deductResource(ResourceType.PATENT, 1);
        } else {
            // TODO : Show error : not enough resources
        }
    }

    public void deductResourcesForMVP() {
        if (hasResourcesForMVP()) {
            deductResource(ResourceType.CAPITAL, 1);
            deductResource(ResourceType.TALENT, 1);
            deductResource(ResourceType.CLOUD, 1);
            deductResource(ResourceType.DATA, 1);
        } else {
            // TODO : Show error : not enough resources
        }
    }

    public Map<ResourceType, Integer> getResourceCount() {
        return resources;
    }

    public void setResourceCount(Map<ResourceType, Integer> resourceCount) {
        this.resources = resourceCount;
    }

    public List<CompanyStructure> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyStructure> companies) {
        this.companies = companies;
    }

    public PlayerRole getRole() {
        return playerRole;
    }

    public void setRole(PlayerRole role) {
        this.playerRole = role;
    }

    public boolean isHasLongestNetwork() {
        return hasLongestNetwork;
    }

    public void setHasLongestNetwork(boolean hasLongestNetwork) {
        this.hasLongestNetwork = hasLongestNetwork;
    }
}
