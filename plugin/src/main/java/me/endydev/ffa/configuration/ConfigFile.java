package me.endydev.ffa.configuration;

import com.zelicraft.commons.shared.utils.Replacer;
import me.endydev.ffa.utils.Text;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.zelicraft.commons.spigot.utils.TextUtils.colorize;

public final class ConfigFile extends YamlConfiguration {

    private final String fileName;
    private final Plugin plugin;
    private final File folder;

    public ConfigFile(Plugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public ConfigFile(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    public ConfigFile(Plugin plugin, String fileName, String fileExtension, File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);

        this.options().copyDefaults(true);
        this.create();
    }

    @Override
    public String getString(String path) {
        return Text.translate(
                super.getString(path, path)
        );
    }

    @Override
    public List<String> getStringList(String path) {
        return getStringList(path, true);
    }

    public List<String> getStringList(String path, boolean colorize) {
        // Get the list
        List<String> list = super.getStringList(path);

        // Check if the list is null
        if(list == null) {
            // Return an empty array list
            return new ArrayList<>();
        }

        // Return the colorized list if wanted
        return colorize ? colorize(list) : list;
    }

    public List<String> getStringList(String path, Replacer... placeholders) {
        return getStringList(path, true, placeholders);
    }

    public List<String> getStringList(String path, boolean colorize, Replacer... placeholders) {
        List<String> list = this.getStringList(path, colorize);

        // Loop through each provided placeholder
        for (Replacer placeholder : placeholders) {
            // Loop through each line of the list
            for (int i = 0; i < list.size(); i++) {
                // Get the string at the current index
                String item = list.get(i);

                // Check if the string contains the placeholder
                if (item.contains(placeholder.getValue())) {
                    // Replace the placeholder with the placeholder's replacement
                    list.set(i, item.replace(placeholder.getValue(), placeholder.getReplacement()));
                }
            }
        }

        // Return the final list
        return list;
    }

    public void create() {
        try {
            File file = new File(this.folder, this.fileName);
            if (file.exists()) {
                this.load(file);
                this.save(file);
            } else {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, true);
                } else {
                    this.save(file);
                }

                this.load(file);
            }
        } catch (IOException | InvalidConfigurationException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to create file '" + this.fileName + "'.", exception);
        }
    }

    public void save() {
        File file = new File(folder, this.fileName);

        try {
            this.save(file);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to save file '" + this.fileName + "'.", exception);
        }
    }

    public void reload() throws IOException, InvalidConfigurationException {
        //File folder = this.plugin.getDataFolder();
        File file = new File(folder, this.fileName);

        try {
            this.load(file);
        } catch (InvalidConfigurationException | IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to reload file '" + this.fileName + "'.", exception);
            throw exception;
        }
    }
}
