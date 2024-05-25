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


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        if (cause == null) return;

        TagPlayer tagPlayer = gameManager.getPlayerTag(player);

        if (tagPlayer == null) return;

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

            utils.addXP(lastAttacker);
            utils.addGold(lastAttacker);
            utils.killStreak(lastAttacker);
            gameManager.givePerk(lastAttacker);
            lastAttacker.updateInventory();
        }

        switch (cause) {
            case VOID:
                List<String> playerCause = messageHandler.getMany(player, "death-messages.void.player");
                List<String> soloCause = messageHandler.getMany(player, "death-messages.void.solo");
                if (hasAttackers) {
                    utils.assitKill(player);
                    versionSupport.playAction(tagPlayer.getLastAttacker(),
                            utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    event.setDeathMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, gameManager.getPlayerTag(player).getLastAttacker()));
                } else {
                    event.setDeathMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                }
                break;
            case ENTITY_ATTACK:
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.assitKill(player);
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    playerCause = messageHandler.getMany(player, "death-messages.player");
                    event.setDeathMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, gameManager.getPlayerTag(player).getLastAttacker()));
                }
                break;
            case FALL:
                playerCause = messageHandler.getMany(player, "deathMessages.fall.player");
                soloCause = messageHandler.getMany(player, "deathMessages.fall.solo");
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.assitKill(player);
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    event.setDeathMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, gameManager.getPlayerTag(player).getLastAttacker()));
                } else {
                    event.setDeathMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                }
                break;
            case LAVA:
                playerCause = messageHandler.getMany(player, "death-messages.lava.player");
                soloCause = messageHandler.getMany(player, "death-messages.lava.solo");
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.assitKill(player);
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    event.setDeathMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player));
                } else {
                    event.setDeathMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                }
                break;
            case PROJECTILE: {
                EntityDamageByEntityEvent f = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                if (f.getDamager() instanceof Arrow) {
                    if (hasAttackers) {
                        versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                        utils.assitKill(player);
                        utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                        List<String> bowCause = messageHandler.getMany(player, "death-messages.projectile.bow.player");
                        event.setDeathMessage(utils.replaceDeathMessage(bowCause.get(Utils.randomInt(0, bowCause.size() - 1)), player, gameManager.getPlayerTag(player).getLastAttacker()));
                    } else {
                        List<String> bowCause = messageHandler.getMany(player, "death-messages.projectile.bow.solo");
                        event.setDeathMessage(utils.replaceDeathMessage(bowCause.get(Utils.randomInt(0, bowCause.size() - 1)), player));
                    }
                }
                break;
            }

            default: {
                List<String> unknownCause = messageHandler.getMany(player, "death-messages.unknown");
                event.setDeathMessage(utils.replaceDeathMessage(unknownCause.get(Utils.randomInt(0, unknownCause.size() - 1)), player));
            }
        }
    }
}
