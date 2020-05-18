package us.centile.hcfactions.factions.events;

import lombok.Getter;
import us.centile.hcfactions.factions.type.PlayerFaction;

@Getter
public class FactionAllyFactionEvent extends FactionEvent {

    private PlayerFaction[] factions;

    public FactionAllyFactionEvent(PlayerFaction[] factions) {
        this.factions = factions;
    }

}
