package us.centile.hcfactions.factions.events.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.events.FactionEvent;
import us.centile.hcfactions.profile.teleport.ProfileTeleportType;

@Getter
public class PlayerCancelFactionTeleportEvent extends FactionEvent {

    private Faction faction;
    private Player player;
    private ProfileTeleportType teleportType;

    public PlayerCancelFactionTeleportEvent(Player player, Faction faction, ProfileTeleportType teleportType) {
        this.player = player;
        this.faction = faction;
        this.teleportType = teleportType;
    }


}
