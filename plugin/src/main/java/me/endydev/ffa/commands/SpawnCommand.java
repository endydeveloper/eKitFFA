package me.endydev.ffa.commands;

import com.zelicraft.commons.shared.cache.ObjectCache;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Join;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.Inject;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Command("spawn")
public class SpawnCommand extends BaseCommand {

    @Inject
    private FFAPlugin plugin;

    @Inject
    private VersionSupport versionSupport;

    @Inject
    private ConfigFile configFile;

    @Inject
    private GameManager gameManager;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private ObjectCache<UUID, Integer> cooldownCache;

    @Inject
    private Utils utils;


    @Default
    public void onCommand(CommandSender sender, @Join String params) {
        String[] args = params.split(" ");
        Player player = (Player) sender;
        if(!configFile.contains("spawn")) {
            messageHandler.send(player,  "misc.spawn-not-set");
            return;
        }
        if(gameManager.getPlayerTag(player).isTagged()) {
            messageHandler.send(player, "in-combat");
            return;
        }

        messageHandler.send(player, "teleport.teleporting");
        AtomicInteger seconds = new AtomicInteger(configFile.getInt("teleport-cooldown"));
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                int second = seconds.getAndDecrement();
                //NMS.spawnParticleAll(EnumParticle.SMOKE_LARGE , (float)player.getLocation().getX(), (float)player.getLocation().add(0, 2.5,0).getY(), (float)player.getLocation().getZ(), 255, 43, 47, (float) 0, 0, 0);
                    /*if (GameManager.getInstance().containsTagPlayer(player) || GameManager.getInstance().containsTagAttacker(player)) {
                        Utils.sendMessage(player, "teleportingCanceled");
                        GameManager.getInstance().teleportTask.remove(player.getName());
                        cancel();
                        return;
                    }*/
                if(second + 1 == 10) {
                    utils.sendTitle("titles.spawn", player, second + 1);
                    utils.playSound(configFile.getConfigurationSection("sounds.spawn"), player);
                }
                if(second < 6 && second > 0) {
                    utils.sendTitle("titles.spawn", player, second);
                    utils.playSound(configFile.getConfigurationSection("sounds.spawn"), player);
                }
                if (second < 1) {
                    player.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
                    gameManager.removeTask(player.getUniqueId());
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 20L, 20L);
        gameManager.addTask(player.getUniqueId(), task);
    }

}
