package logic.models;

public class Partnership {
    private final Player owner;

    public Partnership(Player owner){
        this.owner = owner;
    }

    public void buildPartnership(Player player, Edge edge){
        if (!player.hasResourcesForPartnership()) {
            // TODO : Show not enough Resources (or don't let the player to click on create a partnership button)
            return;
        }

        if (edge.getPartnership() != null){
            // TODO : Show : This edge has a partnership already
            return;
        }

        // قرار دادن پارتنرشیپ جدید روی یال و کم کردن منابع لازم برای ساخت آن از بازیکن
        edge.setPartnership(new Partnership(player));
        player.deductResourcesForPartnership();

        // TODO : Show : Partnership created successfully
    }
}
