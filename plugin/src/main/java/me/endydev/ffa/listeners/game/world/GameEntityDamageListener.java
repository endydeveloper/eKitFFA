package me.endydev.ffa.listeners.game.world;

import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.perks.Perk;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

import java.util.Map;

public class GameEntityDamageListener implements Listener {

    @Inject
    private RegionManager regionManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private FFAPlayerRepository playerRepository;

    @Inject
    private ConfigFile configFile;

    @Inject
    private Map<PerkType, Perk> perks;

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof FishHook && event.getEntity() instanceof Player) {
            FishHook fishing = (FishHook) event.getDamager();
            if (fishing.getShooter() instanceof Player) {
                Player attacker = (Player) fishing.getShooter();
                Player victim = (Player) event.getEntity();
                if (regionManager.getRegion() != null && (regionManager.getRegion().contains(event.getEntity().getLocation()) || regionManager.getRegion().contains(attacker.getLocation()))) {
                    event.setCancelled(true);
                    return;
                }
                if (attacker.equals(victim)) {
                    event.setCancelled(true);
                    return;
                }
                gameManager.addPlayerTag(victim, attacker);
            }
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player attacker = (Player) arrow.getShooter();
                Player victim = (Player) event.getEntity();
                if (regionManager.getRegion() != null && (regionManager.getRegion().contains(event.getEntity().getLocation()) || regionManager.getRegion().contains(attacker.getLocation()))) {
                    event.setCancelled(true);
                    return;
                }
                if (attacker.getUniqueId().equals(victim.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
                FFAPlayer ffaPlayer = playerDataManager.getPlayer(attacker.getUniqueId()).orElse(null);
                if (ffaPlayer != null && playerRepository.hasPerk(attacker.getUniqueId(), PerkType.ARROW)) {
                    int arrows = 0;
                    for (ItemStack i : attacker.getInventory().getContents()) {
                        if (i == null) continue;
                        if (i.getType().equals(Material.ARROW)) {
                            arrows += i.getAmount();
                        }
                    }

                    if (arrows <= configFile.getInt("max-arrows")) {
                        perks.get(PerkType.ARROW).runAction(attacker);
                    }
                }
                gameManager.addPlayerTag(victim, attacker);
            }
        }
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            if (regionManager.getRegion() != null && (regionManager.getRegion().contains(event.getEntity().getLocation()) || regionManager.getRegion().contains(attacker.getLocation()))) {
                event.setCancelled(true);
                return;
            }
            gameManager.addPlayerTag(victim, attacker);
        }
    }
}
