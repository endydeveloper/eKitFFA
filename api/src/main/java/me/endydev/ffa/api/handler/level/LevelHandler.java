package me.endydev.ffa.api.handler.level;

public interface LevelHandler {

    /**
     * Formats the given level into a readable string.
     * @param level The level to format.
     * @return The formatted level.
     */
    String formatLevelString(int level);

    /**
     * Formats an experience amount into a string.
     * @param xp The experience amount.
     * @return The formatted string.
     */
    String formatExperienceString(double xp);

    /**
     * Converts a {@link Long} value of experience to a {@link Integer} value of level.
     * @param xp The experience to convert.
     * @return The level.
     */
    int getLevelFromExperience(double xp);

    /**
     * Gets the required experience to complete a level.
     * @param level The level to get the experience for.
     * @return The required experience to complete the level.
     */
    double getExperienceForLevel(int level);
}
