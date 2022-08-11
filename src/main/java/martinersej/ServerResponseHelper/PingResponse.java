package martinersej.ServerResponseHelper;

import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import martinersej.LeuxServerSelector;

public class PingResponse
{
    private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;

    public PingResponse(final boolean isOnline, final String motd, final int onlinePlayers, final int maxPlayers) {
        this.isOnline = isOnline;
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public PingResponse(final String jsonString, final String address, final int port) {

        if (jsonString == null || jsonString.isEmpty()) {
            this.motd = "Invalid ping response";
            LeuxServerSelector.getInstance().getLogger().log(Level.WARNING, "Received empty Json response from IP \"" + address.toString() + "\"!");
            return;
        }

        final Object jsonObject = JSONValue.parse(jsonString);

        if (!(jsonObject instanceof JSONObject)) {
            this.motd = "Invalid ping response";
            LeuxServerSelector.getInstance().getLogger().log(Level.WARNING, "Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
            return;
        }

        final JSONObject json = (JSONObject) jsonObject;
        this.isOnline = true;

        final Object descriptionObject = json.get("description");

        if (descriptionObject != null) {
            if (descriptionObject instanceof JSONObject) {
                final Object text = ((JSONObject) descriptionObject).get("text");
                if (text != null) {
                    this.motd = text.toString();
                } else {
                    this.motd = "Invalid ping response (text not found)";
                }
            } else {
                this.motd = descriptionObject.toString();
            }
        } else {
            this.motd = "error: motd not returned by server";
        }

        final Object playersObject = json.get("players");

        if (playersObject instanceof JSONObject) {
            final JSONObject playersJson = (JSONObject) playersObject;

            final Object onlineObject = playersJson.get("online");
            if (onlineObject instanceof Number) {
                this.onlinePlayers = ((Number) onlineObject).intValue();
            }

            final Object maxObject = playersJson.get("max");
            if (maxObject instanceof Number) {
                this.maxPlayers = ((Number) maxObject).intValue();
            }
        }
    }

    public boolean isOnline() {
        if (this == null) { return false; }
        return this.isOnline;
    }

    public String getMotd() {
        return this.motd;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

}