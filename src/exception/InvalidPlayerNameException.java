package exception;

public class InvalidPlayerNameException extends RuntimeException {
    private final int playerNumber;
    private final String invalidName;

    public InvalidPlayerNameException(int playerNumber, String invalidName) {
        super(String.format(
                "Invalid name for player %d: \"%s\". " +
                        "Please enter a valid name for player %d.",
                playerNumber, invalidName, playerNumber
        ));
        this.playerNumber = playerNumber;
        this.invalidName = invalidName;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getInvalidName() {
        return invalidName;
    }
}