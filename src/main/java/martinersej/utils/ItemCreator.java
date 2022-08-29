package martinersej.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import martinersej.exceptions.ParseException;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemCreator {
    private ItemStack item;

    public ItemCreator(ItemStack paramItemStack) {
        this.item = paramItemStack;
    }

    public static ItemStack build(String ParamString) throws ParseException {
        JsonObject json;
        try{
            json = new JsonParser().parse(ParamString).getAsJsonObject();
        }catch (JsonSyntaxException | IllegalStateException ex){
            throw new ParseException("There is an error in the json syntax of item: " + ParamString);
        }

        if (!json.has("type")){
            throw new ParseException("Missing type tag");
        }

        Material material;
        if (json.get("type").getAsString() != null && UniversalMaterial.ofType(Material.valueOf(json.get("type").getAsString().toUpperCase().replace(" ", "_"))) != null) {
            material = UniversalMaterial.ofType(Material.valueOf(json.get("type").getAsString().toUpperCase().replace(" ", "_"))).getType();
        } else {
            try {
                material = Material.valueOf(json.get("type").getAsString().toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException ex){
                throw new ParseException("Invalid item type: " + json.get("type").getAsString() + " does the item exist on this minecraft version?");
            }
        }
        ItemCreator item = new ItemCreator(new ItemStack(material));
        try{
            for (Map.Entry<String, JsonElement> entry : json.entrySet()){
                switch (entry.getKey().toLowerCase()) {
                    case "type":
                        continue;
                    case "data":
                        item.setData(entry.getValue().getAsByte());
                        break;
                    case "amount":
                        item.setAmount(entry.getValue().getAsInt());
                        break;
                    case "durability":
                        item.setDurability(entry.getValue().getAsShort());
                        break;
                    case "display-name":
                    case "displayname":
                        item.setDisplayName(entry.getValue().getAsString());
                        break;
                    case "lore":
                        item.setLore(entry.getValue());
                        break;
                    case "enchantments":
                        Enchantment enchantment = null;
                        int level = 0;
                        for (JsonElement jsonElement : entry.getValue().getAsJsonArray()) {
                            JsonObject enchant = jsonElement.getAsJsonObject();
                            enchantment = Version.getVersionUtils().getEnchantmentFromKey(enchant.get("type").getAsString());
                            Validate.notNull(enchantment, "Unknown enchantment type: " + enchant.get("type").getAsString());
                            level = enchant.get("level").getAsInt();
                        }
                        item.addEnchantment(enchantment, level);
                        break;
                    case "glowing":
                    case "glow":
                        item.setGlowing(entry.getValue().getAsBoolean());
                        break;
                    case "skull-data":
                    case "skulldata":
                        if (UniversalMaterial.ofType(item.getType()) != null && (UniversalMaterial.ofType(item.getType()).equals(UniversalMaterial.SKELETON_SKULL) || UniversalMaterial.ofType(item.getType()).equals(UniversalMaterial.PLAYER_HEAD))) {
                            item.setSkullData(entry.getValue().getAsString());
                        }
                        break;
                }
            }
            return item.item;
        }catch (Exception ex){
            if (ex instanceof ParseException){
                throw ex;
            }
            ParseException exception = new ParseException(ex.getMessage());
            ex.setStackTrace(ex.getStackTrace());
            throw exception;
        }
    }

    public Material getType() {
        return this.item.getType();
    }

    public void setType(Material paramMaterial) {
        this.item.setType(paramMaterial);
    }

    public void setData(Byte paramByte) {
        this.item.getData().setData(paramByte);
    }

    public void setAmount(int paramInt) {
        this.item.setAmount(paramInt);
    }

    public String getDisplayName() {
        ItemMeta itemMeta = this.item.getItemMeta();
        return Color.plain(itemMeta.getDisplayName());
    }

    public boolean hasDisplayName() {
        ItemMeta itemMeta = this.item.getItemMeta();
        return itemMeta.hasDisplayName();
    }

    public void setDisplayName(String paramString) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(Color.colored(paramString));
        this.item.setItemMeta(itemMeta);
    }

    public void setDurability(short paramShort) {
        this.item.setDurability(paramShort);
    }

    public List<String> getLore() {
        ItemMeta itemMeta = this.item.getItemMeta();
        return (itemMeta.hasLore() && itemMeta.getLore() != null) ? itemMeta.getLore() : new ArrayList<>();
    }

    public boolean hasLore() {
        ItemMeta itemMeta = this.item.getItemMeta();
        return itemMeta.hasLore();
    }

    public void setLore(JsonElement paramVarArgs) {
        ItemMeta itemMeta = this.item.getItemMeta();
        ArrayList<String> arrayList = new ArrayList<>();
        for (JsonElement str : paramVarArgs.getAsJsonArray()) {
            arrayList.add(Color.colored(str.getAsString()));
        }
        itemMeta.setLore(arrayList);
        this.item.setItemMeta(itemMeta);
    }

    public void setLore(ArrayList<String> paramVarArgs) {
        ItemMeta itemMeta = this.item.getItemMeta();
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : paramVarArgs) {
            arrayList.add(Color.colored(str));
        }
        itemMeta.setLore(arrayList);
        this.item.setItemMeta(itemMeta);
    }

    public void addLore(String... paramVarArgs) {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta.getLore() == null)
            itemMeta.setLore(new ArrayList<>());
        List<String> list = itemMeta.getLore();
        for (String str : paramVarArgs) {
            list.add(Color.colored(str));
        }
        itemMeta.setLore(list);
        this.item.setItemMeta(itemMeta);
    }

    public void addEnchantment(Enchantment paramEnchantment, int paramInt) {
        this.item.addUnsafeEnchantment(paramEnchantment, paramInt);
    }

    public ItemMeta getItemMeta() {
        return this.item.getItemMeta();
    }

    public void setItemMeta(ItemMeta paramItemMeta) {
        this.item.setItemMeta(paramItemMeta);
    }

    public boolean getGlowing() {
        Set<ItemFlag> flags = this.item.getItemFlags();
        System.out.println(flags);
        System.out.println(flags.contains(ItemFlag.HIDE_ENCHANTS));
        return flags.contains(ItemFlag.HIDE_ENCHANTS);
    }

    public void setGlowing(boolean glow) {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (glow) {
            if (this.item.getType() == Material.FISHING_ROD) {
                itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
            } else {
                itemMeta.addEnchant(Enchantment.LURE, 1, true);
            }
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            for (Enchantment enchantment : itemMeta.getEnchants().keySet()) {
                itemMeta.removeEnchant(enchantment);
            }

        }
        this.item.setItemMeta(itemMeta);
    }

    public void setSkullData(String ParamString) {
        if(isBase64(ParamString)) {
            this.item = SkullCreator.itemFromBase64(ParamString);
            /**
             * Temp fix, I'm lazy to implement SkullCreator to this Class.
            */
            //this.setAmount(itemC.getAmount());
            //this.setGlowing(itemC.getGlowing());
            //this.setDisplayName(itemC.getDisplayName());
            //this.setLore(new ArrayList<>(this.getLore()));
            //this.setDurability(itemC.getDurability());
        }
    }

    public ItemStack getItemStack() {
        return this.item;
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
        return Color.plain(item.getItemMeta().getDisplayName());
    }
}
