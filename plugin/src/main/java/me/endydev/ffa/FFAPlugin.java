package me.endydev.ffa;

import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.inject.BinderModule;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;

public class FFAPlugin extends JavaPlugin {

    @Inject
    private Service service;


    @Override
    public void onEnable() {
        Injector injector = Injector.create(new BinderModule(this));
        injector.injectMembers(this);
        service.start();
    }

    @Override
    public void onDisable() {
        if (service == null) {
            getLogger().warning("Service is null!");
            return;
        }

        service.stop();
    }
}
