package martinersej.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigWrapper {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    private final String folderName, fileName;

    public ConfigWrapper(JavaPlugin instance, String folderName, String fileName) {
        this.plugin = instance;
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public FileConfiguration getConfig(){
        if(this.config == null) reloadConfig();
        return this.config;
    }

    public void reloadConfig() {
        if(this.configFile == null) {
            if (this.folderName != null && !this.folderName.isEmpty()) {
                this.configFile = new File(this.plugin.getDataFolder() + File.separator + this.folderName, this.fileName);
            } else {
                this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
            }
        }
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.configFile);
    }

}