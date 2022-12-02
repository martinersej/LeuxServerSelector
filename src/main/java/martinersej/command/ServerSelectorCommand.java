package martinersej.command;

import martinersej.LeuxServerSelector;
import martinersej.menu.SelectorMenuGui;
import martinersej.utils.Color;
import martinersej.utils.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ServerSelectorCommand implements CommandExecutor {

    public ServerSelectorCommand(LeuxServerSelector plugin){
        plugin.getCommand("leuxserverselector").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("leuxss.admin")){
            sender.sendMessage(Color.messageColoredAndPrefix("&fYou don't have permission for this command.."));
            return true;
        }
        String _command = (label == null) ? String.valueOf(command) : label;
        if (args.length == 0) {
            sendDefaultCommand(sender, _command);
            return true;
        } else if (args[0].equalsIgnoreCase("help")) {
            sendDefaultCommand(sender, _command);
            return true;
        } else if (args[0].equalsIgnoreCase("serverlist")) {
            sendServerListCommand(sender);
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            boolean reloadSucces;
            try {
                LeuxServerSelector.menuYMLWrapper.reloadConfig();
                LeuxServerSelector.menuYML = LeuxServerSelector.menuYMLWrapper.getConfig();
                SelectorMenuGui.getLoreUpdaterTask().cancel();
                SelectorMenuGui.SelectorMenuGUI();
                SelectorMenuGui.SelectorHand();
                LeuxServerSelector.registerAllServers();
                reloadSucces = true;
            } catch(Exception e){
                e.printStackTrace();
                reloadSucces = false;
            }
            if(reloadSucces) {
                sender.sendMessage(Color.messageColoredAndPrefix("&aReload successfully completed"));
            } else {
                sender.sendMessage(Color.messageColoredAndPrefix("&cAn error occurred. Please check the console."));
            }
            return true;
        } else {
            return false;
        }
    }

    private void sendDefaultCommand(CommandSender sender, String command){
        String sb = "";
        sb = sb + "\n";
        sb = sb + Server.getPrefix() + "&7/" + command + " help &8- " + "&eSends this help message" + "\n";
        sb = sb + Server.getPrefix() + "&7/" + command + " serverlist &8- " + "&eSends a list with connected servers" + "\n";
        sb = sb + Server.getPrefix() + "&7/" + command + " reload &8- " + "&eReloading &6menu.yml &eand refresh the menu" + "\n ";
        sender.sendMessage(Color.messageColored(sb));
    }

    private void sendServerListCommand(CommandSender sender){
        String sb = "";
        sb = sb + "\n";
        sb = sb + Server.getPrefix() + "&eThe following servers is connected:\n";
        for (Map.Entry<String, martinersej.model.Server> entry : LeuxServerSelector.getServers().entrySet()) {
            sb = sb + " &8- &e" + entry.getKey() + " &8("+(entry.getValue().getOnline() ? "&a✔" : "&c✖") + "&8)" + "\n";
        }
        sb = sb + " \n";
        sender.sendMessage(Color.messageColored(sb));
    }
}
