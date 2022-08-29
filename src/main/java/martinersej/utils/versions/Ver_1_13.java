package martinersej.utils.versions;

import martinersej.utils.Version;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;
import java.util.logging.Logger;


@SuppressWarnings("deprecation")
public class Ver_1_13 extends Version {

    private static final Logger LOGGER = Logger.getLogger(Ver_1_13.class.getCanonicalName());



    @Override
    public String getEnchantmentKey(Enchantment enchantment){
        return enchantment.getKey().getKey();
    }

    @Nullable
    @Override
    public Enchantment getEnchantmentFromKey(String key) {
        Enchantment enchantment = Enchantment.getByName(key);

        if (enchantment != null) {
            LOGGER.warning("Using old deprecated enchantment names, replace: " + key + " with " + enchantment.getKey().getKey());
            return enchantment;
        }

        NamespacedKey namespace;

        try {
            namespace = NamespacedKey.minecraft(key);
        } catch (IllegalArgumentException ignored) {
            return null;
        }

        return Enchantment.getByKey(namespace);
    }
}

