package martinersej.utils;

import martinersej.LeuxServerSelector;
import martinersej.utils.versions.*;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;
import io.papermc.lib.PaperLib;
/**
@author https://gitlab.com/uhccore/uhccore/-/blob/main/src/main/java/com/gmail/val59000mc/utils/VersionUtils.java
*/
public abstract class Version {

    private static Version versionUtils = null;

    public static Version getVersionUtils() {
        if (versionUtils == null) {
            int version = PaperLib.getMinecraftVersion();
            if (version < 12) {
                versionUtils = new Ver_1_8();
            } else if (version == 12) {
                versionUtils = new Ver_1_12();
            } else if (version == 13) {
                versionUtils = new Ver_1_13();
            } else if (version == 14) {
                versionUtils = new Ver_1_14();
            } else {
                versionUtils = new Ver_1_15();
            }
        }
        return versionUtils;
    }

    public abstract String getEnchantmentKey(Enchantment enchantment);

    @Nullable
    public abstract Enchantment getEnchantmentFromKey(String key);
}
