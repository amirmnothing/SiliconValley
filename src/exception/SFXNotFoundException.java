package exception;

public class SFXNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public SFXNotFoundException() {
        super("SFX not found");
    }

    public SFXNotFoundException(String Message) {
        super("Couldn't find \"" + Message + "\" sound effect");
    }
}