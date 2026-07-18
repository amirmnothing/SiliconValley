package logic.engine;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Player;
import logic.models.Sector;
import logic.models.Vertex;
import javafx.util.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

import ui.controller.GameBoardController;

public class MCTSAIBrain implements AIBrain, Serializable {
    private static final long serialVersionUID = 1L;
    private static GameBoardController controller;
    private transient boolean isThinking = false;
    private static final int SIMULATION_TIME_MS = 3000;
    private static final int MAX_MCTS_ITERATIONS = 50000;
    private static final ExecutorService aiExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    public MCTSAIBrain(GameBoardController controller) {
        this.controller = controller;
    }

    @Override
    public void executeTurn(Player aiPlayer, GameEngine mainEngine, Runnable onTurnComplete) {
        if (isThinking) {
            return;
        }
        isThinking = true;
        if (controller != null) {
            Platform.runLater(() -> controller.setHumanControlsDisabled(true));
        }

        if (mainEngine.isSetupPhaseActive()) {
            executeSetupPhaseUI(aiPlayer, mainEngine);
            return;
        }

        processNextMove(mainEngine, aiPlayer, onTurnComplete, 0);
    }

    private void processNextMove(GameEngine mainEngine, Player aiPlayer, Runnable onTurnComplete, int stepCount) {
        Player winner = mainEngine.winnerPlayer();
        if (winner != null) {
            isThinking = false;
            Platform.runLater(() -> {
                controller.showGameOverScreen(winner);
                refreshUI(mainEngine);
            });
            return;
        }
        if (stepCount > 15) {
            endTurnSafely(mainEngine, onTurnComplete);
            return;
        }

        aiExecutor.submit(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MCTSMove bestMove = calculateBestMoveWithMCTS(mainEngine);

            if (bestMove != null) {

                if (bestMove.isRollDiceMove()) {
                    Platform.runLater(() -> {
                        ArrayList<Integer> dice = mainEngine.rollDiceForCurrentTurn();


                        controller.showDiceResultsUI(dice, () -> {
                            if (dice.get(0) + dice.get(1) == 7) {
                                aiPlayer.setCanPlaceAuditor(true);
                                tryPlaceAuditorByAI(aiPlayer, mainEngine);
                            }
                            refreshUI(mainEngine);
                            PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
                            delay.setOnFinished(e -> processNextMove(mainEngine, aiPlayer, onTurnComplete, stepCount + 1));
                            delay.play();
                        });
                    });
                } else if (bestMove.isEndTurn()) {
                    Platform.runLater(() -> endTurnSafely(mainEngine, onTurnComplete));
                } else {
                    Platform.runLater(() -> {
                        bestMove.execute(mainEngine, aiPlayer, true);
                        refreshUI(mainEngine);

                        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                        delay.setOnFinished(e -> processNextMove(mainEngine, aiPlayer, onTurnComplete, stepCount + 1));
                        delay.play();
                    });
                }
            } else {
                Platform.runLater(() -> endTurnSafely(mainEngine, onTurnComplete));
            }
        });
    }

    private void endTurnSafely(GameEngine mainEngine, Runnable onTurnComplete) {
        isThinking = false;
        mainEngine.endCurrentTurn();
        Platform.runLater(() -> {
            if (controller != null) {
                controller.setHumanControlsDisabled(false);
            }
            refreshUI(mainEngine);

        });
    }

    private MCTSMove calculateBestMoveWithMCTS(GameEngine mainEngine) {
        GameEngine simEngine = mainEngine.deepCopy();
        if (simEngine == null) {
            return null;
        }
        MCTSNode root = new MCTSNode(null, null, simEngine);
        long endTime = System.currentTimeMillis() + SIMULATION_TIME_MS;
        int iterations = 0;

        while (System.currentTimeMillis() < endTime && iterations < MAX_MCTS_ITERATIONS) {
            MCTSNode promisingNode = selectPromisingNode(root);

            if (!promisingNode.isTerminal()) {
                expandNode(promisingNode);
            }

            MCTSNode nodeToExplore = promisingNode;
            if (!promisingNode.children.isEmpty()) {
                nodeToExplore = promisingNode.children.get(new Random().nextInt(promisingNode.children.size()));
            }

            double playoutResult = simulateRandomPlayout(nodeToExplore, mainEngine.getCurrentPlayerIndex());
            backPropagate(nodeToExplore, playoutResult);

            iterations++;

        }

        MCTSNode bestChild = getBestChild(root, 0);
        return bestChild != null ? bestChild.moveThatGotUsHere : null;
    }

    private MCTSNode selectPromisingNode(MCTSNode root) {
        MCTSNode node = root;
        while (!node.children.isEmpty()) {
            node = getBestChild(node, 1.41);
        }
        return node;
    }

    private void expandNode(MCTSNode node) {
        Player currentPlayerInState = node.state.getCurrentPlayer();
        List<MCTSMove> possibleMoves = getPossibleMoves(node.state, currentPlayerInState);

        for (MCTSMove move : possibleMoves) {
            GameEngine childState = node.state.deepCopy();
            if (childState == null) continue;

            move.execute(childState, childState.getCurrentPlayer(), false);
            MCTSNode child = new MCTSNode(node, move, childState);
            node.children.add(child);
        }
        node.state = null;
    }

    private double simulateRandomPlayout(MCTSNode node, int aiPlayerIndex) {
        GameEngine playoutEngine = node.state.deepCopy();
        if (playoutEngine == null) return 0.0;

        int depth = 0;
        int maxDepth = 40;

        while (!isGameOver(playoutEngine) && depth < maxDepth) {
            Player currentPlayer = playoutEngine.getCurrentPlayer();
            List<MCTSMove> possibleMoves = getPossibleMoves(playoutEngine, currentPlayer);

            if (possibleMoves.isEmpty()) {
                if (!playoutEngine.isSetupPhase()) {
                    playoutEngine.endCurrentTurn();
                    break;
                } else {
                    break;
                }
            } else {
                MCTSMove randomMove = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
                randomMove.execute(playoutEngine, currentPlayer, false);
            }
            depth++;
        }

        return evaluatePlayout(playoutEngine, aiPlayerIndex);
    }

    private boolean isGameOver(GameEngine engine) {
        for (Player p : engine.getPlayers()) {
            if (p.calculateVictoryPoints() >= 10) return true;
        }
        return false;
    }

    private double evaluatePlayout(GameEngine engine, int aiPlayerIndex) {
        Player aiInPlayout = engine.getPlayers().get(aiPlayerIndex);
        if (aiInPlayout.calculateVictoryPoints() >= 10) return 1.0;


        double score = aiInPlayout.calculateVictoryPoints() * 10.0;
        int totalResources = aiInPlayout.getResourceCount().values().stream().mapToInt(Integer::intValue).sum();
        score += (totalResources * 0.1);
        score += (aiInPlayout.getCompanies().size() * 2.0);
        for (int i = 0; i < engine.getPlayers().size(); i++) {
            if (i != aiPlayerIndex && engine.getPlayers().get(i).calculateVictoryPoints() >= 10) return 0.0;
        }
        return Math.min(score / 150.0, 0.99);
    }

    private void backPropagate(MCTSNode node, double result) {
        MCTSNode tempNode = node;
        while (tempNode != null) {
            tempNode.visitCount++;
            tempNode.winScore += result;
            tempNode = tempNode.parent;
        }
    }

    private MCTSNode getBestChild(MCTSNode node, double explorationParameter) {
        MCTSNode bestChild = null;
        double bestUCT = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : node.children) {
            if (child.visitCount == 0) {
                return child;
            }
            double uctValue = (child.winScore / (double) child.visitCount)
                    + explorationParameter * Math.sqrt(Math.log(node.visitCount) / (double) child.visitCount);
            if (uctValue > bestUCT) {
                bestUCT = uctValue;
                bestChild = child;
            }
        }
        return bestChild != null ? bestChild : (node.children.isEmpty() ? null : node.children.get(0));
    }

    private List<MCTSMove> getPossibleMoves(GameEngine engine, Player player) {
        List<MCTSMove> moves = new ArrayList<>();

        if (engine.isSetupPhase()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canBuildMVP(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.buildMVP(v, p);
                                    if (isReal) buildMVP_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }

            List<Edge> allEdges = engine.getMap().getEdges();
            for (int i = 0; i < allEdges.size(); i++) {
                final int edgeIndex = i;
                if (engine.canBuildPartnership(player, allEdges.get(i))) {
                    moves.add(new MCTSMove() {
                        @Override
                        public void execute(GameEngine e, Player p, boolean isReal) {
                            try {
                                Edge simEdge = e.getMap().getEdges().get(edgeIndex);
                                e.buildPartnership(p, simEdge);
                                if (isReal) buildPartnership_UI(e, simEdge);
                            } catch (Exception ex) {
                            }
                        }
                    });
                }
            }

            return moves;
        }

        if (!engine.getIsDiceRolled()) {
            moves.add(new MCTSMove() {
                @Override
                public void execute(GameEngine e, Player p, boolean isReal) {
                    try {
                        if (!isReal) e.rollDiceForCurrentTurn();
                    } catch (Exception ex) {
                        e.setIsDiceRolled(true);
                    }
                }

                @Override
                public boolean isRollDiceMove() {
                    return true;
                }
            });
            return moves;
        }
        if (player.hasResourcesForUnicornUpgrade()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canUpgradeToUnicorn(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.upgradeToUnicorn(v, p);
                                    if (isReal) upgradeUnicorn_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }
        }

        if (player.hasResourcesForMVP()) {
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    if (engine.canBuildMVP(r, c)) {
                        final int finalR = r;
                        final int finalC = c;
                        moves.add(new MCTSMove() {
                            @Override
                            public void execute(GameEngine e, Player p, boolean isReal) {
                                try {
                                    Vertex v = e.getMap().getVertices()[finalR][finalC];
                                    e.buildMVP(v, p);
                                    if (isReal) buildMVP_UI(e, v);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                }
            }
        }

        if (player.hasResourcesForPartnership()) {
            List<Edge> allEdges = engine.getMap().getEdges();
            for (int i = 0; i < allEdges.size(); i++) {
                final int edgeIndex = i;
                if (engine.canBuildPartnership(player, allEdges.get(i))) {
                    moves.add(new MCTSMove() {
                        @Override
                        public void execute(GameEngine e, Player p, boolean isReal) {
                            try {
                                Edge simEdge = e.getMap().getEdges().get(edgeIndex);
                                e.buildPartnership(p, simEdge);

                                if (isReal) {
                                    buildPartnership_UI(e, simEdge);
                                }
                            } catch (Exception _) {
                            }
                        }
                    });
                }
            }
        }

        for (logic.enums.ResourceType targetResource : logic.enums.ResourceType.values()) {
            if (targetResource == logic.enums.ResourceType.CAPITAL) {
                continue;
            }

            if (canAndShouldBuyResource(engine, player, targetResource)) {
                moves.add(new MCTSMove() {
                    @Override
                    public void execute(GameEngine e, Player p, boolean isReal) {
                        if (isReal) {
                            try {
                                e.getMarket().buyFromMarket(e, p, targetResource, 1);

                                javafx.application.Platform.runLater(() -> {
                                    controller.refreshPlayersResourcesUI();
                                    controller.resetMarketPricesUI();

                                });
                            } catch (exception.InsufficientResourcesException |
                                     exception.InvalidMarketTransactionException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            int price = e.getMarket().getPrice(targetResource);
                            int finalPrice = p.calculateMarketPrice(targetResource, price);
                            p.deductResource(logic.enums.ResourceType.CAPITAL, finalPrice);
                            p.addResource(targetResource, 1);
                        }
                    }
                });
            }
        }

        moves.add(new MCTSMove() {
            @Override
            public void execute(GameEngine e, Player p, boolean isReal) {
                e.endCurrentTurn();
            }

            @Override
            public boolean isEndTurn() {
                return true;
            }
        });

        return moves;
    }

    private interface MCTSMove {
        void execute(GameEngine engine, Player player, boolean isRealMove);

        default boolean isEndTurn() {
            return false;
        }

        default boolean isRollDiceMove() {
            return false;
        }
    }

    private class MCTSNode {
        MCTSNode parent;
        List<MCTSNode> children = new ArrayList<>();
        int visitCount = 0;
        double winScore = 0;
        MCTSMove moveThatGotUsHere;
        GameEngine state;

        public MCTSNode(MCTSNode parent, MCTSMove move, GameEngine state) {
            this.parent = parent;
            this.moveThatGotUsHere = move;
            this.state = state;
        }

        public boolean isTerminal() {
            if (this.state == null) return false;
            return MCTSAIBrain.this.isGameOver(this.state);
        }
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

    private int calculateEnemyPresence(logic.models.Sector sector, Player aiPlayer) {
        int score = 0;
        java.util.Map<logic.enums.CornerDirection, Vertex> corners = sector.getCorners();
        for (logic.enums.CornerDirection direction : logic.enums.CornerDirection.values()) {
            Vertex vertex = corners.get(direction);
            logic.models.CompanyStructure structure = vertex.getCompanyStructure();
            if (structure != null && !structure.getOwner().equals(aiPlayer)) {
                if (structure instanceof logic.models.Unicorn) score += 2;
                else if (structure instanceof logic.models.MVP) score += 1;
            }
        }
        return score;
    }

    private void executeSetupPhaseUI(Player aiPlayer, GameEngine engine) {
        Color color = controller.getPlayerColor();
        PauseTransition mvpDelay = new PauseTransition(Duration.seconds(1.5));
        mvpDelay.setOnFinished(e -> {
            if (!engine.isSetupPlacedMVP()) {
                Vertex bestMVP = findBestSetupMVP(engine);
                if (bestMVP != null) {
                    engine.buildMVP(bestMVP, aiPlayer);
                    Platform.runLater(() -> {
                        controller.updateVertexUI(bestMVP, color);
                        controller.refreshPlayersResourcesUI();
                        controller.updatePlayersPoints();
                    });
                    engine.notifyMVPPlaced();
                }
            }


            PauseTransition partnershipDelay = new PauseTransition(Duration.seconds(1.5));
            partnershipDelay.setOnFinished(e2 -> {
                isThinking = false;
                if (!engine.isSetupPlacedPartnership()) {
                    Edge bestEdge = findBestSetupPartnership(engine, aiPlayer);
                    if (bestEdge != null) {
                        engine.buildPartnership(aiPlayer, bestEdge);
                        Platform.runLater(() -> {
                            controller.updateEdgeUI(bestEdge, color);
                            controller.refreshPlayersResourcesUI();
                            controller.updatePlayersPoints();
                        });
                        engine.notifyPartnershipPlaced();
                    }
                }


                refreshUI(engine);
            });
            partnershipDelay.play();
        });
        mvpDelay.play();
    }

    private void buildMVP_UI(GameEngine engine, Vertex targetVertex) {
        Platform.runLater(() -> {
            controller.updateVertexUI(targetVertex, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    private void buildPartnership_UI(GameEngine engine, Edge targetEdge) {
        Platform.runLater(() -> {
            controller.updateEdgeUI(targetEdge, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    private void upgradeUnicorn_UI(GameEngine engine, Vertex targetVertex) {
        Platform.runLater(() -> {
            controller.updateUnicornUI(targetVertex, controller.getPlayerColor());
            controller.refreshPlayersResourcesUI();
            controller.updatePlayersPoints();
        });
    }

    public static void setController(GameBoardController controller) {
        MCTSAIBrain.controller = controller;
    }

    private void refreshUI(GameEngine engine) {
        if (controller == null) return;
        Platform.runLater(() -> {
            if (engine.getCurrentPlayerIndex() == 0) controller.resetMarketPricesUI();
            controller.changePlayerTextColor();
            controller.refreshPlayersResourcesUI();
            controller.updateTurnControls();
        });
    }

    public java.util.Map<logic.enums.ResourceType, Integer> calculateResourcesToDiscard(Player player) {
        java.util.Map<logic.enums.ResourceType, Integer> currentResources = player.getResourceCount();

        int totalResources = currentResources.values().stream().mapToInt(Integer::intValue).sum();
        if (totalResources <= player.getCrisisModifierThreshold()) {
            return new java.util.HashMap<>();
        }

        int targetDiscardAmount = totalResources / 2;

        java.util.Map<logic.enums.ResourceType, Integer> discardMap = new java.util.HashMap<>();
        java.util.Map<logic.enums.ResourceType, Integer> tempResources = new java.util.HashMap<>(currentResources);

        java.util.Map<logic.enums.ResourceType, Integer> neededResources = calculateNeededResourcesForPlayer(player);

        for (int i = 0; i < targetDiscardAmount; i++) {
            logic.enums.ResourceType bestCandidateToDiscard = null;
            double maxDiscardScore = -Double.MAX_VALUE;

            for (java.util.Map.Entry<logic.enums.ResourceType, Integer> entry : tempResources.entrySet()) {
                logic.enums.ResourceType resourceType = entry.getKey();
                int currentCount = entry.getValue();

                if (currentCount <= 0) {
                    continue;
                }

                int neededCount = neededResources.getOrDefault(resourceType, 0);
                int excessCount = currentCount - neededCount;

                double discardScore;

                if (excessCount > 0) {
                    discardScore = 1000.0 + (excessCount * 10.0) + currentCount;
                } else {
                    discardScore = 100.0 - (neededCount * 5.0) + currentCount;
                }

                if (discardScore > maxDiscardScore) {
                    maxDiscardScore = discardScore;
                    bestCandidateToDiscard = resourceType;
                }
            }
            if (bestCandidateToDiscard != null) {
                discardMap.put(bestCandidateToDiscard, discardMap.getOrDefault(bestCandidateToDiscard, 0) + 1);
                tempResources.put(bestCandidateToDiscard, tempResources.get(bestCandidateToDiscard) - 1);
            }
        }

        return discardMap;
    }

    private java.util.Map<logic.enums.ResourceType, Integer> calculateNeededResourcesForPlayer(Player player) {
        java.util.Map<logic.enums.ResourceType, Integer> needed = new java.util.HashMap<>();
        if (player.hasMVP()) {
            mergeRequiredCosts(needed, logic.models.Unicorn.UPGRADE_COST);
        }

        mergeRequiredCosts(needed, logic.models.MVP.CONSTRUCTION_COST);
        mergeRequiredCosts(needed, logic.models.Partnership.CONSTRUCTION_COST);

        return needed;
    }

    private void mergeRequiredCosts(java.util.Map<logic.enums.ResourceType, Integer> targetMap,
                                    java.util.Map<logic.enums.ResourceType, Integer> costMap) {
        if (costMap == null) return;
        for (java.util.Map.Entry<logic.enums.ResourceType, Integer> entry : costMap.entrySet()) {
            int currentMax = targetMap.getOrDefault(entry.getKey(), 0);
            targetMap.put(entry.getKey(), Math.max(currentMax, entry.getValue()));
        }
    }

    private Vertex findBestSetupMVP(GameEngine engine) {
        Map map = engine.getMap();
        Vertex[][] vertices = map.getVertices();
        Sector[][] sectors = map.getSectors();
        Vertex bestVertex = null;
        double bestScore = -1;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                if (engine.canBuildMVP(r, c)) {
                    Vertex v = vertices[r][c];
                    double score = evaluateVertexForSetup(v, sectors);
                    score += Math.random() * 0.5;

                    if (score > bestScore) {
                        bestScore = score;
                        bestVertex = v;
                    }
                }
            }
        }
        return bestVertex;
    }

    private double evaluateVertexForSetup(Vertex vertex, Sector[][] allSectors) {
        double score = 0;
        List<logic.enums.ResourceType> uniqueResources = new ArrayList<>();
        for (int r = 0; r < allSectors.length; r++) {
            for (int c = 0; c < allSectors[r].length; c++) {
                Sector sector = allSectors[r][c];

                if (sector != null && isVertexInSector(vertex, sector)) {
                    if (sector.getResourceType() != ResourceType.REGULATORY) {
                        int number = sector.getactivationNumber();
                        score += getDiceProbability(number);
                        if (!uniqueResources.contains(sector.getResourceType())) {
                            uniqueResources.add(sector.getResourceType());
                        }
                    }
                }
            }
        }

        score += (uniqueResources.size() * 2.0);

        return score;
    }

    private boolean isVertexInSector(Vertex vertex, Sector sector) {
        for (logic.enums.CornerDirection dir : logic.enums.CornerDirection.values()) {
            if (vertex.equals(sector.getCorner(dir))) {
                return true;
            }
        }
        return false;
    }

    private int getDiceProbability(int diceNumber) {
        switch (diceNumber) {
            case 2:
            case 12:
                return 1;
            case 3:
            case 11:
                return 2;
            case 4:
            case 10:
                return 3;
            case 5:
            case 9:
                return 4;
            case 6:
            case 8:
                return 5;
            default:
                return 0;
        }
    }

    private Edge findBestSetupPartnership(GameEngine engine, Player aiPlayer) {
        List<Edge> validEdges = new ArrayList<>();
        Sector[][] sectors = engine.getMap().getSectors();

        for (Edge edge : engine.getMap().getEdges()) {
            if (engine.canBuildPartnership(aiPlayer, edge)) {
                validEdges.add(edge);
            }
        }

        Vertex lastBuiltMVPVertex = null;
        List<logic.models.CompanyStructure> companies = aiPlayer.getCompanies();

        if (companies != null && !companies.isEmpty()) {

            logic.models.CompanyStructure lastCompany = companies.get(companies.size() - 1);

            outerLoop:
            for (int r = 0; r < engine.getMap().getRows(); r++) {
                for (int c = 0; c < engine.getMap().getCols(); c++) {
                    Vertex v = engine.getMap().getVertices()[r][c];
                    if (v != null && lastCompany.equals(v.getCompanyStructure())) {
                        lastBuiltMVPVertex = v;
                        break outerLoop;
                    }
                }
            }
        }

        Edge bestEdge = null;
        double bestEdgeScore = -1;

        for (Edge edge : validEdges) {
            if (lastBuiltMVPVertex != null) {
                if (!edge.getStart().equals(lastBuiltMVPVertex) && !edge.getEnd().equals(lastBuiltMVPVertex)) {
                    continue;
                }
            }

            Vertex targetVertex = edge.getOppositeVertex(lastBuiltMVPVertex);

            double score = (targetVertex != null) ? evaluateVertexForSetup(targetVertex, sectors) : 0;

            if (score > bestEdgeScore) {
                bestEdgeScore = score;
                bestEdge = edge;
            }
        }

        if (bestEdge == null && !validEdges.isEmpty()) {
            List<Edge> connectedEdges = new ArrayList<>();
            for (Edge e : validEdges) {
                if (lastBuiltMVPVertex == null || e.getStart().equals(lastBuiltMVPVertex) || e.getEnd().equals(lastBuiltMVPVertex)) {
                    connectedEdges.add(e);
                }
            }
            if (!connectedEdges.isEmpty()) {
                return connectedEdges.get(new java.util.Random().nextInt(connectedEdges.size()));
            }
            return validEdges.get(new java.util.Random().nextInt(validEdges.size()));
        }

        return bestEdge;
    }

    private boolean canAndShouldBuyResource(GameEngine engine, Player player, logic.enums.ResourceType targetResource) {
        java.util.Map<logic.enums.ResourceType, Integer> resources = player.getResourceCount();
        int currentCapital = resources.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
        int marketPrice = engine.getMarket().getPrice(targetResource);
        int finalCost = player.calculateMarketPrice(targetResource, marketPrice);
        if (currentCapital < finalCost) return false;

        if (player.hasMVP() && !player.hasResourcesForUnicornUpgrade()) {
            int reservedCapital = logic.models.Unicorn.UPGRADE_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) {
                if (targetResource == logic.enums.ResourceType.CLOUD) {
                    int missingCloud = Math.max(0, (logic.models.Unicorn.UPGRADE_COST.get(logic.enums.ResourceType.CLOUD) - player.getUpgradeCloudDiscount()) - resources.getOrDefault(logic.enums.ResourceType.CLOUD, 0));
                    if (missingCloud > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.DATA) {
                    int missingData = Math.max(0, logic.models.Unicorn.UPGRADE_COST.get(logic.enums.ResourceType.DATA) - resources.getOrDefault(logic.enums.ResourceType.DATA, 0));
                    if (missingData > 0) return true;
                }
            }
        }

        if (!player.hasResourcesForMVP()) {
            int reservedCapital = logic.models.MVP.CONSTRUCTION_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) {
                if (targetResource == logic.enums.ResourceType.TALENT) {
                    int missingTalent = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.TALENT) - resources.getOrDefault(logic.enums.ResourceType.TALENT, 0));
                    if (missingTalent > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.CLOUD) {
                    int missingCloud = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.CLOUD) - resources.getOrDefault(logic.enums.ResourceType.CLOUD, 0));
                    if (missingCloud > 0) return true;
                }
                if (targetResource == logic.enums.ResourceType.DATA) {
                    int missingData = Math.max(0, logic.models.MVP.CONSTRUCTION_COST.get(logic.enums.ResourceType.DATA) - resources.getOrDefault(logic.enums.ResourceType.DATA, 0));
                    if (missingData > 0) return true;
                }
            }
        }

        if (!player.hasResourcesForPartnership()) {
            int reservedCapital = logic.models.Partnership.CONSTRUCTION_COST.getOrDefault(logic.enums.ResourceType.CAPITAL, 0);
            if (currentCapital - finalCost >= reservedCapital) {
                if (targetResource == logic.enums.ResourceType.PATENT) {
                    int missingPatent = Math.max(0, logic.models.Partnership.CONSTRUCTION_COST.get(logic.enums.ResourceType.PATENT) - resources.getOrDefault(logic.enums.ResourceType.PATENT, 0));
                    if (missingPatent > 0) return true;
                }
            }
        }
        return false;
    }
}