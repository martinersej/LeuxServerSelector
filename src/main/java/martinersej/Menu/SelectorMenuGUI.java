package martinersej.Menu;

import martinersej.LeuxServerSelector;
import martinersej.Menu.Placeholder.forGuis;
import martinersej.Model.Server;
import martinersej.Utils.Chat;
import martinersej.Utils.itemHelper;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectorMenuGUI {

    private static ItemStack selectorHandItem;
    private static Inventory selectorGUI;
    private static int selectorHandItemSlot;
    private static Map<String, Inventory> menus;
    private static Map<String, Inventory> slotWithPlaceholderLore;

    public SelectorMenuGUI() {}

    public static void SelectorHand() {
        Material material = Material.valueOf(LeuxServerSelector.menuYML.getString("MainItem.Material").toUpperCase().replaceAll(" ", "_")) ;
        int amount = LeuxServerSelector.menuYML.getInt("MainItem.Amount");
        String name = LeuxServerSelector.menuYML.getString("MainItem.Name");
        List<String> lore = LeuxServerSelector.menuYML.getStringList("MainItem.Lore");
        selectorHandItemSlot = LeuxServerSelector.menuYML.getInt("MainItem.PlayerSlot");
        if (selectorHandItemSlot > 9 || selectorHandItemSlot < 1) {
        } else {
            selectorHandItemSlot -= 1;
        }
        selectorHandItem = itemHelper.item(material, amount, name, lore);
    }

    public static int SelectorHandSlot() {
        return selectorHandItemSlot;
    }

    public static void SelectorMenuGUI() {
        menus = new HashMap<>();
        slotWithPlaceholderLore = new HashMap<>();
        forGuis.addDefaultPlaceholder();
        Pattern pattern = Pattern.compile("[%]serverselector_\\w+::\\w+[%]");
        if (LeuxServerSelector.menuYML.getString("MainGUI.Type").equalsIgnoreCase("chest")) {
            selectorGUI = Bukkit.createInventory(null, 9*LeuxServerSelector.menuYML.getInt("MainGUI.Rows"), Chat.colored(LeuxServerSelector.menuYML.getString("MainGUI.Name")));
        } else if (LeuxServerSelector.menuYML.getString("MainGUI.Type").equalsIgnoreCase("hopper")) {
            selectorGUI = Bukkit.createInventory(null, InventoryType.HOPPER, Chat.colored(LeuxServerSelector.menuYML.getString("MainGUI.Name")));
        }
        for (String slot : LeuxServerSelector.menuYML.getConfigurationSection("MainGUI.Slot").getKeys(false)) {
            Material material = Material.valueOf(LeuxServerSelector.menuYML.getString("MainGUI.Slot."+slot+".Material").toUpperCase().replaceAll(" ", "_"));
            int amount = LeuxServerSelector.menuYML.getInt("MainGUI.Slot."+slot+".Amount");
            String name = LeuxServerSelector.menuYML.getString("MainGUI.Slot."+slot+".Name");
            List<String> lore = LeuxServerSelector.menuYML.getStringList("MainGUI.Slot."+slot+".Lore");
            for (String singleLore : lore) {
                if (LeuxServerSelector.getPlaceholderAPI()) {
                    if (PlaceholderAPI.containsPlaceholders(singleLore)) {
                        slotWithPlaceholderLore.put("MainGUI.Slot." + slot, selectorGUI);
                        break;
                    }
                } else {
                    Matcher matcher = pattern.matcher(singleLore);
                    boolean matchCheck = matcher.find();
                    if (matchCheck) {
                        String[] placeholderList = matcher.group(0).replaceAll("%", "").replace("serverselector_", "").split("::", 2);
                        Server server = LeuxServerSelector.getServers().values().stream().filter(v -> v.name.equalsIgnoreCase(placeholderList[1])).findFirst().orElse(null);
                        if (forGuis.contains(placeholderList[0].toLowerCase()) && server != null) {
                            slotWithPlaceholderLore.put("MainGUI.Slot."+slot, selectorGUI);
                            break;
                        }
                    }
                }
            }
            selectorGUI.setItem(Integer.parseInt(slot)-1, itemHelper.item(material, amount, name, lore));
        }
        menus.put("MainGUI", selectorGUI);
        for (String gui : LeuxServerSelector.menuYML.getConfigurationSection("GoToGUI").getKeys(false)) {
            Inventory goToGUI = null;
            if (LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Type").equalsIgnoreCase("chest")) {
                goToGUI = Bukkit.createInventory(null, 9*LeuxServerSelector.menuYML.getInt("GoToGUI."+gui+".Rows"), Chat.colored(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Name")));
            } else if (LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Type").equalsIgnoreCase("hopper")) {
                goToGUI = Bukkit.createInventory(null, InventoryType.HOPPER, Chat.colored(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Name")));
            }
            for (String slot : LeuxServerSelector.menuYML.getConfigurationSection("GoToGUI."+gui+".Slot").getKeys(false)) {
                Material material = Material.valueOf(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Slot."+slot+".Material").toUpperCase().replaceAll(" ", "_")) ;
                int amount = LeuxServerSelector.menuYML.getInt("GoToGUI."+gui+".Slot."+slot+".Amount");
                String name = LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Slot."+slot+".Name");
                List<String> lore = LeuxServerSelector.menuYML.getStringList("GoToGUI."+gui+".Slot."+slot+".Lore");
                for (String singleLore : lore) {
                    if (LeuxServerSelector.getPlaceholderAPI()) {
                        if (PlaceholderAPI.containsPlaceholders(singleLore)) {
                            slotWithPlaceholderLore.put("GoToGUI." + gui + ".Slot." + slot, goToGUI);
                            break;
                        }
                    } else {
                        Matcher matcher = pattern.matcher(singleLore);
                        boolean matchCheck = matcher.find();
                        if (matchCheck) {
                            String[] placeholderList = matcher.group(0).replaceAll("%", "").replace("serverselector_", "").split("::", 2);
                            Server server = LeuxServerSelector.getServers().values().stream().filter(v -> v.name.equalsIgnoreCase(placeholderList[1])).findFirst().orElse(null);
                            if (forGuis.contains(placeholderList[0].toLowerCase()) && server != null) {
                                slotWithPlaceholderLore.put("GoToGUI."+gui+".Slot."+slot, goToGUI);
                                break;
                            }
                        }
                    }
                }
                goToGUI.setItem(Integer.parseInt(slot)-1, itemHelper.item(material, amount, name, lore));
            }
            menus.put("GoToGUI."+gui, goToGUI);
        }
        updateAllLores();
    }

    public static void updateAllLores() {
        List<String> placeholderChecked = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                Pattern pattern = Pattern.compile("[%]serverselector_\\w+::\\w+[%]");
                for (Map.Entry<String, Inventory> entry : slotWithPlaceholderLore.entrySet()) {
                    Inventory gui = entry.getValue();
                    String[] ymlSplit = entry.getKey().split("\\.");
                    String ymlSlot = ymlSplit[ymlSplit.length-1];
                    int slot = Integer.parseInt(ymlSlot) - 1;
                    ItemStack item = gui.getItem(slot);
                    ItemMeta itemMeta = item.getItemMeta();
                    List<String> lores = LeuxServerSelector.menuYML.getStringList(entry.getKey()+".Lore");
                    for (int i = 0; i < lores.size(); i++) {
                        if (LeuxServerSelector.getPlaceholderAPI()) {
                            while (PlaceholderAPI.containsPlaceholders(lores.get(i)) && !placeholderChecked.contains(lores.get(i))) {// && !matchCheck) {
                                lores.set(i, PlaceholderAPI.setPlaceholders(null, lores.get(i)));
                                placeholderChecked.add(lores.get(i));
                            }
                        } else {
                            Matcher matcher = pattern.matcher(lores.get(i));
                            boolean matchCheck = matcher.find();
                            while (matchCheck && !placeholderChecked.contains(matcher.group(0))) {
                                String[] placeholderList = matcher.group(0).replaceAll("%", "").replace("serverselector_", "").split("::", 2);
                                Server server = LeuxServerSelector.getServers().values().stream().filter(s -> s.name.equalsIgnoreCase(placeholderList[1].toLowerCase())).findFirst().orElse(null);
                                if (server == null) {
                                    placeholderChecked.add(matcher.group(0));
                                }
                                if (forGuis.contains(placeholderList[0].toLowerCase())) {
                                    lores.set(i, forGuis.editPlacholder(lores.get(i), placeholderList[0], placeholderList[1], server));
                                } else {
                                    placeholderChecked.add(matcher.group(0));
                                }
                                matcher = pattern.matcher(lores.get(i));
                                matchCheck = matcher.find();
                            }
                        }
                    }
                    itemMeta.setLore(lores);
                    item.setItemMeta(itemMeta);
                    gui.setItem(slot, item);
                }
            }
        }.runTaskTimer(LeuxServerSelector.getInstance(), 30, 60);
    }

    public static Map<String, Inventory> getGUIS() {
        return menus;
    }

    public static Inventory getSelectorGUI(){
        return selectorGUI;
    }

    public static ItemStack getSelectorHandItem(){
        return selectorHandItem;
    }
}