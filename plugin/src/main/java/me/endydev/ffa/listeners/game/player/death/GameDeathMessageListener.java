package me.endydev.ffa.listeners.game.player.death;

import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import team.unnamed.inject.Inject;

import java.util.List;

public class GameDeathMessageListener implements Listener {
    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private VersionSupport versionSupport;

    @Inject
    private Utils utils;

    @Inject
    private ConfigFile configFile;

    @Inject
    private GameManager gameManager;

    @Inject
    private MessageHandler messageHandler;


    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        if (cause == null) {
            return;
        }

        TagPlayer tagPlayer = gameManager.getPlayerTag(player);

        if (tagPlayer == null) {
            return;
        }

        boolean hasAttackers = tagPlayer.hasAttackers();

        if (hasAttackers) {
            Player lastAttacker = tagPlayer.getLastAttacker();
            playerDataManager.getPlayer(lastAttacker.getUniqueId())
                    .ifPresent(attackerData -> {
                        attackerData.addKillStreak();
                        attackerData.addKills();
                        if (attackerData.getKillStreak() > attackerData.getMaxKs()) {
                            attackerData.setMaxKillStreak(attackerData.getKillStreak());
                        }
                    });

            gameManager.rewardPlayer(lastAttacker, GameManager.RewardType.KILL);
            if(tagPlayer.getAssister() != null) {
                gameManager.assitKill(player);
            }
            utils.killStreak(lastAttacker);
            gameManager.givePerk(lastAttacker);
            lastAttacker.updateInventory();
        }

        gameManager.executeDeathMessage(player, cause, event.getEntity().getLastDamageCause().getEntity());
    }
}
