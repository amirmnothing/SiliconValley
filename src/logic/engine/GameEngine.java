package logic.engine;

import exception.InvalidPlacementException;
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

    private boolean setupPhaseActive;
    private int setupRound;
    private int setupDirection; //1 یعنی دور اول رو بجلو 1- یعنی دور دوم رو به عقب
    private boolean setupPlacedMVP;//TODO
    private boolean setupPlacedPartnership;//TODO
    private int setupTurnCount;

    private BuildMode currentBuildMode = BuildMode.NONE;


    private boolean canRollDiceThisTurn = false;
    private boolean mainPhaseActive = false;
    private int turnNumber = 1;
    private int currentTurnNumber = 1;

    public GameEngine(Map map, List<Player> players) {
        this.map = map;
        this.players = players;
        this.market = new Market();
        this.currentPlayerIndex = 0;
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

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

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
            handleSevenOrCrisis();
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

    public void handleSevenOrCrisis() {
        for (Player player : players) {
            // محاسبه کل کارت‌های منبع بازیکن در حال حاضر
            int totalResources = 0;
            java.util.Map<ResourceType, Integer> resourceCount = player.getResourceCount();
            for (int count : resourceCount.values()) {
                totalResources += count;
            }

            int threshold = player.getCrisisModifierThreshold();

            if (totalResources > threshold) {
                int countOfCardsToDiscard = totalResources / 2;
                // TODO : نمایش پیام و باز شدن پنجره برای انتخاب کارت ها و فراخوانی متد discardSelectedResources
            }
        }
    }

    public boolean discardSelectedResources(Player player, java.util.Map<ResourceType, Integer> resourcesToDiscard) {
        if (player == null || resourcesToDiscard == null) return false;

        // بررسی تعداد کارت های بازیکن و تعداد انتخاب شده برای جلوگیری از تقلب یا ...
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

        // قرار دادن MVP گره و کم کردن منابع لازم برای ساخت آن از بازیکن
        if (!setupPhaseActive) player.deductResourcesForMVP();
        vertex.setCompanyStructure(new MVP(player));

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
            if (playerPath > maxSubPathLength) {//احتماال وجود خطا
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

    //متد های مربوط به فاز شروع بازی
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

    public void checkAndMoveToNextSetupTurn() {
        if (setupPlacedMVP && setupPlacedPartnership) {
            setupTurnCount++;

            setupPlacedMVP = false;
            setupPlacedPartnership = false;

            if (setupTurnCount == players.size() * 2) {
                setupPhaseActive = false;
                mainPhaseActive = true;
                setupRound = 2;
                setupDirection = 1;
                currentPlayerIndex = 0;
                canRollDiceThisTurn = false;
                return;
            }
            if (setupRound == 0 && currentPlayerIndex == players.size() - 1) {
                setupRound = 1;
                setupDirection = -1;
            } else {
                currentPlayerIndex += setupDirection;
            }

        }
    }

    public void endCurrentTurn() {
//        if (setupPhaseActive) {
//            throw new IllegalStateException("Cannot end normal turn during setup phase.");
//        }
        int previousPlayerIndex = currentPlayerIndex;
        nextTurn();
        currentTurnNumber = turnNumber;
        if(currentPlayerIndex == players.size() - 1) {market.updateMarketAtEndOfRound();}
        if (currentPlayerIndex == 0 && previousPlayerIndex == players.size() - 1) {
            turnNumber++;

//
        }

        canRollDiceThisTurn = false;
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

    public boolean isCanRollDiceThisTurn() {
        return canRollDiceThisTurn;
    }

    public void setCanRollDiceThisTurn(boolean canRollDiceThisTurn) {
        this.canRollDiceThisTurn = canRollDiceThisTurn;
    }

    public ArrayList<Integer> rollDiceForCurrentTurn() {
        if (setupPhaseActive) {
            throw new IllegalStateException("You cannot roll dice during setup phase.");
        }

        if (canRollDiceThisTurn) {
            throw new IllegalStateException("You have rolled the dice.");
        }

        ArrayList<Integer> dice = rollDice();
        canRollDiceThisTurn = true;
        distribute(dice);

        return dice;
    }

    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    public void setCurrentTurnNumber(int currentTurnNumber) {
        this.currentTurnNumber = currentTurnNumber;
    }
}