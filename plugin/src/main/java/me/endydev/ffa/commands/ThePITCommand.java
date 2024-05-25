package me.endydev.ffa.commands;

import com.zelicraft.commons.shared.data.enums.Language;
import dev.triumphteam.cmd.core.annotation.Join;
import me.endydev.ffa.FFAPlugin;
import com.zelicraft.core.spigot.api.CoreAPI;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.menus.PerksMenu;
import me.endydev.ffa.menus.ShopMenu;
import me.endydev.ffa.menus.kits.KitCreatorMenu;
import me.endydev.ffa.utils.Cuboid;
import me.endydev.ffa.utils.Text;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Command(value = "thepit", alias = "pit")
public class ThePITCommand extends BaseCommand {

    @Inject
    private ConfigFile configFile;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private FFAPlugin plugin;

    @Inject
    private CoreAPI coreAPI;

    @Inject
    private PerksMenu perksMenu;
    
    @Inject
    private FFAAPI api;

    @Inject
    private ShopMenu shopMenu;

    @Inject
    private RegionManager regionManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private Utils utils;

    @Inject
    private KitManager kitManager;

    @Inject
    private KitCreatorMenu kitCreatorMenu;

    @SubCommand("createkit")
    @Permission("thepit.admin")
    public void createKit(Player player, @dev.triumphteam.cmd.core.annotation.Optional String name) {
        if(name == null) {

            return;
        }

        kitCreatorMenu.open(player, name);
    }

