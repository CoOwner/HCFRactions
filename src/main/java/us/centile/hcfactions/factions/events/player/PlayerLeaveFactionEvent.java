package us.centile.hcfactions.factions.events.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.centile.hcfactions.factions.events.FactionEvent;
import us.centile.hcfactions.factions.type.PlayerFaction;

@Getter
public class PlayerLeaveFactionEvent extends FactionEvent {

    private PlayerFaction faction;
    private Player player;

    public PlayerLeaveFactionEvent(Player player, PlayerFaction faction) {
        this.player = player;
        this.faction = faction;
    }

}
