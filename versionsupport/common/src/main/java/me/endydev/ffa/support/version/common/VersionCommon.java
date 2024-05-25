package me.endydev.ffa.support.version.common;

import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class VersionCommon {
    public static FFAAPI api;

    public VersionCommon(VersionSupport versionSupport) {
        api = Bukkit.getServicesManager().getRegistration(FFAAPI.class).getProvider();

    }

    private void registerListeners(Plugin plugin, Listener... listener) {
        for (Listener l : listener) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}
