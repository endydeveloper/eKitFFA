package me.endydev.ffa.listeners.game.player.move;

import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.Inject;

public class GameMoveListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private Utils utils;

    @Inject
    private ConfigFile configFile;

    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        double x = Math.floor(from.getX());
        double z = Math.floor(from.getZ());
        double y = Math.floor(from.getY());

        Location to = event.getTo();
        if (Math.floor(to.getY()) < configFile.getInt("prevoid-kill")) {
            EntityDamageEvent e = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, player.getHealth());
            player.setLastDamageCause(e);
            player.setHealth(0);
            return;
        }

        BukkitTask task = gameManager.getTask(player.getUniqueId());

        if(task == null) {
            return;
        }

        if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z || Math.floor(to.getY()) != y) {
            gameManager.getTask(player.getUniqueId()).cancel();
            gameManager.removeTask(player.getUniqueId());
            utils.sendTitle("titles.spawn-cancelled", player);
            utils.playSound(configFile.getConfigurationSection("sounds.spawn-cancelled"), player);
            messageHandler.send(player, "teleport.cancel");
        }
    }
}