    @SubCommand("reload")
    @Permission("thepit.admin")
    public void reload(CommandSender sender) {
        try {
            configFile.reload();

            for (Language language : Language.values()) {
                reloadMessageHandlers(
                        language.getCode(),
                        messageHandler
                );
            }
            kitManager.loadKits();

            messageHandler.send(sender, "misc.reloaded");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void reloadMessageHandlers(String language, MessageHandler... messageHandler) {
        for (MessageHandler handler : messageHandler) {
            handler.getSource().load(language);
        }
    }

    /*
    @HelpCommand
    public static void help(CommandSender sender, CommandHelp help) {
        if (sender.hasPermission("thepit.admin")) {
            List<String> helpList = new ArrayList<>(FFAPlugin.get().getMessages().getStringList("helpAdmin"));
            for (String s : helpList) {
                sender.sendMessage(Text.translate(s));
            }

            return;
        }

        sender.sendMessage(Text.translate("&aThePit v" + FFAPlugin.get().getDescription().getVersion() + " by endydev#4103"));
    }
     */

    @SubCommand("addenderchest")
    @Permission("thepit.admin")
    public void addEnderChest(Player player) {
        Block block = player.getTargetBlock((Set<Material>) null, 10);
        if (!block.getType().equals(Material.ENDER_CHEST)) {
            messageHandler.send(player, "ender-chest.edit.no-enderchest");
            return;
        }

        if (configFile.contains("enderChests") && configFile.getStringList("enderChests").size() > 0) {
            for (String enderChest : configFile.getStringList("enderChests")) {
                Location loc = Utils.getLocationBlock(enderChest);
                if (block.getLocation().equals(loc)) {
                    messageHandler.send(player, "ender-chest.edit.already-added");
                    return;
                }
            }
        }

        utils.addLocationBlock(block.getLocation(), "enderChests");
        messageHandler.send(player, "ender-chest.edit.added");
    }

    @SubCommand("build")
    @Permission("thepit.admin")
    public void buildMode(Player player) throws Exception {
        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new Exception("User not found!"));
        boolean buildMode = gameManager.toggleBuildMode(player.getUniqueId());
        messageHandler.send(player, "build-mode." + (buildMode ? "enabled" : "disabled"));
    }

    @SubCommand(value = "setspawn", alias = "setlobby")
    @Permission("thepit.admin")
    public void setSpawn(Player player) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        String worldName = player.getWorld().getName();
        configFile.set("spawn.x", x);
        configFile.set("spawn.y", y);
        configFile.set("spawn.z", z);
        configFile.set("spawn.yaw", yaw);
        configFile.set("spawn.pitch", pitch);
        configFile.set("spawn.world", worldName);
        configFile.save();
        try {
            configFile.reload();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        player.sendMessage("");
        messageHandler.send(player, "misc.spawn-set");
    }

    @SubCommand("setlevel")
    @Permission("thepit.admin")
    public void setLevel(Player player, @Join String params) {
        String[] args = params.split(" ");

        if (args.length < 2) {
            player.sendMessage(Text.translate("&cUsa: &7/thepit setlevel <jugador> <level>"));
            return;
        }

        Player p = Bukkit.getPlayer(args[0]);
        if (p == null || !p.isOnline()) {
            messageHandler.send(player, "playerNotOnline");
            return;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            messageHandler.send(player, "command.invalid-number");
            return;
        }

        Optional<FFAPlayer> optional = playerDataManager.getPlayer(p.getUniqueId());

        if (!optional.isPresent()) {
            messageHandler.send(player, "player.not-online");
            return;
        }

        FFAPlayer ffaPlayer = optional.get();

        ffaPlayer.setLevel(Integer.parseInt(args[1]));
        p.sendMessage(messageHandler.replacing("ffa.level.set.target", "%player%", p.getName(), "%level%", String.valueOf(args[1])));
        player.sendMessage(messageHandler.replacing("ffa.level.set.player", "%player%", player.getName(), "%level%", String.valueOf(args[1])));
    }

    @SubCommand("shop")
    public void commandShopMenu(Player player) {
        shopMenu.open(player);
    }

    @SubCommand("perks")
    public void commandPerksMenu(Player player) {

        playerDataManager.getPlayer(player.getUniqueId())
                .ifPresent(playerData -> {
                    if (api.getLevel(player) < 15) {
                        messageHandler.send(player, "leve.no-level");
                        return;
                    }

                    perksMenu.open(player);
                });
    }

    /*@Subcommand("setshop")
    @CommandPermission("thepit.admin")
    @Syntax("[skin]")
    public static void setShop(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendMessage(sender, "noConsole");
            return;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("thepit.admin")) {
            Utils.sendMessage(player, "noPermission");
            return;
        }
        NPC npc = Main.get().getNpcm().getNpcLib().createNPC(Main.get().getMessages().getStringList("shopNPC"));
        Location location = player.getLocation().clone();
        location.setX((double)location.getBlockX() + 0.5);
        location.setZ((double)location.getBlockZ() + 0.5);
        npc.setLocation(location);
        if(args.length == 1) {
            String[] values = PlayerSkin.getFromName(args[0]);
            if(values != null) {
                Main.get().getConfig().set("shopNPC.skin", args[0]);
                Skin skin = new Skin(values[0], values[1]);
                npc.setSkin(skin);
            }
        }
        NPC shopNPC = Main.get().getNpcm().getShopNPC();
        if(shopNPC != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                shopNPC.hide(p);
            }
            shopNPC.destroy();
        }
        npc.create();
        for (Player p : Bukkit.getOnlinePlayers()) {
            npc.show(p);
        }
        Main.get().getConfig().set("shopNPC.id", npc.getId());
        Utils.setLocationToString(player.getLocation(), "shopNPC.location");
        Main.get().getConfig().save();
        Main.get().getNpcm().setShopNPC(npc);
        Utils.sendMessage(player, "shopNPCSet");
    }

    @Subcommand("setperk")
    @CommandPermission("thepit.admin")
    @Syntax("[skin]")
    public static void setPerk(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendMessage(sender, "noConsole");
            return;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("thepit.admin")) {
            Utils.sendMessage(player, "noPermission");
            return;
        }
        NPC npc = Main.get().getNpcm().getNpcLib().createNPC(Main.get().getMessages().getStringList("perkNPC"));
        Location location = player.getLocation().clone();
        location.setX((double)location.getBlockX() + 0.5);
        location.setZ((double)location.getBlockZ() + 0.5);
        npc.setLocation(location);
        if(args.length == 1) {
            String[] values = PlayerSkin.getFromName(args[0]);
            if(values != null) {
                Main.get().getConfig().set("perkNPC.skin", args[0]);
                Skin skin = new Skin(values[0], values[1]);
                npc.setSkin(skin);
            }
        }
        NPC perkNPC = Main.get().getNpcm().getPerkNPC();
        if(perkNPC != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                perkNPC.hide(p);
            }
            perkNPC.destroy();
        }
        npc.create();
        for (Player p : Bukkit.getOnlinePlayers()) {
            npc.show(p);
        }
        Main.get().getConfig().set("perkNPC.id", npc.getId());
        Utils.setLocationToString(player.getLocation(), "perkNPC.location");
        Main.get().getConfig().save();
        Main.get().getNpcm().setPerkNPC(npc);
        Utils.sendMessage(player, "perkNPCSet");
    }*/

    @SubCommand("coins")
    @Permission("thepit.admin")
    public void coins(CommandSender sender, @Join String params) {
        String[] args = params.split(" ");
        boolean isConsole = !(sender instanceof Player);
        String name = isConsole ? "Consola" : ((Player) sender).getName();

        if (args.length > 1) {
            switch (args[0].toLowerCase()) {
                case "set": {
                    if (args.length < 3) {
                        sender.sendMessage(Text.translate("&cUsa: /thepit coins set <jugador> <monedas>"));
                        return;
                    }

                    Player p = Bukkit.getPlayer(args[1]);
                    if (p == null || !p.isOnline()) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }
                    try {
                        Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        messageHandler.send(sender, "command.invalid-number");
                        return;
                    }
                    FFAPlayer ffaPlayer = playerDataManager.getPlayer(p.getUniqueId()).orElse(null);
                    if (ffaPlayer == null) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }

                    ffaPlayer.setCoins(Integer.parseInt(args[2]));
                    messageHandler.sendReplacing(p, "coins.set.target", "%player%", name, "%coins%", ffaPlayer.getCoins());
                    messageHandler.sendReplacing(sender, "coins.set.player", "%player%", p.getName(), "%coins%", ffaPlayer.getCoins());
                    return;
                }
                case "add": {

                    if (args.length < 3) {
                        sender.sendMessage(Text.translate("&cUsa: /thepit coins add <jugador> <monedas>"));
                        return;
                    }

                    Player p = Bukkit.getPlayer(args[1]);
                    if (p == null || !p.isOnline()) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }
                    try {
                        Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        messageHandler.send(sender, "command.invalid-number");
                        return;
                    }
                    FFAPlayer ffaPlayer = playerDataManager.getPlayer(p.getUniqueId()).orElse(null);
                    if (ffaPlayer == null) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }

                    ffaPlayer.addCoins(Integer.parseInt(args[2]));
                    messageHandler.sendReplacing(p, "coins.add.target", "%player%", name, "%coins%", args[2]);
                    messageHandler.sendReplacing(sender, "coins.add.player", "%player%", p.getName(), "%coins%", args[2]);
                    return;
                }
                case "remove": {

                    if (args.length < 3) {
                        sender.sendMessage(Text.translate("&cUsa: /thepit coins remove <jugador> <monedas>"));
                        return;
                    }

                    Player p = Bukkit.getPlayer(args[1]);
                    if (p == null || !p.isOnline()) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }
                    try {
                        Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        messageHandler.send(sender, "command.invalid-number");
                        return;
                    }
                    
