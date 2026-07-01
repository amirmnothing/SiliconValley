package logic.models;

import exception.InsufficientResourcesException;
import exception.InvalidMarketTransactionException;
import logic.engine.GameEngine;
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

    public void buyFromMarket(GameEngine gameEngine, Player player, ResourceType resourceToBuy, int count) {
        if (resourceToBuy == ResourceType.CAPITAL) {
            throw new InvalidMarketTransactionException(ResourceType.CAPITAL, "You cannot buy CAPITAL from the market!");
        }
        if (count <= 0) {
            throw new InvalidMarketTransactionException(resourceToBuy, "Count must be greater than zero!");
        }

        int Price = getPrice(resourceToBuy) * count;
        int finalPrice = player.calculateMarketPrice(resourceToBuy, Price);

        int playerCapitalCount = player.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0);
        if (playerCapitalCount < finalPrice) {
            throw new InsufficientResourcesException(player, "You don’t have enough capital to buy this resource", null);
        }

        player.deductResource(ResourceType.CAPITAL, finalPrice);
        player.addResource(resourceToBuy, count);

        roundsWithoutTrade.put(resourceToBuy, 0);
        tradedInCurrentRound.put(resourceToBuy, true);


    }

    public void updateMarketAtEndOfRound() {
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType == ResourceType.CAPITAL) continue;

            if(tradedInCurrentRound.get(resourceType)){
                currentPrices.put(resourceType, currentPrices.get(resourceType) + 1);
            }

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