package me.endydev.ffa.api.events.xp;

public interface ExperienceEvent {

    double getPreviousXP();

    double getCurrentXP();

    int getPreviousLevel();

    int getCurrentLevel();

    default boolean hasLevelledUp() {
        return getCurrentLevel() > getPreviousLevel();
    }

    default boolean hasLevelledDown() {
        return getCurrentLevel() < getPreviousLevel();
    }
}
