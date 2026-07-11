package logic.engine;

import exception.InsufficientResourcesException;
import exception.InvalidPlacementException;
import javafx.scene.paint.Color;
import logic.enums.BuildMode;
import logic.enums.CornerDirection;
import logic.enums.ResourceType;
import logic.models.*;

import java.util.*;

public class GameEngine {
    private final Map map;
    private final List<Player> players;
    private final Market market;
    private int currentPlayerIndex;
    private final Random random = new Random();

    final public static String PLAYER1COLOR = "rgb(150, 0, 0)";
    final public static String PLAYER2COLOR = "rgb(0, 0, 170)";
    final public static String PLAYER3COLOR = "rgb(0, 140, 100)";
    final public static String PLAYER4COLOR = "rgb(255, 215, 0)";

    private boolean setupPhaseActive;
    private int setupRound;
    private int setupDirection; // 1 : Toward | -1 : Backward
    private boolean setupPlacedMVP;
    private boolean setupPlacedPartnership;
    private int setupTurnCount;

    private BuildMode currentBuildMode = BuildMode.NONE;


    private boolean isDiceRolled = false;
    private boolean mainPhaseActive = false;
    private int turnNumber = 1;
    private int currentTurnNumber = 1;


    public GameEngine(Map map, List<Player> players) {
        this.map = map;
        this.players = players;
        this.market = new Market();
        this.currentPlayerIndex = 0;
    }

