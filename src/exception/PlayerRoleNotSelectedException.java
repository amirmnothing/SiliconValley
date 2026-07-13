package exception;

public class PlayerRoleNotSelectedException extends RuntimeException {
    private final int playerNumber;

    public PlayerRoleNotSelectedException(int playerNumber) {
        super(String.format(
                "Player role for player %d is not selected. " +
                        "Each player must be assigned a role before the game can proceed. " +
                        "Please select a valid role for player %d.",
                playerNumber, playerNumber
        ));
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}