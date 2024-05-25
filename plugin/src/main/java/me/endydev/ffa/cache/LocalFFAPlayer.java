package me.endydev.ffa.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.perks.PerkType;

import java.util.UUID;

@Getter
@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of")
public class LocalFFAPlayer implements FFAPlayer {
    private final UUID uniqueId;
    private final String name;
    private int kills;
    private int deaths;
    private int assists;
    private int coins;
    private int level;
    private int prestige;
    private double XP;
    private int killStreak;
    private int maxKs;
    private String selectedKit;
    private PerkType perk1;
    private PerkType perk2;
    private PerkType perk3;

    @Override
    public void setMaxKillStreak(int killStreak) {
        this.maxKs = killStreak;
    }

    @Override
    public void addMaxKillStreak() {
        this.maxKs++;
    }

    @Override
    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;
    }

    @Override
    public void addKillStreak() {
        this.killStreak++;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public void addKills() {
        this.kills += 1;
        setKills(this.kills);
    }

    @Override
    public void addDeaths() {
        this.deaths += 1;
        setDeaths(this.deaths);
    }

    @Override
    public void addAssist() {
        this.assists += 1;
    }

    @Override
    public void setAssists(int assists) {
        this.assists = assists;
    }

    @Override
    public void setPerk1(PerkType perk1) {
        this.perk1 = perk1;
    }

    @Override
    public void setPerk2(PerkType perk2) {
        this.perk2 = perk2;
    }

    @Override
    public void setPerk3(PerkType perk3) {
        this.perk3 = perk3;
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public void addCoins(int coins) {
        this.coins += coins;
        this.setCoins(this.coins);
    }

    @Override
    public void removeCoins(int coins) {
        this.coins -= coins;
        this.setCoins(this.coins);
    }

    @Override
    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    @Override
    public int getPrestige() {
        return this.prestige;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void addXP(double xp) {
        this.XP += xp;
        if (this.XP <= 0.0) {
            this.XP = 0.0;
        }

    }

    @Override
    public void setXP(double xp) {
        this.XP = xp;
        if (this.XP <= 0.0) {
            this.XP = 0.0;
        }

    }

    @Override
    public void removeXP(double xp) {
        this.XP -= xp;
        if (this.XP <= 0.0) {
            this.XP = 0.0;
        }

    }

    public PerkType getPerk1() {
        if (perk1 == null) {
            return PerkType.NONE;
        }

        return perk1;
    }

    public PerkType getPerk2() {
        if (perk2 == null) {
            return PerkType.NONE;
        }

        return perk2;
    }

    public PerkType getPerk3() {
        if (perk3 == null) {
            return PerkType.NONE;
        }

        return perk3;
    }
}
