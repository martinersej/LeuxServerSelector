package martinersej.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Base64;
import java.util.List;

public class ItemHelper {
    public static ItemStack item(Material material, String base64, Integer amount, String name, List<String> lore) {
        material = (null != material) ? material : UniversalMaterial.AIR.getType();
        if (material != null && UniversalMaterial.ofType(material) != null) {
            material = UniversalMaterial.ofType(material).getType();
        }
        amount = (amount > 0 && amount <= 64) ? amount : 1;
        ItemStack item = new ItemStack(material, amount);
        if (name == null && lore == null){
            return item;
        }
        if (UniversalMaterial.ofType(material) != null && (UniversalMaterial.ofType(material).equals(UniversalMaterial.SKELETON_SKULL) || UniversalMaterial.ofType(material).equals(UniversalMaterial.PLAYER_HEAD))) {
            if(isBase64(base64)) {
                item = SkullCreator.itemFromBase64(base64);
                item.setAmount(amount);
            }
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

    public static boolean isBase64(String path) {
        if (path == null) return false;
        try {
            Base64.getDecoder().decode(path);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static String displayName(ItemStack item) {
        return Chat.plain(item.getItemMeta().getDisplayName());
    }
}
