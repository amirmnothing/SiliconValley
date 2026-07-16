package logic.engine;

import exception.InsufficientResourcesException;
import exception.InvalidMarketTransactionException;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import logic.enums.CornerDirection;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Player;
import logic.models.Sector;
import logic.models.Vertex;
import logic.models.*;
import ui.controller.GameBoardController;
import javafx.application.Platform;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimpleAIBrain implements Serializable, AIBrain {
    private static final long serialVersionUID = 1L;
    private static GameBoardController controller;

    public SimpleAIBrain(GameBoardController controller) {
        SimpleAIBrain.controller = controller;
    }

    public void executeTurn(Player aiPlayer, GameEngine engine, Runnable onTurnComplete) {

        if (controller != null) {
            controller.setHumanControlsDisabled(true);
        }
        if (engine.isSetupPhaseActive()) {
            executeSetupPhase(aiPlayer, engine);
            return;
        }
        if (engine.isMainPhaseActive()) {
            if (!engine.getIsDiceRolled()) {
                ArrayList<Integer> dice = engine.rollDiceForCurrentTurn();

                Runnable continueAITurn = () -> {
                    PauseTransition actionDelay = new PauseTransition(Duration.seconds(1.5));
                    actionDelay.setOnFinished(e -> {

                        if (dice.get(0) + dice.get(1) == 7) {
                            aiPlayer.setCanPlaceAuditor(true);
                            tryPlaceAuditorByAI(aiPlayer, engine);
                        }

                        scheduleAction(() -> {
                            tryMarketTrade(aiPlayer, engine);
                            refreshUI(engine);

                            scheduleAction(() -> {
                                tryUpgradeToUnicorn(aiPlayer, engine);
                                refreshUI(engine);

                                scheduleAction(() -> {
                                    tryBuildMVP(aiPlayer, engine);
                                    refreshUI(engine);

                                    scheduleAction(() -> {
                                        tryBuildPartnership(aiPlayer, engine);
                                        refreshUI(engine);

                                        scheduleAction(() -> {
                                            engine.endCurrentTurn();
                                            refreshUI(engine);
                                        });
                                    });
                                });
                            });
                        });
                    });
                    actionDelay.play();
                };


                Platform.runLater(() -> {

                    controller.showDiceResultsUI(dice, continueAITurn);
                    refreshUI(engine);
                });

            }
            return;
        }

    }

    private void scheduleAction(Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
        pause.setOnFinished(e -> action.run());
        pause.play();
    }

    private void executeSetupPhase(Player aiPlayer, GameEngine engine) {
        PauseTransition mvpDelay = new PauseTransition(Duration.seconds(1.5));
        mvpDelay.setOnFinished(e -> {

            if (!engine.isSetupPlacedMVP()) {
                placeSetupMVP(aiPlayer, engine);
            }

            PauseTransition partnershipDelay = new PauseTransition(Duration.seconds(1.5));
            partnershipDelay.setOnFinished(e2 -> {

                if (!engine.isSetupPlacedPartnership()) {
                    placeSetupPartnership(aiPlayer, engine);
                }

                refreshUI(engine);
            });

            partnershipDelay.play();
        });
        mvpDelay.play();
    }

    private void placeSetupMVP(Player aiPlayer, GameEngine engine) {
        Map map = engine.getMap();
        Vertex[][] vertices = map.getVertices();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                if (engine.canBuildMVP(r, c)) {
                    engine.buildMVP(vertices[r][c], aiPlayer);
                    Vertex vertex = vertices[r][c];
                    Color aiColor = controller.getPlayerColor();
                    Platform.runLater(() -> {

                        controller.updateVertexUI(vertex, aiColor);


                        controller.refreshPlayersResourcesUI();
                        controller.updatePlayersPoints();
                    });
                    engine.notifyMVPPlaced();
                    return;
                }
            }
        }
    }

    private void placeSetupPartnership(Player aiPlayer, GameEngine engine) {
        Map map = engine.getMap();
        Vertex[][] vertices = map.getVertices();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Vertex v = vertices[r][c];
                if (v == null) continue;

                for (Edge edge : v.getAdjacentEdges()) {
                    if (engine.canBuildPartnership(aiPlayer, edge)) {
                        engine.buildPartnership(aiPlayer, edge);
                        Color aiColor = controller.getPlayerColor();
                        Platform.runLater(() -> {

                            controller.updateEdgeUI(edge, aiColor);


                            controller.refreshPlayersResourcesUI();
                            controller.updatePlayersPoints();
                        });
                        engine.notifyPartnershipPlaced();
                        return;
                    }
                }
            }
        }
    }

    public java.util.Map<ResourceType, Integer> calculateResourcesToDiscard(Player aiPlayer) {
        java.util.Map<ResourceType, Integer> discardMap = new HashMap<>();
        java.util.Map<ResourceType, Integer> currentResources = aiPlayer.getResourceCount();

        int totalResources = 0;
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType == ResourceType.REGULATORY) continue;
            totalResources += currentResources.getOrDefault(resourceType, 0);
        }
        if (totalResources <= aiPlayer.getCrisisModifierThreshold()) {
            return new java.util.HashMap<>();
        }
        int targetDiscardCount = totalResources / 2;
        int discarded = 0;

        List<ResourceType> discardPriority = Arrays.asList(
                ResourceType.PATENT,
                ResourceType.TALENT,
                ResourceType.CLOUD,
                ResourceType.DATA,
                ResourceType.CAPITAL

        );

        for (ResourceType type : discardPriority) {
            int count = currentResources.getOrDefault(type, 0);

            while (count > 0 && discarded < targetDiscardCount) {
                discardMap.put(type, discardMap.getOrDefault(type, 0) + 1);
                count--;
                discarded++;
            }

            if (discarded == targetDiscardCount) {
                break;
            }
        }

        return discardMap;
    }

    private void tryPlaceAuditorByAI(Player aiPlayer, GameEngine engine) {

        int[] targetCoords = findBestSectorCoordsForAuditor(engine, aiPlayer);

        if (targetCoords != null) {
            int targetRow = targetCoords[0];
            int targetCol = targetCoords[1];

            Platform.runLater(() -> {
                controller.performAIAuditorMove(targetRow, targetCol);
            });
        }
    }

    private int[] findBestSectorCoordsForAuditor(GameEngine engine, Player aiPlayer) {
        Sector[][] sectors = engine.getMap().getSectors();
        int bestScore = -1;
        int[] bestCoords = null;

        for (int r = 0; r < engine.getMap().getRows(); r++) {
            for (int c = 0; c < engine.getMap().getCols(); c++) {
                Sector sector = sectors[r][c];
                if (sector != null && engine.canPlaceAuditor(sector)) {
                    int currentScore = calculateEnemyPresence(sector, aiPlayer);
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestCoords = new int[]{r, c};
                    }
                }
            }
        }

        return bestCoords;
    }


    private int calculateEnemyPresence(Sector sector, Player aiPlayer) {
        int score = 0;
        java.util.Map<CornerDirection, Vertex> corners = sector.getCorners();
        for (CornerDirection direction : CornerDirection.values()) {
            Vertex vertex = corners.get(direction);
            CompanyStructure structure = vertex.getCompanyStructure();
            if (structure != null && !structure.getOwner().equals(aiPlayer)) {
                if (structure instanceof Unicorn) {
                    score += 2;
                } else if (structure instanceof MVP) {
                    score += 1;
                }
            }
        }
        return score;
    }

    private void tryBuildMVP(Player aiPlayer, GameEngine engine) {
        if (!aiPlayer.hasResourcesForMVP()) return;

        try {
            Map map = engine.getMap();
            Vertex[][] vertices = map.getVertices();

            for (int r = 0; r < map.getRows(); r++) {
                for (int c = 0; c < map.getCols(); c++) {
                    if (engine.canBuildMVP(r, c)) {
                        engine.buildMVP(vertices[r][c], aiPlayer);
                        Vertex vertex = vertices[r][c];
                        Platform.runLater(() -> {

                            controller.updateVertexUI(vertex, controller.getPlayerColor());


                            controller.refreshPlayersResourcesUI();
                            controller.updatePlayersPoints();
                        });
                        refreshUI(engine);
                        return;
                    }
                }
            }
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        }
    }

    private void tryBuildPartnership(Player aiPlayer, GameEngine engine) {
        if (!aiPlayer.hasResourcesForPartnership()) return;

        try {
            Map map = engine.getMap();
            Vertex[][] vertices = map.getVertices();

            for (int r = 0; r < map.getRows(); r++) {
                for (int c = 0; c < map.getCols(); c++) {
                    Vertex v = vertices[r][c];
                    if (v == null) continue;

                    for (Edge edge : v.getAdjacentEdges()) {
                        if (engine.canBuildPartnership(aiPlayer, edge)) {
                            engine.buildPartnership(aiPlayer, edge);
                            Platform.runLater(() -> {

                                controller.updateEdgeUI(edge, controller.getPlayerColor());


                                controller.refreshPlayersResourcesUI();
                                controller.updatePlayersPoints();
                            });
                            refreshUI(engine);
                            return;
                        }
                    }
                }
            }
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        }
    }

    private void tryUpgradeToUnicorn(Player aiPlayer, GameEngine engine) {
        if (!aiPlayer.hasResourcesForUnicornUpgrade()) {
            return;
        }

        Map map = engine.getMap();
        Vertex[][] vertices = map.getVertices();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Vertex v = vertices[r][c];
                if (v != null && v.getCompanyStructure() instanceof MVP) {
                    if (v.getCompanyStructure().getOwner() == aiPlayer) {
                        try {
                            aiPlayer.deductResourcesForUnicornUpgrade();

                            CompanyStructure oldMvp = v.getCompanyStructure();
                            Unicorn newUnicorn = new Unicorn(aiPlayer);

                            v.setCompanyStructure(newUnicorn);
                            aiPlayer.removeCompanyStructure(oldMvp);
                            aiPlayer.addCompanyStructure(newUnicorn);
                            Platform.runLater(() -> {
                                controller.updateUnicornUI(v, controller.getPlayerColor());

                                controller.refreshPlayersResourcesUI();
                                controller.updatePlayersPoints();
                            });
                            refreshUI(engine);
                            return;
                        } catch (InsufficientResourcesException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void tryMarketTrade(Player aiPlayer, GameEngine engine) {
        java.util.Map<ResourceType, Integer> resources = aiPlayer.getResourceCount();

        if (aiPlayer.hasMVP() && !aiPlayer.hasResourcesForUnicornUpgrade()) {
            int reservedCapital = Unicorn.UPGRADE_COST.getOrDefault(ResourceType.CAPITAL, 0);
            int missingCloud = Math.max(0, (Unicorn.UPGRADE_COST.get(ResourceType.CLOUD) - aiPlayer.getUpgradeCloudDiscount()) - resources.getOrDefault(ResourceType.CLOUD, 0));
            int missingData = Math.max(0, Unicorn.UPGRADE_COST.get(ResourceType.DATA) - resources.getOrDefault(ResourceType.DATA, 0));

            if (missingCloud > 0 && tryBuyResource(engine, aiPlayer, ResourceType.CLOUD, reservedCapital)) return;
            if (missingData > 0 && tryBuyResource(engine, aiPlayer, ResourceType.DATA, reservedCapital)) return;
        }

        if (!aiPlayer.hasResourcesForMVP()) {
            int reservedCapital = MVP.CONSTRUCTION_COST.getOrDefault(ResourceType.CAPITAL, 0);
            int missingTalent = Math.max(0, MVP.CONSTRUCTION_COST.get(ResourceType.TALENT) - resources.getOrDefault(ResourceType.TALENT, 0));
            int missingCloud = Math.max(0, MVP.CONSTRUCTION_COST.get(ResourceType.CLOUD) - resources.getOrDefault(ResourceType.CLOUD, 0));
            int missingData = Math.max(0, MVP.CONSTRUCTION_COST.get(ResourceType.DATA) - resources.getOrDefault(ResourceType.DATA, 0));

            if (missingTalent > 0 && tryBuyResource(engine, aiPlayer, ResourceType.TALENT, reservedCapital)) return;
            if (missingCloud > 0 && tryBuyResource(engine, aiPlayer, ResourceType.CLOUD, reservedCapital)) return;
            if (missingData > 0 && tryBuyResource(engine, aiPlayer, ResourceType.DATA, reservedCapital)) return;
        }

        if (!aiPlayer.hasResourcesForPartnership()) {
            int reservedCapital = Partnership.CONSTRUCTION_COST.getOrDefault(ResourceType.CAPITAL, 0);
            int missingPatent = Math.max(0, Partnership.CONSTRUCTION_COST.get(ResourceType.PATENT) - resources.getOrDefault(ResourceType.PATENT, 0));

            if (missingPatent > 0 && tryBuyResource(engine, aiPlayer, ResourceType.PATENT, reservedCapital)) return;
        }
    }

    private boolean tryBuyResource(GameEngine engine, Player aiPlayer, ResourceType type, int reservedCapital) {
        int currentCapital = aiPlayer.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0);
        int marketPrice = engine.getMarket().getPrice(type);
        if (currentCapital - marketPrice < reservedCapital) {
            return false;
        }

        try {
            engine.getMarket().buyFromMarket(engine, aiPlayer, type, 1);
            return true;
        } catch (InsufficientResourcesException | InvalidMarketTransactionException e) {
            return false;
        }
    }

    private void refreshUI(GameEngine engine) {
        if (controller == null) return;

        Platform.runLater(() -> {
            if (engine.getCurrentPlayerIndex() == 0) {
                controller.resetMarketPricesUI();
            }
            controller.changePlayerTextColor();
            controller.refreshPlayersResourcesUI();
        });
        controller.updateTurnControls();

    }

    public static void setController(GameBoardController c) {
        controller = c;
    }
}