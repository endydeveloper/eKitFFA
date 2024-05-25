package me.endydev.ffa.translation.linguist;

import com.zelicraft.core.spigot.api.CoreAPI;
import lombok.RequiredArgsConstructor;
import me.yushust.message.language.Linguist;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@RequiredArgsConstructor
public class UUIDLinguist implements Linguist<UUID> {
    private final CoreAPI coreAPI;

    @Override
    public @Nullable String getLanguage(UUID sender) {
        return coreAPI.getPlayerLanguage(sender).getCode();
    }
}