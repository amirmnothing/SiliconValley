package logic.models;

import logic.enums.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class Market {
    private final Map<ResourceType, Integer> currentPrices = new HashMap<>();
    private final Map<ResourceType, Integer> roundsWithoutTrade = new HashMap<>();

    private final Map<ResourceType, Boolean> tradedInCurrentRound = new HashMap<>();

    public Market() {
        for (ResourceType type : ResourceType.values()) {
            currentPrices.put(type, 4);
            roundsWithoutTrade.put(type, 0);
            tradedInCurrentRound.put(type, false);
        }
    }

    public int getPrice(ResourceType type) {
        return currentPrices.getOrDefault(type, 4);
    }

    public boolean buyFromMarket(Player player, ResourceType resourceToBuy) {
        if (resourceToBuy == ResourceType.CAPITAL) {
            //TODO برای خطا میشه از اکسپشن استفاده کرد
            return false;
        }

        int capitalPrice = getPrice(resourceToBuy);
        int finalPrice = player.calculateMarketPrice(resourceToBuy,capitalPrice);
        int playerCapitalCount = player.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0);
        if (playerCapitalCount < finalPrice) {
            //TODO برای خطا اینجا هم  اکسپشن
            return false;
        }

        player.deductResource(ResourceType.CAPITAL, finalPrice);
        player.addResource(resourceToBuy, 1);

        if (currentPrices.get(resourceToBuy) < 6) {
            currentPrices.put(resourceToBuy, currentPrices.get(resourceToBuy) + 1);
        }
        roundsWithoutTrade.put(resourceToBuy, 0);
        tradedInCurrentRound.put(resourceToBuy, true);

        return true;
    }
    
    public void updateMarketAtEndOfRound() {
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType == ResourceType.CAPITAL) continue;

            if (!tradedInCurrentRound.get(resourceType)) {
                roundsWithoutTrade.put(resourceType, roundsWithoutTrade.get(resourceType) + 1);


                if (roundsWithoutTrade.get(resourceType) >= 3) {
                    if (currentPrices.get(resourceType) > 2) {
                        currentPrices.put(resourceType, currentPrices.get(resourceType) - 1);
                    }
                    roundsWithoutTrade.put(resourceType, 0);
                }
            } else {
                roundsWithoutTrade.put(resourceType, 0);
            }


            tradedInCurrentRound.put(resourceType, false);
        }
    }
}