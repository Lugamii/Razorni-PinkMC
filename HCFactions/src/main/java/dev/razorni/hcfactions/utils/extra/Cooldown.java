package dev.razorni.hcfactions.utils.extra;

import dev.razorni.hcfactions.extras.framework.Manager;
import dev.razorni.hcfactions.utils.Formatter;
import dev.razorni.hcfactions.utils.Tasks;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {

    private final Map<UUID, Long> cooldowns;

    public Cooldown(Manager manager) {
        this.cooldowns = new HashMap<>();
        Tasks.executeScheduled(manager, 6000, this::clean);
    }

    public boolean hasCooldown(Player player) {
        return this.cooldowns.containsKey(player.getUniqueId()) && this.cooldowns.get(player.getUniqueId()) >= System.currentTimeMillis();
    }

    public void removeCooldown(Player player) {
        this.cooldowns.remove(player.getUniqueId());
    }

    public void applyCooldown(Player player, int HCF) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + HCF * 1000L);
    }

    public String getRemaining(Player player) {
        long strings = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
        return Formatter.getRemaining(strings, true);
    }

    private void clean() {
        this.cooldowns.values().removeIf(time -> time - System.currentTimeMillis() < 0L);
    }

    public void applyCooldownTicks(Player player, int HCF) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + HCF);
    }
}
