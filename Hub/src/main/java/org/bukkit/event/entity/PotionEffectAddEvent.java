package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

/**
 * Called when a potion effect is applied to an entity, or an existing effect is extended or upgraded
 */
public class PotionEffectAddEvent extends PotionEffectEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    public PotionEffectAddEvent(LivingEntity entity, PotionEffect effect) {
        super(entity, effect);
    }

    public static HandlerList getHandlerList() {
        return PotionEffectAddEvent.HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PotionEffectAddEvent.HANDLER_LIST;
    }
}