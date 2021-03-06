package us.centile.hcfactions.profile.fight.killer.type;

import us.centile.hcfactions.profile.fight.ProfileFightEnvironment;
import us.centile.hcfactions.profile.fight.killer.ProfileFightKiller;
import lombok.Getter;

public class ProfileFightEnvironmentKiller extends ProfileFightKiller {

    @Getter private final ProfileFightEnvironment type;

    public ProfileFightEnvironmentKiller(ProfileFightEnvironment type) {
        super(null, null);
        this.type = type;
    }
}
