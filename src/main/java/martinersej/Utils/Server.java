package martinersej.Utils;

import martinersej.LeuxServerSelector;

public class Server {
    public static String getPrefix() {
        return LeuxServerSelector.configYML.getString("Prefix");
    }
}