package me.endydev.ffa.managers;

import lombok.Getter;
import lombok.Setter;
import me.endydev.ffa.utils.Cuboid;
import org.bukkit.Location;

public class RegionManager {

    @Getter @Setter
    public Cuboid region;
    @Getter @Setter
    public Location first;
    @Getter @Setter
    public Location second;

    public RegionManager() {
        region = null;
    }

}
