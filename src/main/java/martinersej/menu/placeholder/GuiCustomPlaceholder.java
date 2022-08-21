package martinersej.menu.placeholder;

import martinersej.LeuxServerSelector;
import martinersej.model.Server;

import java.util.ArrayList;

public class GuiCustomPlaceholder {

    static ArrayList<String> currentPlaceholder = new ArrayList<>();;

    public GuiCustomPlaceholder() {
    }

    public static void addDefaultPlaceholder() {
        currentPlaceholder.add("motd");
        currentPlaceholder.add("online");
        currentPlaceholder.add("onlineplayers");
        currentPlaceholder.add("maxplayers");
    }

    public static String editPlacholder(String text, String whichPlaceholder, String whichServer, Server server) {
        if (server == null) {
            return text;
        }
        String replaceText = "%serverselector_"+whichPlaceholder.toLowerCase()+"::"+whichServer.toLowerCase()+"%";
        if (whichPlaceholder.equalsIgnoreCase("motd")) {
            text = text.replace(replaceText, server.getMotd());
        } else if (whichPlaceholder.equalsIgnoreCase("online")) {
            if (LeuxServerSelector.configYML.getString("lang.online." + String.valueOf(server.getOnline()).toLowerCase()) != null) {
                text = text.replace(replaceText, LeuxServerSelector.configYML.getString("lang.online." + String.valueOf(server.getOnline()).toLowerCase()));
            } else {
                text = text.replace(replaceText, String.valueOf(server.getOnline()));
            }
            text = text.replace(replaceText, String.valueOf(server.getOnline()));
        } else if (whichPlaceholder.equalsIgnoreCase("onlineplayers")) {
            if (LeuxServerSelector.configYML.getString("lang.onlineplayers." + server.getPlayerCount()) != null) {
                text = text.replace(replaceText, LeuxServerSelector.configYML.getString("lang.onlineplayers." + server.getPlayerCount()));
            } else {
                text = text.replace(replaceText, String.valueOf(server.getPlayerCount()));
            }
            text = text.replace(replaceText, String.valueOf(server.getPlayerCount()));
        } else if (whichPlaceholder.equalsIgnoreCase("maxplayers")) {
            text = text.replace(replaceText, String.valueOf(server.getMaxPlayerCount()));
        } else  {
            return text;
        }
        return text;
    }

    public static boolean contains(String placeholder) {
        return placeholder != null && currentPlaceholder.contains(placeholder);
    }
}