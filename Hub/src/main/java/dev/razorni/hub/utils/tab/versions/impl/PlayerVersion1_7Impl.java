package dev.razorni.hub.utils.tab.versions.impl;

import dev.razorni.hub.utils.tab.versions.module.IPlayerVersion;
import dev.razorni.hub.utils.tab.versions.module.PlayerVersion;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 22/09/2021
 */
public class PlayerVersion1_7Impl implements IPlayerVersion {

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(
                ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion()
        );
    }

}