    public void processCrisisForAIOnly() {
        for (Player p : players) {
            if (p instanceof PlayableAI) {
                if (isResourceBelowCrisisThreshold(p)) {
                    java.util.Map<ResourceType, Integer> discardMap = ((PlayableAI) p).getBrain().calculateResourcesToDiscard(p);
                    discardSelectedResources(p, discardMap);
                }
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map getMap() {
        return map;
    }

    public Market getMarket() {
        return market;
    }

    public void setBuildMode(BuildMode mode) {
        this.currentBuildMode = mode;
    }

    public BuildMode getCurrentBuildMode() {
        return currentBuildMode;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isSetupPlacedMVP() {
        return setupPlacedMVP;
    }

    public boolean isSetupPlacedPartnership() {
        return setupPlacedPartnership;
    }

    public void nextTurn(Runnable onTurnChanged) {
        Player winner = winnerPlayer();
        if (winner != null) {
            // TODO: هندل کردن پایان بازی
            return;
        }


        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player player = getCurrentPlayer();


        if (onTurnChanged != null) {
            javafx.application.Platform.runLater(onTurnChanged);
        }


        if (player instanceof PlayableAI) {
            ((PlayableAI) player).playTurn(this, () -> {
                nextTurn(onTurnChanged);
            });
        }
    }

    public void nextTurn() {
        nextTurn(null);
    }

    public Player winnerPlayer() {
        Player currentTurnPlayer = getCurrentPlayer();
        if (currentTurnPlayer.calculateVictoryPoints() >= 10) {
            return currentTurnPlayer;
        }
        List<Player> potentialWinners = new ArrayList<>();
        for (Player player : players) {
            if (player == currentTurnPlayer) continue;
            if (player.calculateVictoryPoints() >= 10) {
                potentialWinners.add(player);
            }
        }

        if (potentialWinners.isEmpty()) {
            return null;
        }
        Player winner = potentialWinners.get(0);
        for (Player player : potentialWinners) {
            if (player.calculateVictoryPoints() > winner.calculateVictoryPoints()) {
                winner = player;
            }
        }
        return winner;
    }

    public ArrayList<Integer> rollDice() {
        ArrayList<Integer> diceList = new ArrayList<>();
        diceList.add(random.nextInt(6) + 1);
        diceList.add(random.nextInt(6) + 1);
        return diceList;
    }

    public void distribute(ArrayList<Integer> diceList) {
        int activationNumber = diceList.get(0) + diceList.get(1);

        // بحران قانونی
        if (activationNumber == 7) {
            getCurrentPlayer().setCanPlaceAuditor(true);
            return;
        }
        Sector[][] sectors = map.getSectors();
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                if (sectors[r][c].getactivationNumber() == activationNumber && !sectors[r][c].isAuditor()) {
                    for (CornerDirection cornerDirection : CornerDirection.values())
                        if ((sectors[r][c].getCorner(cornerDirection)).getCompanyStructure() != null)
                            (sectors[r][c].getCorner(cornerDirection)).getCompanyStructure().produce(sectors[r][c]);
                }
            }
        }
    }

    public boolean isResourceBelowCrisisThreshold(Player player) {
        int totalResources = 0;
        java.util.Map<ResourceType, Integer> resourceCount = player.getResourceCount();
        for (int count : resourceCount.values()) {
            totalResources += count;
        }

        int threshold = player.getCrisisModifierThreshold();

        return totalResources > threshold;

    }

    public boolean discardSelectedResources(Player player, java.util.Map<ResourceType, Integer> resourcesToDiscard) {
        if (player == null || resourcesToDiscard == null) return false;

        java.util.Map<ResourceType, Integer> playerInventory = player.getResourceCount();
        for (java.util.Map.Entry<ResourceType, Integer> entry : resourcesToDiscard.entrySet()) {
            if (entry.getValue() < 0) return false;
            if (playerInventory.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }

        for (java.util.Map.Entry<ResourceType, Integer> entry : resourcesToDiscard.entrySet()) {
            if (entry.getValue() > 0) {
                player.deductResource(entry.getKey(), entry.getValue());
            }
        }

        return true;
    }

    public boolean canBuildMVP(int row, int col) {
        if (setupPhaseActive && setupPlacedMVP) return false;

        Vertex[][] vertices = map.getVertices();
        Vertex vertex = vertices[row][col];
        if (vertex.getCompanyStructure() != null) return false;

        for (Edge edge : vertex.getAdjacentEdges()) {
            if (edge.getOppositeVertex(vertex).getCompanyStructure() != null &&
                    edge.getOppositeVertex(vertex).getCompanyStructure().getOwner() != getCurrentPlayer()) return false;
        }
        return true;
    }

    public void buildMVP(Vertex vertex, Player player) {
        if (vertex.getCompanyStructure() != null) {
            throw new InvalidPlacementException(vertex, "A company has already been built on this vertex");

        }
        for (Edge edge : vertex.getAdjacentEdges()) {
            Vertex opposite = edge.getOppositeVertex(vertex);
            if (opposite != null && opposite.getCompanyStructure() != null &&
                    opposite.getCompanyStructure().getOwner() != getCurrentPlayer()) {
                throw new InvalidPlacementException(vertex, "Placement violation! You cannot build in the immediate neighborhood of an existing company.");
            }
        }

        if (!setupPhaseActive) player.deductResourcesForMVP();
        MVP mvp = new MVP(player);
        vertex.setCompanyStructure(mvp);
        player.addCompanyStructure(mvp);

        // TODO : Show MPV created successfully
    }

    public boolean canPlaceAuditor(Sector sector) {
        if (sector == null) return false;
        if (sector.isAuditor()) return false;

        if (sector.hasAnyCompanyOnSector()) return true;

        Sector[][] sectors = map.getSectors();
        int mapRows = map.getRows();
        int mapCols = map.getCols();
        for (int r = 0; r < mapRows; r++) {
            for (int c = 0; c < mapCols; c++) {
                if (sectors[r][c].hasAnyCompanyOnSector()) return false;
            }
        }
        return true;
    }

    public boolean moveAuditor(Sector sector) {
        if (sector == null) return false;
        if (!canPlaceAuditor(sector)) return false;

        Sector[][] sectors = map.getSectors();
        int mapRows = map.getRows();
        int mapCols = map.getCols();
        for (int r = 0; r < mapRows; r++) {
            for (int c = 0; c < mapCols; c++) {
                if (sectors[r][c].isAuditor()) {
                    sectors[r][c].setAuditor(false);
                    sector.setAuditor(true);
                    return true;
                }
            }
        }
        sector.setAuditor(true);
        return true;
    }

    public boolean canBuildPartnership(Player player, Edge edge) {
        if (edge == null || player == null) return false;
        if (edge.getPartnership() != null) return false;

        if (setupPhaseActive && setupPlacedPartnership) return false;

        Vertex startVertex = edge.getStart();
        Vertex endVertex = edge.getEnd();

        if (startVertex != null && startVertex.getCompanyStructure() != null) {
            if (startVertex.getCompanyStructure().getOwner() == player) {
                return true;
            }
        }

        if (endVertex != null && endVertex.getCompanyStructure() != null) {
            if (endVertex.getCompanyStructure().getOwner() == player) {
                return true;
            }
        }

        if (startVertex != null) {
            for (Edge e : startVertex.getAdjacentEdges()) {
                if (e != edge && e.getPartnership() != null) {
                    if (e.getPartnership().getOwner() == player) return true;
                }
            }
        }

        if (endVertex != null) {
            for (Edge e : endVertex.getAdjacentEdges()) {
                if (e != edge && e.getPartnership() != null) {
                    if (e.getPartnership().getOwner() == player) return true;
                }
            }
        }

        return false;
    }

    public void buildPartnership(Player player, Edge edge) {

        if (edge.getPartnership() != null)
            throw new InvalidPlacementException(edge, "Placement violation! This edge already has a partnership on it.");
        if (!canBuildPartnership(player, edge))
            throw new InvalidPlacementException(edge, "Placement violation! Partnership must connect to your existing companies or partnerships.");

        if (!setupPhaseActive) player.deductResourcesForPartnership();
        edge.setPartnership(new Partnership(player));
        // TODO : Show Partnership created successfully
    }


    public boolean canUpgradeToUnicorn(int row, int col) {
        if (setupPhaseActive) return false;
        Vertex[][] vertices = map.getVertices();
        Vertex vertex = vertices[row][col];
        if (vertex.getCompanyStructure() instanceof MVP) {
            return vertex.getCompanyStructure().getOwner() == getCurrentPlayer();
        }
        return false;
    }

    public void upgradeToUnicorn(Vertex vertex,Player player) {
        if(vertex.getCompanyStructure()==null||vertex.getCompanyStructure().getOwner()!=getCurrentPlayer() ){
            throw new InvalidPlacementException(vertex,"You can't make unicorns here");
        }
        player.deductResourcesForUnicornUpgrade();
        CompanyStructure oldMvp = vertex.getCompanyStructure();
        Unicorn newUnicorn = new Unicorn(player);
        vertex.setCompanyStructure(newUnicorn);
        player.removeCompanyStructure(oldMvp);
        player.addCompanyStructure(newUnicorn);

    }


    public int DFS(Vertex currentVertex, Player player, Set<Edge> visitedEdges) {

        if (currentVertex.getCompanyStructure() != null && currentVertex.getCompanyStructure().getOwner() != player) {
            return 0;
        }
        int maxSubPathLength = 0;

        for (Edge edge : currentVertex.getAdjacentEdges()) {
            if (edge.getPartnership() != null && edge.getPartnership().getOwner() == player) {
                if (!visitedEdges.contains(edge)) {
                    Vertex destination = edge.getOppositeVertex(currentVertex);

                    visitedEdges.add(edge);
                    int subPathLength = DFS(destination, player, visitedEdges) + 1;
                    if (subPathLength > maxSubPathLength) {
                        maxSubPathLength = subPathLength;
                    }
                    visitedEdges.remove(edge);
                }
            }
        }
        return maxSubPathLength;
    }

    public void updateLongestNetwork() {
        Player currentPartnershipHolder = null;

        for (Player player : players) {
            if (player.isHasLongestNetwork()) {
                currentPartnershipHolder = player;
                break;
            }
        }

        int maxSubPathLength = 2;
        if (currentPartnershipHolder != null) {
            maxSubPathLength = calculateLongestPathForPlayer(currentPartnershipHolder);
        }
        Player newLongestPartnershipHolder = currentPartnershipHolder;

        for (Player player : players) {
            if (player == currentPartnershipHolder) continue;
            int playerPath = calculateLongestPathForPlayer(player);
            if (playerPath > maxSubPathLength) {
                maxSubPathLength = playerPath;
                newLongestPartnershipHolder = player;
            }
        }
        if (newLongestPartnershipHolder != currentPartnershipHolder) {
            for (Player player : players) {
                player.setHasLongestNetwork(false);
            }
            if (newLongestPartnershipHolder != null) {
                newLongestPartnershipHolder.setHasLongestNetwork(true);
            }
        }

    }

    private int calculateLongestPathForPlayer(Player player) {
        int maxPath = 0;
        for (Vertex[] row : map.getVertices()) {
            if (row == null) continue;
            for (Vertex vertex : row) {
                if (vertex == null) continue;

                boolean isCandidate = false;
                for (Edge e : vertex.getAdjacentEdges()) {
                    if (e.getPartnership() != null && e.getPartnership().getOwner() == player) {
                        isCandidate = true;
                        break;
                    }
                }
                if (isCandidate) {
                    Set<Edge> visitedEdges = new HashSet<>();
                    int currentVertexMax = DFS(vertex, player, visitedEdges);
                    if (currentVertexMax > maxPath) {
                        maxPath = currentVertexMax;
                    }
                }
            }
        }
        return maxPath;
    }

    public boolean isSetupPhaseActive() {
        return setupPhaseActive;
    }

    public void setSetupPhaseActive(boolean setupPhaseActive) {
        this.setupPhaseActive = setupPhaseActive;
    }

    public int getSetupRound() {
        return setupRound;
    }

    public void setSetupRound(int setupRound) {
        this.setupRound = setupRound;
    }

    public int getSetupDirection() {
        return setupDirection;
    }

    public void setSetupDirection(int setupDirection) {
        this.setupDirection = setupDirection;
    }

    public int getSetupTurnCount() {
        return setupTurnCount;
    }

    public void setSetupTurnCount(int setupTurnCount) {
        this.setupTurnCount = setupTurnCount;
    }

    public void startSetupPhase() {
        setupPhaseActive = true;
        setupRound = 0;
        setupDirection = 1;
        currentPlayerIndex = 0;
        setupTurnCount = 0;
    }

    public boolean isSetupPhase() {
        return setupPhaseActive;
    }

    public void notifyMVPPlaced() {
        setupPlacedMVP = true;
        checkAndMoveToNextSetupTurn();

    }

    public void notifyPartnershipPlaced() {
        setupPlacedPartnership = true;
        checkAndMoveToNextSetupTurn();


    }

    public List<Integer> calculatePlayerPoints() {
        List<Integer> pointList = new ArrayList<>();
        updateLongestNetwork();
        for (Player player : players) {
            pointList.add(player.calculateVictoryPoints());
        }
        return pointList;
    }

    public void checkAndMoveToNextSetupTurn() {
        if (setupPlacedMVP && setupPlacedPartnership) {
            setupTurnCount++;

            setupPlacedMVP = false;
            setupPlacedPartnership = false;

            if (setupTurnCount == players.size() * 2) {
                setupPhaseActive = false;
                distributeSetupResources();


                mainPhaseActive = true;
                setupRound = 2;
                setupDirection = 1;
                currentPlayerIndex = 0;
                isDiceRolled = false;
                triggerNextPlayerIfAI();
                return;
            }
            if (setupRound == 0 && currentPlayerIndex == players.size() - 1) {
                setupRound = 1;
                setupDirection = -1;
            } else {
                currentPlayerIndex += setupDirection;
            }
            triggerNextPlayerIfAI();

        }
    }

    private void triggerNextPlayerIfAI() {
        Player nextPlayer = getCurrentPlayer();
        if (nextPlayer instanceof PlayableAI) {
            javafx.application.Platform.runLater(() -> {
                ((PlayableAI) nextPlayer).playTurn(this, null);
            });
        }
    }

    public void endCurrentTurn() {
        if (setupPhaseActive) {
            throw new IllegalStateException("Cannot end normal turn during setup phase.");
        }

        int previousPlayerIndex = currentPlayerIndex;
        nextTurn();
        currentTurnNumber = turnNumber;
        if (currentPlayerIndex == 0 && previousPlayerIndex == players.size() - 1) {
            turnNumber++;
            market.updateMarketAtEndOfRound();
        }

        isDiceRolled = false;
        currentBuildMode = BuildMode.NONE;
    }


    public boolean isMainPhaseActive() {
        return mainPhaseActive;
    }

    public void setMainPhaseActive(boolean mainPhaseActive) {
        this.mainPhaseActive = mainPhaseActive;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isDiceRolled() {
        return isDiceRolled;
    }

    public void setIsDiceRolled(boolean canRollDiceThisTurn) {
        this.isDiceRolled = canRollDiceThisTurn;
    }

    public ArrayList<Integer> rollDiceForCurrentTurn() {
        if (setupPhaseActive) {
            throw new IllegalStateException("You cannot roll dice during setup phase.");
        }

        if (isDiceRolled) {
            throw new IllegalStateException("You have rolled the dice.");
        }

        ArrayList<Integer> dice = rollDice();
        isDiceRolled = true;
        distribute(dice);

        return dice;
    }

    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    public void setCurrentTurnNumber(int currentTurnNumber) {
        this.currentTurnNumber = currentTurnNumber;
    }

    public void distributeSetupResources() {
        Sector[][] sectors = map.getSectors();
        for (Sector[] row : sectors) {
            for (Sector sector : row) {
                for (CornerDirection corner : CornerDirection.values()) {
                    if (sector.getCorner(corner) != null) {
                        Vertex v = sector.getCorner(corner);
                        if (v.getCompanyStructure() != null && sector.getResourceType() != ResourceType.REGULATORY) {
                            v.getCompanyStructure().getOwner().addResource(sector.getResourceType(), 1);
                        }
                    }
                }
            }
        }
    }

    public void trade(java.util.Map<ResourceType, Integer> givenResources, java.util.Map<ResourceType, Integer> receiveResources, Player... players) {
        for (ResourceType type : ResourceType.values()) {
            if (type == ResourceType.REGULATORY) continue;
            int giveAmount = givenResources.getOrDefault(type, 0);
            int receiveAmount = receiveResources.getOrDefault(type, 0);


            int currentP1 = players[0].getResourceCount().getOrDefault(type, 0);
            players[0].getResourceCount().put(type, currentP1 - giveAmount + receiveAmount);


            int currentP2 = players[1].getResourceCount().getOrDefault(type, 0);
            players[1].getResourceCount().put(type, currentP2 + giveAmount - receiveAmount);
        }
    }
}