package me.endydev.ffa.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Item;

import java.util.UUID;

@AllArgsConstructor(staticName = "of")
@Data(staticConstructor = "of")
public class TemporalDropItem {
    private final UUID uuid;
    private final Item item;
    private final long until;
}
