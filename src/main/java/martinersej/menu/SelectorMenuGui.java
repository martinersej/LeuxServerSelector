package martinersej.menu;

import martinersej.LeuxServerSelector;
import martinersej.exceptions.ParseException;
import martinersej.menu.placeholder.GuiCustomPlaceholder;
import martinersej.model.Server;
import martinersej.utils.Color;
import martinersej.utils.ItemCreator;
import martinersej.utils.UniversalMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectorMenuGui {

    private static ItemStack selectorHandItem;
    private static Inventory selectorGUI;
    private static BukkitTask loreUpdater;
    private static int selectorHandItemSlot;
    private static Map<String, Inventory> menus;
    private static Map<Inventory, String> menusAndNames;
    private static Map<String, Inventory> slotWithPlaceholderLore;

    public SelectorMenuGui() {}

    public static void SelectorHand() throws ParseException {
        String jsonString = LeuxServerSelector.menuYML.getString("MainItem.Item");
        ItemStack item = ItemCreator.build(jsonString);
        ItemCreator cItem = new ItemCreator(item);
        //int amount = LeuxServerSelector.menuYML.getInt("MainItem.Amount");
        //String base64 = LeuxServerSelector.menuYML.getString("MainItem.SkullData");
        //String name = LeuxServerSelector.menuYML.getString("MainItem.Name");
        //List<String> lore = LeuxServerSelector.menuYML.getStringList("MainItem.Lore");
        selectorHandItemSlot = LeuxServerSelector.menuYML.getInt("MainItem.PlayerSlot");
        if (selectorHandItemSlot > 9 || selectorHandItemSlot < 1) {
        } else {
            selectorHandItemSlot -= 1;
        }
        selectorHandItem = item;
    }

    public static int SelectorHandSlot() {
        return selectorHandItemSlot;
    }

    public static void SelectorMenuGUI() throws ParseException {
        menus = new HashMap<>();
        menusAndNames = new HashMap<>();
        slotWithPlaceholderLore = new HashMap<>();
        GuiCustomPlaceholder.addDefaultPlaceholder();
        Pattern pattern = Pattern.compile("%serverselector_\\w+::\\w+%");
        if (LeuxServerSelector.menuYML.getString("MainGUI.Type").equalsIgnoreCase("chest")) {
            selectorGUI = Bukkit.createInventory(null, 9*LeuxServerSelector.menuYML.getInt("MainGUI.Rows"), Color.colored(LeuxServerSelector.menuYML.getString("MainGUI.Name")));
        } else if (LeuxServerSelector.menuYML.getString("MainGUI.Type").equalsIgnoreCase("hopper")) {
            selectorGUI = Bukkit.createInventory(null, InventoryType.HOPPER, Color.colored(LeuxServerSelector.menuYML.getString("MainGUI.Name")));
        }
        for (String slot : LeuxServerSelector.menuYML.getConfigurationSection("MainGUI.Slot").getKeys(false)) {
            String jsonString = LeuxServerSelector.menuYML.getString("MainGUI.Slot."+slot+".Item");
            ItemStack item = ItemCreator.build(jsonString);
            ItemCreator cItem = new ItemCreator(item);
            //int amount = LeuxServerSelector.menuYML.getInt("MainGUI.Slot."+slot+".Amount");
            //String name = LeuxServerSelector.menuYML.getString("MainGUI.Slot."+slot+".Name");
            //String base64 = LeuxServerSelector.menuYML.getString("MainGUI.Slot."+slot+".SkullData");
            //List<String> lore = LeuxServerSelector.menuYML.getStringList("MainGUI.Slot."+slot+".Lore");
            for (String singleLore : cItem.getLore()) {
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
                        if (GuiCustomPlaceholder.contains(placeholderList[0].toLowerCase()) && server != null) {
                            slotWithPlaceholderLore.put("MainGUI.Slot."+slot, selectorGUI);
                            break;
                        }
                    }
                }
            }
            selectorGUI.setItem(Integer.parseInt(slot)-1, item);
        }
        menus.put("MainGUI", selectorGUI);
        menusAndNames.put(selectorGUI, Color.colored(LeuxServerSelector.menuYML.getString("MainGUI.Name")));
        if (LeuxServerSelector.menuYML.getConfigurationSection("GoToGUI") != null) {
            for (String gui : LeuxServerSelector.menuYML.getConfigurationSection("GoToGUI").getKeys(false)) {
                Inventory goToGUI = null;
                if (LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Type").equalsIgnoreCase("chest")) {
                    goToGUI = Bukkit.createInventory(null, 9*LeuxServerSelector.menuYML.getInt("GoToGUI."+gui+".Rows"), Color.colored(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Name")));
                } else if (LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Type").equalsIgnoreCase("hopper")) {
                    goToGUI = Bukkit.createInventory(null, InventoryType.HOPPER, Color.colored(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Name")));
                }
                for (String slot : LeuxServerSelector.menuYML.getConfigurationSection("GoToGUI."+gui+".Slot").getKeys(false)) {
                    String jsonString = LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Slot."+slot+".Item");
                    ItemStack item = ItemCreator.build(jsonString);
                    ItemCreator cItem = new ItemCreator(item);
                    //int amount = LeuxServerSelector.menuYML.getInt("GoToGUI."+gui+".Slot."+slot+".Amount");
                    //String name = LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Slot."+slot+".Name");
                    //String base64 = LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Slot."+slot+".SkullData");
                    //List<String> lore = LeuxServerSelector.menuYML.getStringList("GoToGUI."+gui+".Slot."+slot+".Lore");
                    for (String singleLore : cItem.getLore()) {
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
                                if (GuiCustomPlaceholder.contains(placeholderList[0].toLowerCase()) && server != null) {
                                    slotWithPlaceholderLore.put("GoToGUI."+gui+".Slot."+slot, goToGUI);
                                    break;
                                }
                            }
                        }
                    }
                    goToGUI.setItem(Integer.parseInt(slot)-1, item);
                }
                menus.put("GoToGUI."+gui, goToGUI);
                menusAndNames.put(goToGUI, Color.colored(LeuxServerSelector.menuYML.getString("GoToGUI."+gui+".Name")));
            }
        }
        updateAllLores();
    }

    public static void updateAllLores() {
        List<String> placeholderChecked = new ArrayList<>();
        loreUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                Pattern pattern = Pattern.compile("[%]serverselector_\\w+::\\w+[%]");
                for (Map.Entry<String, Inventory> entry : slotWithPlaceholderLore.entrySet()) {
                    Inventory gui = entry.getValue();
                    String[] ymlSplit = entry.getKey().split("\\.");
                    String ymlSlot = ymlSplit[ymlSplit.length-1];
                    int slot = Integer.parseInt(ymlSlot) - 1;
                    ItemStack item = gui.getItem(slot);
                    ItemMeta itemMeta = item.getItemMeta() != null ? item.getItemMeta() : UniversalMaterial.ofType(item.getType()).getStack().getItemMeta();
                    List<String> lores = new ItemCreator(item).getLore();
                    for (int i = 0; i < lores.size(); i++) {
                        if (LeuxServerSelector.getPlaceholderAPI()) {
                            while (PlaceholderAPI.containsPlaceholders(lores.get(i)) && !placeholderChecked.contains(lores.get(i))) {
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
                                if (GuiCustomPlaceholder.contains(placeholderList[0].toLowerCase())) {
                                    lores.set(i, GuiCustomPlaceholder.editPlacholder(lores.get(i), placeholderList[0], placeholderList[1], server));
                                } else {
                                    placeholderChecked.add(matcher.group(0));
                                }
                                matcher = pattern.matcher(lores.get(i));
                                matchCheck = matcher.find();
                            }
                        }
                    }
                    for (int i = 0; i < lores.size(); i++) {
                        lores.set(i, Color.colored(lores.get(i)));
                    }
                    itemMeta.setLore(lores);
                    item.setItemMeta(itemMeta);
                    gui.setItem(slot, item);
                }
            }
        }.runTaskTimerAsynchronously(LeuxServerSelector.getInstance(), 0, LeuxServerSelector.configYML.getInt("UpdateLoresTimer"));
    }

    public static BukkitTask getLoreUpdaterTask() { return loreUpdater; }

    public static Map<String, Inventory> getGUIS() {
        return menus;
    }

    public static Map<Inventory, String> getGUISWithNames() {
        return menusAndNames;
    }

    public static Inventory getSelectorGUI(){
        return selectorGUI;
    }

    public static ItemStack getSelectorHandItem(){
        return selectorHandItem;
    }
}