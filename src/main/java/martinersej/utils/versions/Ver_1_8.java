package martinersej.utils.versions;

import martinersej.utils.Version;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;


@SuppressWarnings("deprecation")
public class Ver_1_8 extends Version {

    @Override
    public String getEnchantmentKey(Enchantment enchantment){
        return enchantment.getName();
    }

    @Nullable
    @Override
    public Enchantment getEnchantmentFromKey(String key){
        return Enchantment.getByName(key);
    }
}

