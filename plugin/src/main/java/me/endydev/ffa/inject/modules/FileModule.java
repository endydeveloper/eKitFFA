package me.endydev.ffa.inject.modules;

import me.endydev.ffa.FFAPlugin;
import team.unnamed.inject.AbstractModule;

import java.io.File;

import static com.zelicraft.commons.shared.utils.folder.FolderUtils.copyFolder;

public class FileModule extends AbstractModule {
    private final FFAPlugin plugin;

    public FileModule(FFAPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        try {
            copyFolder("kits", plugin.getDataFolder(), false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy kits files.", e);
        }

        File folder = new File(plugin.getDataFolder(), "i18n");
        this.bind(File.class)
                .named("i18nFolder")
                .toInstance(folder);

        this.bind(File.class)
                .named("kitsFolder")
                .toInstance(new File(plugin.getDataFolder(), "kits"));
    }
}
