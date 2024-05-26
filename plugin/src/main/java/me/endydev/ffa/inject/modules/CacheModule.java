package me.endydev.ffa.inject.modules;

import com.zelicraft.commons.shared.cache.LocalObjectCache;
import com.zelicraft.commons.shared.cache.ObjectCache;
import me.endydev.ffa.api.data.TemporalBlock;
import me.endydev.ffa.api.data.TemporalDropItem;
import me.endydev.ffa.api.data.kit.KitCreate;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.perks.*;
import me.endydev.ffa.service.menu.dto.ActiveMenu;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Named;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

import java.util.*;

public class CacheModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(Perk.class)
                .asMap(PerkType.class)
                .bind(PerkType.BOW)
                .to(BowPerk.class)

                .bind(PerkType.ARROW)
                .to(ArrowPerk.class)

                .bind(PerkType.GOLDEN_HEAD)
                .to(GoldenHeadPerk.class)

                .bind(PerkType.FISHING_ROD)
                .to(FishingRodPerk.class)
                .singleton();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, TemporalDropItem> provideTempoDropItem() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<String, TemporalBlock> provideTempBlock() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, KitCreate> provideCreateKitCache() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, TagPlayer> provideTaggedPlayers() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, Integer> provideSpawnCooldown() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, Boolean> provideBuildMode() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    public Map<UUID, List<Class<? extends CoreBaseMenu>>> provideBackHistory() {
        return new HashMap<>();
    }

    @Singleton
    @Provides
    @Named("click")
    public List<UUID> provideClickBackMenu() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    @Named("ignoreClick")
    public List<UUID> provideIgnoreClickMenu() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    public ObjectCache<UUID, ActiveMenu> provideMenuCache() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    public ObjectCache<UUID, Object[]> provideMenuObjectsCache() {
        return new LocalObjectCache<>();
    }

    @Singleton
    @Provides
    private ObjectCache<UUID, BukkitTask> provideTask() {
        return new LocalObjectCache<>();
    }
}
