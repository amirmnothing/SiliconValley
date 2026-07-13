package exception;

public class PlayerTypeNotSelectedException extends RuntimeException {
    private final int playerNumber;

    public PlayerTypeNotSelectedException(int playerNumber) {
        super(String.format(
                "Player type for player %d is not selected. " +
                        "Each player must be assigned a type (Human or AI) before the game can proceed. " +
                        "Please select a valid player type for player %d.",
                playerNumber, playerNumber
        ));
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}