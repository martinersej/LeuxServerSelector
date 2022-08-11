package martinersej.Event;


import martinersej.LeuxServerSelector;
import martinersej.Menu.SelectorMenuGUI;
import martinersej.Utils.itemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class onHubConnection implements Listener {
    public onHubConnection(LeuxServerSelector plugin) {
        LeuxServerSelector.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        inventory.clear();
        LeuxServerSelector.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(LeuxServerSelector.getInstance(), () -> {
            inventory.setItem(SelectorMenuGUI.SelectorHandSlot(), SelectorMenuGUI.getSelectorHandItem());
        }, 8);
    }

    @EventHandler
    public void onCompassInteraction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player == null) {
            return;
        }
        Inventory inventory = event.getClickedInventory();
        ItemStack item = null;
        if (inventory != null) {
            item = inventory.getItem(event.getSlot());
        }
        if (item == null) {
            return;
        }
        if (item.getType().equals(SelectorMenuGUI.getSelectorHandItem().getType()) && itemHelper.displayName(item).equals(itemHelper.displayName(SelectorMenuGUI.getSelectorHandItem()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCompassDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType().equals(SelectorMenuGUI.getSelectorHandItem().getType()) && itemHelper.displayName(item).equals(itemHelper.displayName(SelectorMenuGUI.getSelectorHandItem()))) {
            event.setCancelled(true);
        }
    }
}