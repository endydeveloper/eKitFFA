package me.endydev.ffa.cache;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
public class TagPlayer {

    @Setter
    private UUID uniqueId;
    private boolean tagged;
    @Setter
    private List<Player> attackers;
    private int seconds;
    private BukkitTask task;

    public TagPlayer(Player player) {
        this.uniqueId = player.getUniqueId();
        this.task = null;
        this.tagged = false;
        this.seconds = 15;
        attackers = new ArrayList<>();
    }

    public void resetSeconds() {
        this.seconds = 15;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public void addAttacker(Player attacker) {
        attackers.add(attacker);
    }

    public boolean hasAttackers() {
        return attackers.size() > 0;
    }

    public Player getLastAttacker() {
        if(attackers.size() > 0) {
            return attackers.get(attackers.size() - 1);
        }
        return null;
    }

    public Player getAssister() {
        if(attackers.size() > 1) {
            Player attacker = attackers.get(attackers.size() - 1);
            Player assiter = null;
            Map<Player, Integer> playerMap = new HashMap<>();
            for (Player p : attackers) {
                if(attacker.getName().equals(p.getName())) continue;
                if(playerMap.containsKey(p)) {
                    playerMap.put(p, playerMap.get(p) + 1);
                } else {
                    playerMap.put(p, 1);
                }
            }
            for (Player p : playerMap.keySet()) {
                if(assiter == null) assiter = p;
                if(playerMap.get(assiter) < playerMap.get(p)) {
                    assiter = p;
                }
            }
            return assiter;
        }
        return null;
    }
}
