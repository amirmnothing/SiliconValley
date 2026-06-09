package logic.models;

public class Partnership {
    private final Player owner;

    public Partnership(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
