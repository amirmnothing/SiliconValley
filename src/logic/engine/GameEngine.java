package logic.engine;

import logic.enums.CornerDirection;
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
        Sector[][] sectors =  map.getSectors();
        for (int r = 0; r < map.getRows(); r++){
            for (int c = 0; c < map.getCols(); c++){
                if (sectors[r][c].getactivationNumber() == activationNumber && !sectors[r][c].isInspector()){
                    for (CornerDirection cornerDirection : CornerDirection.values())
                        if ((sectors[r][c].getCorner(cornerDirection)).getCompanyStructure() != null) (sectors[r][c].getCorner(cornerDirection)).getCompanyStructure().produce(sectors[r][c]);
                }
            }
        }
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
        if (!player.hasResourcesForMVP()){
            // TODO : Show not enough Resources (or don't let the player to click on create a partnership button)
            return;
        }
        if (vertex.getCompanyStructure() != null){
            // TODO : Show : This vertex has a company on it already
            return;
        }

        // قرار دادن MVP گره و کم کردن منابع لازم برای ساخت آن از بازیکن
        vertex.setCompanyStructure(new MVP(player));
        player.deductResourcesForMVP();

        // TODO : Show MPV created successfully

    }

    public void buildPartnership(Player player, Edge edge){
        if (!player.hasResourcesForPartnership()) {
            // TODO : Show not enough Resources (or don't let the player to click on create a partnership button)
            return;
        }

        if (edge.getPartnership() != null){
            // TODO : Show : This edge has a partnership on it already
            return;
        }

        // قرار دادن پارتنرشیپ جدید روی یال و کم کردن منابع لازم برای ساخت آن از بازیکن
        edge.setPartnership(new Partnership(player));
        player.deductResourcesForPartnership();

        // TODO : Show Partnership created successfully
    }
}