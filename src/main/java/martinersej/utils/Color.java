package martinersej.utils;

import net.md_5.bungee.api.ChatColor;

import static martinersej.utils.Server.getPrefix;

public class Color {

    public static String colored(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String plain(String s) {
        return s.replaceAll("§", "&");
    }

    public static String coloredAndPrefix(String s) {
        return ChatColor.translateAlternateColorCodes('&', getPrefix() + s);
    }

    public static String messageColored(String s) {
        //return (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', s))).create();
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String messageColoredAndPrefix(String s) {
        //return (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', getPrefix() + s))).create();
        return ChatColor.translateAlternateColorCodes('&', getPrefix() + s);
    }
}