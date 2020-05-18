package us.centile.hcfactions.factions.events;

import lombok.Getter;
import us.centile.hcfactions.factions.type.PlayerFaction;

@Getter
public class FactionEnemyFactionEvent extends FactionEvent {

    private PlayerFaction[] factions;

    public FactionEnemyFactionEvent(PlayerFaction[] factions) {
        this.factions = factions;
    }

}
