package me.endydev.ffa.api.perks;

public enum PerkType {
    ARROW,
    BOW,
    FISHING_ROD,
    GOLDEN_HEAD,
    SPEED_KILL,
    NONE;

    public static PerkType from(String value) {
        try {
            return PerkType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return NONE;
        }
    }
}
