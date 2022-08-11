package martinersej.Event;

import martinersej.LeuxServerSelector;
import martinersej.Menu.SelectorMenuGUI;
import martinersej.Model.Server;
import martinersej.Utils.itemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class guiInteraction implements Listener {

    public guiInteraction(LeuxServerSelector plugin) {
        LeuxServerSelector.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void compassRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType().equals(SelectorMenuGUI.getSelectorHandItem().getType()) && itemHelper.displayName(player.getItemInHand()).equals(itemHelper.displayName(SelectorMenuGUI.getSelectorHandItem()))) {
                player.openInventory(SelectorMenuGUI.getSelectorGUI());
            }
        }
    }

    @EventHandler
    public void serverSelectorGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        if (inventory != null) {
            Map.Entry<String, Inventory> foundEntry = SelectorMenuGUI.getGUIS().entrySet().stream().filter(v ->
                    v.getValue().getName().equals(inventory.getName())).findFirst().orElse(null);
            if (foundEntry == null) {
                return;
            }
            Inventory foundInventory = foundEntry.getValue();
            String foundPath = foundEntry.getKey();
            if (!foundInventory.getName().equals(inventory.getName())) {
                return;
            }
            event.setCancelled(true);
            Set<String> slots = LeuxServerSelector.menuYML.getConfigurationSection(foundPath+".Slot").getKeys(false);
            int slot = event.getSlot();
            if (slots.contains(String.valueOf(slot+1))) {
                if (LeuxServerSelector.menuYML.getBoolean(foundPath+".Slot."+(slot+1)+".Connect.Enabled")) {
                    String servername = LeuxServerSelector.menuYML.getString(foundPath+".Slot."+(slot+1)+".Connect.ToServer");
                    Server server = LeuxServerSelector.getServers().values().stream().filter(v -> v.name.equalsIgnoreCase(servername)).findFirst().orElse(null);
                    if (server != null && server.getOnline()) {
                        connectToServer(player, server.name);
                    }
                } else if (LeuxServerSelector.menuYML.getString(foundPath+".Slot."+(slot+1)+".GoToGUI") != null) {
                    String guiName = LeuxServerSelector.menuYML.getString(foundPath+".Slot."+(slot+1)+".GoToGUI");
                    player.openInventory(SelectorMenuGUI.getGUIS().get("GoToGUI."+guiName));
                }
            }
        }
    }

    private void connectToServer(Player player, String servername) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(servername);
        } catch (IOException ignored) {
        }
        player.sendPluginMessage(LeuxServerSelector.getInstance(), "BungeeCord", b.toByteArray());
    }

}
