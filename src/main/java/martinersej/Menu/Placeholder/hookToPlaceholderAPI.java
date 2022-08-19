package martinersej.Menu.Placeholder;

import martinersej.LeuxServerSelector;
import martinersej.Model.Server;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class hookToPlaceholderAPI extends PlaceholderExpansion {

    private final LeuxServerSelector plugin;

    public hookToPlaceholderAPI(LeuxServerSelector plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "MartinErSej";
    }

    @Override
    public String getIdentifier() {
        return "serverselector";
    }

    @Override
    public String getVersion() {
        return "1.0.2";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String text) {
        String[] placeholderList = text.split("::", 2);
        Server server = LeuxServerSelector.getServers().values().stream().filter(s -> s.name.equalsIgnoreCase(placeholderList[1].toLowerCase())).findFirst().orElse(null);
        if (server == null) {
            return null;
        }
        text = placeholderList[0];

        if(text.equalsIgnoreCase("motd")){
            return server.getMotd();
        }

        else if (text.equalsIgnoreCase("online")) {
            if (LeuxServerSelector.configYML.getString("lang.online." + String.valueOf(server.getOnline()).toLowerCase()) != null) {
                return LeuxServerSelector.configYML.getString("lang.online." + String.valueOf(server.getOnline()).toLowerCase());
            } else {
                return String.valueOf(server.getOnline());
            }
        }

        else if (text.equalsIgnoreCase("onlineplayers")) {
            if (LeuxServerSelector.configYML.getString("lang.onlineplayers." + server.getPlayerCount()) != null) {
                return LeuxServerSelector.configYML.getString("lang.onlineplayers." + server.getPlayerCount());
            } else {
                return String.valueOf(server.getPlayerCount());
            }
        }

        else if (text.equalsIgnoreCase("maxplayers")) {
            return String.valueOf(server.getMaxPlayerCount());
        }

        else if (text.equalsIgnoreCase("ip")) {
            return server.getIP();
        }

        else if (text.equalsIgnoreCase("port")) {
            return String.valueOf(server.getPort());
        }

        else if (text.equalsIgnoreCase("fullip")) {
            return server.getIP() +":"+ server.getPort();
        }

        return null; // Placeholder is unknown by the Expansion
    }
}