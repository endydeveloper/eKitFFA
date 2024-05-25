package me.endydev.ffa.api.handler.level.impl;

import me.endydev.ffa.api.handler.level.LevelHandler;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class LevelHandlerImpl implements LevelHandler {

    /**
     * Amount of experience required to obtain the first network level
     */
    private final static int BASE_EXPERIENCE = 850;

    /**
     * Growth rate of the required experience to rank up
     */
    private final static double EXPONENT = 1.3;

    /**
     * How steep the curve should be starting network level 95
     */
    private final static double DIFFICULTY_PERCENT = 0.8;

    private final DecimalFormat levelFormatter;
    private final DecimalFormat experienceFormatter;

    public LevelHandlerImpl(DecimalFormat levelFormatter, DecimalFormat experienceFormatter) {
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormatSymbols.setDecimalSeparator(',');

        levelFormatter.setDecimalFormatSymbols(decimalFormatSymbols);
        experienceFormatter.setDecimalFormatSymbols(decimalFormatSymbols);

        this.levelFormatter = levelFormatter;
        this.experienceFormatter = experienceFormatter;
    }

    @Override
    public String formatLevelString(int level) {
        String formattedLevel = levelFormatter.format(level);
        String levelTag;

        if (level >= 300) {
            levelTag = ChatColor.DARK_RED + formattedLevel + ChatColor.RESET;
        } else if (level >= 200) {
            levelTag = ChatColor.LIGHT_PURPLE + formattedLevel + ChatColor.RESET;
        } else if (level >= 100) {
            levelTag = ChatColor.AQUA + formattedLevel + ChatColor.RESET;
        } else if (level >= 80) {
            levelTag = ChatColor.RED + formattedLevel + ChatColor.RESET;
        } else if (level >= 50) {
            levelTag = ChatColor.GOLD + formattedLevel + ChatColor.RESET;
        } else if (level >= 30) {
            levelTag = ChatColor.DARK_PURPLE + formattedLevel + ChatColor.RESET;
        } else if (level >= 15) {
            levelTag = ChatColor.BLUE + formattedLevel + ChatColor.RESET;
        } else {
            levelTag = ChatColor.GRAY + formattedLevel + ChatColor.RESET;
        }

        return ChatColor.translateAlternateColorCodes('&', levelTag);
    }

    @Override
    public String formatExperienceString(double xp) {
        return experienceFormatter.format(xp);
    }

    @Override
    public int getLevelFromExperience(double experience) {
        int level = 1;
        double levelExperience = 0;

        while (levelExperience <= experience) {
            double logSetting = (level - 94) > 0 ? Math.log(DIFFICULTY_PERCENT * (level - 94)) : 0;
            levelExperience += Math.round(BASE_EXPERIENCE * (Math.pow(level + 1, EXPONENT) - Math.pow(level, EXPONENT)) * (1 + logSetting));
            level++;
        }

        return level - 1;
    }

    @Override
    public double getExperienceForLevel(int level) {
        double levelExperience = 0;
        double nextLevelExperience = 0;

        for (int i = 1; i <= level; i++) {
            levelExperience = nextLevelExperience;
            double logSetting = (i - 94) > 0 ? Math.log(DIFFICULTY_PERCENT * (i - 94)) : 0;
            nextLevelExperience += Math.round(BASE_EXPERIENCE * (Math.pow(i + 1, EXPONENT) - Math.pow(i, EXPONENT)) * (1 + logSetting));
        }

        return levelExperience;
    }
}