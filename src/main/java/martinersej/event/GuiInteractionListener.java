package martinersej.event;

import martinersej.LeuxServerSelector;
import martinersej.menu.SelectorMenuGui;
import martinersej.model.Server;
import martinersej.utils.ItemCreator;
import martinersej.utils.UniversalMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class GuiInteractionListener implements Listener {

    public GuiInteractionListener(LeuxServerSelector plugin) {
        LeuxServerSelector.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void compassRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType().equals(UniversalMaterial.AIR.getType())) {
                return;
            }
            if (!new ItemCreator(player.getItemInHand()).hasDisplayName() && !new ItemCreator(SelectorMenuGui.getSelectorHandItem()).hasDisplayName() && (SelectorMenuGui.SelectorHandSlot() == player.getInventory().getHeldItemSlot())) {
                event.setCancelled(true);
                player.openInventory(SelectorMenuGui.getSelectorGUI());
            }
            else if (player.getItemInHand().getType().equals(SelectorMenuGui.getSelectorHandItem().getType()) && ItemCreator.displayName(player.getItemInHand()).equals(ItemCreator.displayName(SelectorMenuGui.getSelectorHandItem()))) {
                event.setCancelled(true);
                player.openInventory(SelectorMenuGui.getSelectorGUI());
            }
        }
    }

    @EventHandler
    public void serverSelectorGui(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        String inventoryName = SelectorMenuGui.getGUISWithNames().get(inventory);
        if (inventory != null) {
            if (event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
                if (SelectorMenuGui.getSelectorHandItem().equals(inventory.getItem(event.getHotbarButton()))) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (SelectorMenuGui.getSelectorHandItem().equals(event.getCurrentItem()) || SelectorMenuGui.getSelectorHandItem().equals(event.getCursor())) {
                event.setCancelled(true);
                return;
            }
            Map.Entry<Inventory, String> foundEntry = SelectorMenuGui.getGUISWithNames().entrySet().stream().filter(v ->
                    v.getValue().equals(inventoryName)).findFirst().orElse(null);
            if (foundEntry == null) {
                return;
            }
            String foundInventoryName = foundEntry.getValue();
            Map.Entry<String, Inventory> foundEntry2 = SelectorMenuGui.getGUIS().entrySet().stream().filter(v -> foundEntry.getKey().equals(v.getValue())).findFirst().orElse(null);
            String foundPath = foundEntry2.getKey();
            if (!foundInventoryName.equals(inventoryName)) {
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
                    if (guiName.equals("MainGUI")) {
                        player.openInventory(SelectorMenuGui.getGUIS().get("MainGUI"));
                    } else {
                        player.openInventory(SelectorMenuGui.getGUIS().get("GoToGUI."+guiName));
                    }
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
