package me.endydev.ffa.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PapiHook extends PlaceholderExpansion {
    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private ConfigFile configFile;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    private List<FFAPlayer> topKillsCache = new ArrayList<>();
    private List<FFAPlayer> topDeathsCache = new ArrayList<>();
    private List<FFAPlayer> topCoinsCache = new ArrayList<>();
    private List<FFAPlayer> topLevelCache = new ArrayList<>();
    private long lastUpdated = 0;

    @Override
    public @NotNull String getIdentifier() {
        return "thepit";
    }

    @Override
    public @NotNull String getAuthor() {
        return "endydev";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("User not found!"));

        if(ffaPlayer == null) {
            return params;
        }

        if(params.equalsIgnoreCase("level")) {
            return String.valueOf(ffaPlayer.getLevel());
        }
        if(params.equalsIgnoreCase("coins")) {
            return String.valueOf(ffaPlayer.getCoins());
        }
        if(params.equalsIgnoreCase("killstreak") || params.equalsIgnoreCase("ks")) {
            return String.valueOf(ffaPlayer.getKillStreak());
        }
        if(params.equalsIgnoreCase("kills")) {
            return String.valueOf(ffaPlayer.getKills());
        }
        if(params.equalsIgnoreCase("deaths")) {
            return String.valueOf(ffaPlayer.getDeaths());
        }
        if(params.equalsIgnoreCase("xp")) {
            return String.valueOf(ffaPlayer.getXP());
        }
            /*if(params.equalsIgnoreCase("needxp")) {
                int level = ffaPlayer.getLevel();
                int neededXP = (5 * (level * level) + 80 * level + 100) - ffaPlayer.getXP();
                return String.valueOf(Math.max(neededXP, 0));
            }*/

        if(params.equalsIgnoreCase("maxks")) {
            return String.valueOf(ffaPlayer.getMaxKs());
        }
        if(params.equalsIgnoreCase("combatlog")) {
            return Objects.equals(gameManager.getPlayerState(player), "combat") ? messageHandler.get(player, "state.combat") : messageHandler.get(player, "state.free");
        }

        if(params.equalsIgnoreCase("enderchest")) {
            if(ffaPlayer.getLevel() >= configFile.getInt("enderchest-level")) {
                return messageHandler.get(player, "ender-chest.has-level");
            } else {
                return messageHandler.get(player, "ender-chest.no-level");
            }
        }

        if (params.startsWith("top_")) {
            if(topKillsCache.isEmpty()) {
                topKillsCache.addAll(ffaPlayerRepository.getTopKillers(100));
                lastUpdated = System.currentTimeMillis();
            }

            if(topDeathsCache.isEmpty()) {
                topDeathsCache.addAll(ffaPlayerRepository.getTopDeaths(100));
                lastUpdated = System.currentTimeMillis();
            }

            if(topCoinsCache.isEmpty()) {
                topCoinsCache.addAll(ffaPlayerRepository.getTopCoins(100));
                lastUpdated = System.currentTimeMillis();
            }

            if(topLevelCache.isEmpty()) {
                topLevelCache.addAll(ffaPlayerRepository.getTopLevel(100));
                lastUpdated = System.currentTimeMillis();
            }

            if ((System.currentTimeMillis() - lastUpdated) >= 300000) {
                topKillsCache.clear();
                topDeathsCache.clear();
                topCoinsCache.clear();
                topLevelCache.clear();

                topKillsCache = new ArrayList<>(ffaPlayerRepository.getTopKillers(100));
                topDeathsCache = new ArrayList<>(ffaPlayerRepository.getTopDeaths(100));
                topCoinsCache = new ArrayList<>(ffaPlayerRepository.getTopCoins(100));
                topLevelCache = new ArrayList<>(ffaPlayerRepository.getTopLevel(100));

                lastUpdated = System.currentTimeMillis();
            }

            if (params.contains("deaths")) {
                int topIndex;
                try {
                    topIndex = Integer.parseInt(params.replaceAll("\\D", ""));
                } catch (NumberFormatException ex) {
                    return params;
                }

                if(topDeathsCache.isEmpty() || (topIndex-1) >= topDeathsCache.size()) return "N/A";

                FFAPlayer value = topDeathsCache.get(topIndex-1);
                String[] partes = params.split("_");
                String findValue = partes[3];

                if (findValue.equalsIgnoreCase("name")) {
                    return value.getName();
                } else if(findValue.equalsIgnoreCase("value")) {
                    return String.valueOf(value.getDeaths());
                }
            } else if (params.contains("kills")) {
                int topIndex;
                try {
                    topIndex = Integer.parseInt(params.replaceAll("\\D", ""));
                } catch (NumberFormatException ex) {
                    return params;
                }

                if(topKillsCache.isEmpty() || (topIndex-1) >= topKillsCache.size()) return "N/A";

                FFAPlayer value = topKillsCache.get(topIndex-1);
                String[] partes = params.split("_");
                String findValue = partes[3];

                if (findValue.equalsIgnoreCase("name")) {
                    return value.getName();
                } else if(findValue.equalsIgnoreCase("value")) {
                    return String.valueOf(value.getKills());
                }
            } else if (params.contains("level")) {
                int topIndex;
                try {
                    topIndex = Integer.parseInt(params.replaceAll("\\D", ""));
                } catch (NumberFormatException ex) {
                    return params;
                }

                if(topLevelCache.isEmpty() || (topIndex-1) >= topLevelCache.size()) return "N/A";

                FFAPlayer value = topLevelCache.get(topIndex-1);
                String[] partes = params.split("_");
                String findValue = partes[3];

                if (findValue.equalsIgnoreCase("name")) {
                    return value.getName();
                } else if(findValue.equalsIgnoreCase("value")) {
                    return String.valueOf(value.getLevel());
                }
            } else if (params.contains("coins")) {
                int topIndex;
                try {
                    topIndex = Integer.parseInt(params.replaceAll("\\D", ""));
                } catch (NumberFormatException ex) {
                    return params;
                }

                if(topCoinsCache.isEmpty() || (topIndex-1) >= topCoinsCache.size()) return "N/A";

                FFAPlayer value = topCoinsCache.get(topIndex-1);
                String[] partes = params.split("_");
                String findValue = partes[3];

                if (findValue.equalsIgnoreCase("name")) {
                    return value.getName();
                } else if(findValue.equalsIgnoreCase("value")) {
                    return String.valueOf(value.getCoins());
                }
            } else if(params.contains("lastupdate")) {
                return Utils.calculateTimeAgo(lastUpdated);
            }
        }
        
        return params;
    }
}
