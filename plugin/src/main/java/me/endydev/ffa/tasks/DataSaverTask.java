package me.endydev.ffa.tasks;

import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class DataSaverTask implements Runnable {
    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private GameManager gameManager;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        int dataCount = 0;
        for(FFAPlayer pd : playerDataManager.getPlayers().values()) {
            gameManager.savePlayer(pd);
            dataCount++;
        }

        long time = System.currentTimeMillis() - startTime;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("ffa.log")) continue;
            p.sendMessage(Text.translate("&2[FFA] &a" + dataCount + " datos guardados en " + (time) + "ms."));
        }
    }
}
