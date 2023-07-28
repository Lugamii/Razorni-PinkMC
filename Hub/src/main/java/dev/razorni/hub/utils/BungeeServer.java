package dev.razorni.hub.utils;

import dev.razorni.hub.utils.shits.BungeeUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BungeeServer {

    private String name;
    private int playerCount;

    public BungeeServer(String name) {
        this.name = name;
        this.playerCount = 0;
        BungeeUtils.getServersByName().put(name, this);
        BungeeUtils.getServers().add(this);
        BungeeUtils.getServersName().add(name);
    }
}