                    FFAPlayer FFAffaPlayerlayer = playerDataManager.getPlayer(p.getUniqueId()).orElse(null);
                    if (FFAffaPlayerlayer == null) {
                        messageHandler.send(sender, "player.not-online");
                        return;
                    }

                    FFAffaPlayerlayer.removeCoins(Integer.parseInt(args[2]));
                    messageHandler.sendReplacing(p, "coins.remove.target", "%player%", name, "%coins%", args[2]);
                    messageHandler.sendReplacing(sender, "coins.remove.player", "%player%", p.getName(), "%coins%", args[2]);
                }
                default: {
                    return;
                }
            }
        } else {
            return;
        }
    }

    @SubCommand("setfirst")
    @Permission("thepit.admin")
    public void setFirst(Player player) {
        regionManager.setFirst(player.getLocation());
        messageHandler.send(player, "2");
        messageHandler.send(player, "region.first");
        if (regionManager.getFirst() != null && regionManager.getSecond() != null) {
            utils.setRegionLocationConfig("region-spawn.first.", regionManager.getFirst());
            utils.setRegionLocationConfig("region-spawn.second.", regionManager.getSecond());
            regionManager.setRegion(new Cuboid(utils.getLocationSection(configFile.getConfigurationSection("region-spawn.first")), utils.getLocationSection(configFile.getConfigurationSection("region-spawn.second"))));
            messageHandler.send(player, "region.save");
            regionManager.setFirst(null);
            regionManager.setSecond(null);
        }
    }

    @SubCommand("setsecond")
    @Permission("thepit.admin")
    public void setSecond(Player player) {
        regionManager.setSecond(player.getLocation());
        messageHandler.send(player, "region.second");
        if (regionManager.getFirst() != null && regionManager.getSecond() != null) {
            utils.setRegionLocationConfig("region-spawn.first.", regionManager.getFirst());
            utils.setRegionLocationConfig("region-spawn.second.", regionManager.getSecond());
            regionManager.setRegion(new Cuboid(utils.getLocationSection(configFile.getConfigurationSection("region-spawn.first")), utils.getLocationSection(configFile.getConfigurationSection("region-spawn.second"))));
            messageHandler.send(player, "region.save");
            regionManager.setFirst(null);
            regionManager.setSecond(null);
        }
    }
}
