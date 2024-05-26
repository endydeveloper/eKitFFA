package me.endydev.ffa.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.UUID;

@AllArgsConstructor(staticName = "of")
@Getter
public class TemporalBlock {
    private final String id;
    private final Block block;
    private final Location location;
    private final long until;
}
