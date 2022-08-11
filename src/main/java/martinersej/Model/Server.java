package martinersej.Model;

public class Server {
    public String name;
    private String motd = null;
    private boolean online = false;
    private int playerCount = 0;
    private int maxPlayerCount = 0;
    private String ip = null;
    private int port = 0;

    public Server(String servername) {
        this.name = servername;
    }

    public String getMotd() {
        return this.motd;
    }

    public boolean getOnline() {
        return this.online;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    public String getIP() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
