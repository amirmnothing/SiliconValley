package logic.models;

import exception.InsufficientResourcesException;
import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Player {
    public static final int LONGEST_NETWORK_BONUS_POINTS = 2;
    public static final int DEFAULT_CRISIS_THRESHOLD = 7;
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
        if (hasLongestNetwork) totalPoints += LONGEST_NETWORK_BONUS_POINTS;

        totalPoints -= getRolePenalty();

        return totalPoints;

    }

    public int calculateMarketPrice(ResourceType resource, int currentMarketPrice) {
        return currentMarketPrice;
    }

    //سقف تعداد کارت های منبع بازیکن برای فرار از مالیات
    public int getCrisisModifierThreshold() {
        return DEFAULT_CRISIS_THRESHOLD;
    }

    public int getUpgradeCloudDiscount() {
        return 0;
    }

    //جریمه برای انتخاب نقش
    public int getRolePenalty() {
        return 0; // بازیکن معمولی هیچ جریمه امتیازی ندارد
    }

    public void addResource(ResourceType type, int count) {
        resources.put(type, resources.getOrDefault(type, 0) + count);
    }

    public void deductResource(ResourceType type, int count) {
        int currentCount = resources.getOrDefault(type, 0);
        int newCount = currentCount - count;
        if (newCount < 0) {
            Map<ResourceType, Integer> missingResources = new HashMap<>();
            missingResources.put(type, count - currentCount);

            throw new exception.InsufficientResourcesException(this, "Not enough resources of type: " + type, missingResources);
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
        return resources.getOrDefault(ResourceType.PATENT, 0) >= Partnership.CONSTRUCTION_COST.get(ResourceType.PATENT) &&
                resources.getOrDefault(ResourceType.CAPITAL, 0) >= Partnership.CONSTRUCTION_COST.get(ResourceType.CAPITAL);
    }

    public boolean hasResourcesForMVP() {
        return resources.getOrDefault(ResourceType.CAPITAL, 0) >= MVP.CONSTRUCTION_COST.get(ResourceType.CAPITAL) &&
                resources.getOrDefault(ResourceType.TALENT, 0) >= MVP.CONSTRUCTION_COST.get(ResourceType.TALENT) &&
                resources.getOrDefault(ResourceType.CLOUD, 0) >= MVP.CONSTRUCTION_COST.get(ResourceType.CLOUD) &&
                resources.getOrDefault(ResourceType.DATA, 0) >= MVP.CONSTRUCTION_COST.get(ResourceType.DATA);
    }

    public boolean hasResourcesForUnicornUpgrade() {
        return resources.getOrDefault(ResourceType.CLOUD, 0) >= Unicorn.UPGRADE_COST.get(ResourceType.CLOUD) - getUpgradeCloudDiscount()
                && resources.getOrDefault(ResourceType.DATA, 0) >= Unicorn.UPGRADE_COST.get(ResourceType.DATA);
    }

    public void deductResourcesForPartnership() {
        if (hasResourcesForPartnership()) {
            deductResource(ResourceType.CAPITAL, Partnership.CONSTRUCTION_COST.get(ResourceType.CAPITAL));
            deductResource(ResourceType.PATENT, Partnership.CONSTRUCTION_COST.get(ResourceType.PATENT));
        } else {
            Map<ResourceType, Integer> missingResources = new HashMap<>();
            ResourceType[] requiredTypes = {
                    ResourceType.CAPITAL, ResourceType.PATENT
            };
            for (ResourceType type : requiredTypes) {
                int currentAmount = resources.getOrDefault(type, 0);
                if (currentAmount < Partnership.CONSTRUCTION_COST.get(type)) {
                    missingResources.put(type, Partnership.CONSTRUCTION_COST.get(type) - currentAmount);
                }
            }
            throw new InsufficientResourcesException(this,"There are not enough resources to build an Partnership",missingResources);

        }
    }

    public void deductResourcesForMVP() {
        if (hasResourcesForMVP()) {
            deductResource(ResourceType.CAPITAL, MVP.CONSTRUCTION_COST.get(ResourceType.CAPITAL));
            deductResource(ResourceType.TALENT, MVP.CONSTRUCTION_COST.get(ResourceType.TALENT));
            deductResource(ResourceType.CLOUD, MVP.CONSTRUCTION_COST.get(ResourceType.CLOUD));
            deductResource(ResourceType.DATA, MVP.CONSTRUCTION_COST.get(ResourceType.DATA));
        } else {
            Map<ResourceType, Integer> missingResources = new HashMap<>();
            ResourceType[] requiredTypes = {
                    ResourceType.CAPITAL, ResourceType.TALENT, ResourceType.CLOUD, ResourceType.DATA
            };
            for (ResourceType type : requiredTypes) {
                int currentAmount = resources.getOrDefault(type, 0);
                if (currentAmount < MVP.CONSTRUCTION_COST.get(type)) {
                    missingResources.put(type, MVP.CONSTRUCTION_COST.get(type) - currentAmount);
                }
            }
            throw new InsufficientResourcesException(this,"There are not enough resources to build an MVP",missingResources);

        }
    }

    public void deductResourcesForUnicornUpgrade() {
        int cloudCost = Math.max(Unicorn.UPGRADE_COST.get(ResourceType.CLOUD) - getUpgradeCloudDiscount(), 0);
        int dataCost = Unicorn.UPGRADE_COST.get(ResourceType.DATA);

        if (hasResourcesForUnicornUpgrade()) {
            deductResource(ResourceType.CLOUD, cloudCost);
            deductResource(ResourceType.DATA, dataCost);
        } else {
            Map<ResourceType, Integer> missingResources = new HashMap<>();

            int currentData = resources.getOrDefault(ResourceType.DATA, 0);
            if (currentData < dataCost) {
                missingResources.put(ResourceType.DATA, dataCost - currentData);
            }

            int currentCloud = resources.getOrDefault(ResourceType.CLOUD, 0);
            if (currentCloud < cloudCost) {
                missingResources.put(ResourceType.CLOUD, cloudCost - currentCloud);
            }

            throw new InsufficientResourcesException(this, "There are not enough resources to upgrade your MVP to Unicorn", missingResources);
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
