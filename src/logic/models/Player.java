package logic.models;

import logic.enums.PlayerRole;
import logic.enums.ResourceType;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Player {
    private Map<ResourceType, Integer> resources = new HashMap<>();
    private List<CompanyStructure> companies;
    private PlayerRole playerRole;
    private boolean hasLongestNetwork = false;//یک flag برای بلندترین مسیر

    public Player( List<CompanyStructure> companies, PlayerRole playerRole) {
        resources = new HashMap<>();
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
        this.companies=companies;
        this.playerRole=playerRole;
    }
    public int culculateVictoryPoints() {
        int totalPoints = 0;
        if(companies!=null) {
            for (CompanyStructure comp : companies) {
                totalPoints += comp.getVictoryPoints();
            }
        }
        if(hasLongestNetwork) totalPoints+=2;

        // TODO : برای کسر یک امتیاز برای نقش های خاص ک بعدا تکمیل بشه

        return totalPoints;

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
        if (this.companies != null)  this.companies.remove(structure);
    }

    public boolean hasResourcesForPartnership(){
        if (resources.getOrDefault(ResourceType.PATENT, 0) > 0 && resources.getOrDefault(ResourceType.CAPITAL, 0) > 0){
            return true;
        }
        return false;
    }

    public void deductResourcesForPartnership(){
        deductResource(ResourceType.CAPITAL , 1);
        deductResource(ResourceType.PATENT , 1);
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
