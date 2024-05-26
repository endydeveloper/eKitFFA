package me.endydev.ffa.listeners.game.world;

import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import team.unnamed.inject.Inject;

import java.util.Set;

public class GameFlowLavaListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(!event.getBucket().equals(Material.WATER_BUCKET) && !event.getBucket().equals(Material.LAVA_BUCKET)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        Block targetBlock = player.getTargetBlock((Set<Material>) null, 10);
        if(Tag) {
            event.setCancelled(true);
            return;
        }

        if (targetBlock.getState() instanceof Slab) return;

        if(!gameManager.containsBlock(block.getLocation())) {
            gameManager.addBlock(block, 10000);
        }
    }

    @EventHandler
    public void fromBlock(BlockFormEvent event) {
        Block block = event.getBlock();
        System.out.println("Form " + block.getType());
        if(block.getType().equals(Material.LAVA)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void flowLava(BlockFromToEvent event) {
        if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.LAVA)) {
            event.setCancelled(true);
        }
    }
}
