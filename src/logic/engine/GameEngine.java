package logic.engine;

import exception.InsufficientResourcesException;
import exception.InvalidPlacementException;
import logic.enums.CornerDirection;
import logic.enums.ResourceType;
import logic.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private final Map map;
    private final List<Player> players;
    private int currentPlayerIndex;
    private final Random random = new Random();


    public GameEngine(Map map, List<Player> players) {
        this.map = map;
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public void nextTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public ArrayList<Integer> rollDice(){
        ArrayList<Integer> diceList = new ArrayList<>();
        diceList.add(random.nextInt(6) + 1);
        diceList.add(random.nextInt(6) + 1);
        return diceList;
    }

    public void distribute(ArrayList<Integer> diceList){
        int activationNumber = diceList.get(0) + diceList.get(1);

        // بحران قانونی
        if (activationNumber == 7){
            handleSevenOrCrisis();
            return;
        }

        Sector[][] sectors =  map.getSectors();
        for (int r = 0; r < map.getRows(); r++){
            for (int c = 0; c < map.getCols(); c++){
                if (sectors[r][c].getactivationNumber() == activationNumber && !sectors[r][c].isAuditor()){
                    for (CornerDirection cornerDirection : CornerDirection.values())
                        if ((sectors[r][c].getCorner(cornerDirection)).getCompanyStructure() != null) (sectors[r][c].getCorner(cornerDirection)).getCompanyStructure().produce(sectors[r][c]);
                }
            }
        }
    }

    public void handleSevenOrCrisis(){
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

    public boolean discardSelectedResources(Player player, java.util.Map<ResourceType, Integer> resourcesToDiscard){
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

    public boolean canBuildMVP(int row,int col) {
        Vertex[][] vertices = map.getVertices();
        Vertex vertex = vertices[row][col];
        if (vertex.getCompanyStructure() != null) return false;
        for (Edge edge : vertex.getAdjacentEdges()) {
            if (edge.getOppositeVertex(vertex).getCompanyStructure() != null) return false;
        }
        return true;
    }

    public void buildMVP(Vertex vertex, Player player){
        if (vertex.getCompanyStructure() != null){
            throw new InvalidPlacementException(vertex,"A company has already been built on this vertex");

        }
        for (Edge edge : vertex.getAdjacentEdges()) {
            Vertex opposite = edge.getOppositeVertex(vertex);
            if (opposite != null && opposite.getCompanyStructure() != null) {
                throw new InvalidPlacementException(vertex, "Placement violation! You cannot build in the immediate neighborhood of an existing company.");
            }
        }

        // قرار دادن MVP گره و کم کردن منابع لازم برای ساخت آن از بازیکن
        player.deductResourcesForMVP();
        vertex.setCompanyStructure(new MVP(player));

        // TODO : Show MPV created successfully

    }

    public boolean canPlaceAuditor(Sector sector){
        if (sector == null) return false;
        if (sector.isAuditor()) return false;

        if (sector.hasAnyCompanyOnSector()) return true;

        Sector[][] sectors = map.getSectors();
        int mapRows = map.getRows();
        int mapCols = map.getCols();
        for (int r = 0; r < mapRows; r++){
            for (int c = 0; c < mapCols; c++){
                if (sectors[r][c].hasAnyCompanyOnSector()) return false;
            }
        }
        return true;
    }

    public boolean moveAuditor(Sector sector){
        if (sector == null) return false;
        if (!canPlaceAuditor(sector)) return false;

        Sector[][] sectors = map.getSectors();
        int mapRows = map.getRows();
        int mapCols = map.getCols();
        for (int r = 0; r < mapRows; r++){
            for (int c = 0; c < mapCols; c++){
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

    public void buildPartnership(Player player, Edge edge){

        if (edge.getPartnership() != null){
            throw new InvalidPlacementException(edge,"Placement violation! This edge already has a partnership on it.");
        }

        // قرار دادن پارتنرشیپ جدید روی یال و کم کردن منابع لازم برای ساخت آن از بازیکن
        player.deductResourcesForPartnership();
        edge.setPartnership(new Partnership(player));


        // TODO : Show Partnership created successfully
    }
}