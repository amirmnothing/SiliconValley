package exception;

public class MusicFileNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public MusicFileNotFoundException() {
        super("Music file not found");
    }

    public MusicFileNotFoundException(String Message) {
        super("Couldn't find \"" + Message + "\" music");
    }
}