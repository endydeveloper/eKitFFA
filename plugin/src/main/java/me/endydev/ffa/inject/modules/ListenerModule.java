package me.endydev.ffa.inject.modules;

import me.endydev.ffa.listeners.NPCListener;
import me.endydev.ffa.listeners.RegionListener;
import me.endydev.ffa.listeners.game.interact.GameHitArrowListener;
import me.endydev.ffa.listeners.game.join.GameJoinListener;
import me.endydev.ffa.listeners.game.player.*;
import me.endydev.ffa.listeners.game.player.death.GameDeathMessageListener;
import me.endydev.ffa.listeners.game.player.death.GameRespawnListener;
import me.endydev.ffa.listeners.game.player.move.GameMoveListener;
import me.endydev.ffa.listeners.game.quit.*;
import me.endydev.ffa.listeners.game.world.*;
import org.bukkit.event.Listener;
import team.unnamed.inject.AbstractModule;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(Listener.class)
                .asSet()

                .to(GameHitArrowListener.class)
                .to(GameJoinListener.class)
                .to(GameMoveListener.class)

                .to(GameInteractEnderChestListener.class)
                .to(GamePlayerBreakBlockListener.class)
                .to(GamePlayerDeathListener.class)
                .to(GameRespawnListener.class)
                .to(GameDeathMessageListener.class)
                .to(GamePlayerDropItemListener.class)
                .to(GamePlayerInteractListener.class)
                //.to(GamePlayerPickupListener.class)
                .to(GamePlayerPlaceBlockListener.class)
                .to(GameDataQuitListener.class)
                .to(GameItemsQuitListener.class)
                .to(GameQuitSpawnListener.class)
                .to(GameRegionRemoveListener.class)
                .to(GameTagQuitListener.class)
                .to(GameCreatureSpawnListener.class)
                .to(GameEntityDamageListener.class)
                .to(GameEntityExplodeListener.class)
                .to(GameFlowLavaListener.class)
                .to(GameWeatherListener.class)

                .to(RegionListener.class)
                .to(NPCListener.class)
                .singleton();

    }
}
