package me.endydev.ffa.listeners;

import org.bukkit.event.Listener;

public class NPCListener implements Listener {

    /*@EventHandler
    public void NPCJoinPlayer(PlayerJoinEvent event) {
        NPCManager npcManager = Main.get().getNpcm();
        Player player = event.getPlayer();
        if(npcManager.getShopNPC() != null) {
            npcManager.getShopNPC().create();
            npcManager.getShopNPC().show(player);
        }

        if (npcManager.getPerkNPC() != null) {
            npcManager.getPerkNPC().create();
            npcManager.getPerkNPC().show(player);
        }
    }

    @EventHandler
    public void NPCQuitPlayer(PlayerQuitEvent event) {
        NPCManager npcManager = Main.get().getNpcm();
        Player player = event.getPlayer();
        if(npcManager.getShopNPC() != null && npcManager.getShopNPC().isShown(player)) {
            npcManager.getShopNPC().hide(player);
        }

        if(npcManager.getPerkNPC() != null && npcManager.getPerkNPC().isShown(player)) {
            npcManager.getPerkNPC().hide(player);
        }
    }

    @EventHandler
    public void NPCInteract(NPCInteractEvent event) {
        NPCManager npcManager = Main.get().getNpcm();
        NPC npc = event.getNPC();
        Player player = event.getWhoClicked();
        if(npcManager.getShopNPC() != null && npc.getId().equals(npcManager.getShopNPC().getId())) {
            MenuManager.get().getMenu("shopMenu", player).display();
        }

        if(npcManager.getPerkNPC() != null && npc.getId().equals(npcManager.getPerkNPC().getId())) {
            MenuManager.get().getMenu("perkMenu", player).display();
        }
    }*/

}
