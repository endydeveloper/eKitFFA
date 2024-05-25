package me.endydev.ffa.api.data;

import me.endydev.ffa.api.perks.PerkType;

import java.util.UUID;

public interface FFAPlayer {

    UUID getUniqueId();
    String getName();

    void setMaxKillStreak(int killStreak);
    void addMaxKillStreak();
    int getKillStreak();
    double getXP();
    int getMaxKs();
    int getLevel();
    String getSelectedKit();
    int getDeaths();
    int getAssists();

    void setKillStreak(int killStreak);
    void addKillStreak();
    void setKills(int kills);
    int getCoins();
    int getKills();
    void setDeaths(int deaths);
    void addKills();
    void addDeaths();

    void addAssist();
    void setAssists(int assists);
    void setPerk1(PerkType perk1);
    void setPerk2(PerkType perk2);
    void setPerk3(PerkType perk3);
    void setCoins(int coins);
    void addCoins(int coins);
    void removeCoins(int coins);
    void setPrestige(int prestige);
    int getPrestige();
    void setLevel(int level);
    void setXP(double xp);
    void removeXP(double xp);
    void addXP(double xp);
    PerkType getPerk1();
    PerkType getPerk2();
    PerkType getPerk3();
}
