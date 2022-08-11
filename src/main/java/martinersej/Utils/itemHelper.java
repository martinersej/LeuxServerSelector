package martinersej.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static org.bukkit.Material.AIR;

public class itemHelper {
    public static ItemStack item(Material material, Integer amount, String name, List<String> lore) {
        material = (null != material) ? material : AIR;
        amount = (amount > 0 && amount <= 64) ? amount : 1;
        ItemStack item = new ItemStack(material, amount);
        if (name == null && lore == null){
            return item;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (name != null) {
            itemMeta.setDisplayName(Chat.colored(name));
        }
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, Chat.colored(lore.get(i)));
            }
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static String displayName(ItemStack item) {
        return Chat.plain(item.getItemMeta().getDisplayName());
    }
}
