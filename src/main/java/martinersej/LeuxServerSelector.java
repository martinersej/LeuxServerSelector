package martinersej;

import martinersej.command.ServerSelectorCommand;
import martinersej.event.GuiInteractionListener;
import martinersej.event.HubConnectListener;
import martinersej.menu.placeholder.HookToPlaceholderAPI;
import martinersej.menu.SelectorMenuGui;
import martinersej.model.Server;
import martinersej.serverresponsehelper.PingResponse;
import martinersej.serverresponsehelper.ServerPinger;
import martinersej.utils.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class LeuxServerSelector extends JavaPlugin {

    private static LeuxServerSelector instance;
    private static boolean placeholderAPI;
    private static PluginManager pluginManager;
    private static Map<String, Server> serverList;

    public static ConfigWrapper menuYMLWrapper, configYMLWrapper;
    public static FileConfiguration menuYML, configYML;

    @Override
    public void onEnable() {
        instance = this;
        pluginManager = getServer().getPluginManager();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HookToPlaceholderAPI(this).register();
            placeholderAPI = true;
        } else {
            placeholderAPI = false;
        }
        registerCommands();
        registerEvents();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        if(!(new File(getDataFolder(), "menu.yml").exists())) {
            saveResource("menu.yml", false);
        }
        if(!(new File(getDataFolder(), "config.yml").exists())) {
            saveResource("config.yml", false);
        }
        menuYMLWrapper = new ConfigWrapper(this, null, "menu.yml");
        menuYML = menuYMLWrapper.getConfig();
        configYMLWrapper = new ConfigWrapper(this, null, "config.yml");
        configYML = configYMLWrapper.getConfig();
        registerAllServers();
        SelectorMenuGui.SelectorMenuGUI();
        SelectorMenuGui.SelectorHand();
        registerAllServersInfo(1200, true);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    public static PluginManager getPluginManager() { return pluginManager; }

    private void registerCommands(){
        new ServerSelectorCommand(getInstance());
    }

    private void registerEvents(){
        new GuiInteractionListener(getInstance());
        new HubConnectListener(getInstance());
    }

    public static LeuxServerSelector getInstance(){
        return instance;
    }

    public static boolean getPlaceholderAPI() { return placeholderAPI; }

    public static void registerAllServers() {
        serverList = new HashMap<>();
        for (String server : configYML.getConfigurationSection("Serverlist").getKeys(false)) {
            Server s = new Server(server);
            s.setIP(configYML.getString("Serverlist."+server+".Ip"));
            s.setPort(configYML.getInt("Serverlist."+server+".Port"));
            serverList.put(server.toLowerCase(), s);
        }
    }

    public static void registerAllServersInfo(int period, boolean stopAfterRegister) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Server server : serverList.values()) {
                    PingResponse res = null;
                    try {
                        res = ServerPinger.fetchData(server.getIP(), server.getPort(), 900);
                    } catch (IOException ignored) {
                    }
                    server.setOnline(res != null && res.isOnline());
                    if (server.getOnline()) {
                        server.setPlayerCount(res.getOnlinePlayers());
                        server.setMaxPlayerCount(res.getMaxPlayers());
                        server.setMotd(res.getMotd());
                    }
                }
                if (stopAfterRegister) {
                    cancel();
                    registerAllServersInfo(configYML.getInt("UpdateServersTimer"), false);
                }
            }
        }.runTaskTimerAsynchronously(LeuxServerSelector.getInstance(), 0, period);
    }

    public static Map<String, Server> getServers() {
        return serverList;
    }
}

