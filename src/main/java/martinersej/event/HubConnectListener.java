package martinersej.event;


import martinersej.LeuxServerSelector;
import martinersej.menu.SelectorMenuGui;
import martinersej.utils.ItemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class HubConnectListener implements Listener {
    public HubConnectListener(LeuxServerSelector plugin) {
        LeuxServerSelector.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        inventory.clear();
        LeuxServerSelector.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(LeuxServerSelector.getInstance(), () -> {
            inventory.setItem(SelectorMenuGui.SelectorHandSlot(), SelectorMenuGui.getSelectorHandItem());
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
        if (item.getType().equals(SelectorMenuGui.getSelectorHandItem().getType()) && ItemHelper.displayName(item).equals(ItemHelper.displayName(SelectorMenuGui.getSelectorHandItem()))) {
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
        if (item.getType().equals(SelectorMenuGui.getSelectorHandItem().getType()) && ItemHelper.displayName(item).equals(ItemHelper.displayName(SelectorMenuGui.getSelectorHandItem()))) {
            event.setCancelled(true);
        }
    }
}