package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a living entity, such as a monster or player
 */
public interface LivingEntity extends Entity, Damageable, ProjectileSource {

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight();

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @param ignoreSneaking if set to true, the effects of sneaking will be
     *                       ignored
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight(boolean ignoreSneaking);

    /**
     * Get a Location detailing the current eye position of the living entity.
     *
     * @return a location at the eyes of the living entity
     */
    public Location getEyeLocation();

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     * sight
     * @deprecated Magic value
     */
    @Deprecated
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance);

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     * sight
     */
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance);

    /**
     * Gets the block that the living entity has targeted.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     * @deprecated Magic value
     */
    @Deprecated
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance);

    /**
     * Gets the block that the living entity has targeted.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     */
    public Block getTargetBlock(Set<Material> transparent, int maxDistance);

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *                    further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     * line of sight
     * @deprecated Magic value
     */
    @Deprecated
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance);

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *                    further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     * line of sight
     */
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance);

    /**
     * Throws an egg from the living entity.
     *
     * @return the egg thrown
     * @deprecated use launchProjectile(Egg.class) instead
     */
    @Deprecated
    public Egg throwEgg();

    /**
     * Throws a snowball from the living entity.
     *
     * @return the snowball thrown
     * @deprecated use launchProjectile(Snowball.class) instead
     */
    @Deprecated
    public Snowball throwSnowball();

    /**
     * Shoots an arrow from the living entity.
     *
     * @return the arrow shot
     * @deprecated use launchProjectile(Arrow.class) instead
     */
    @Deprecated
    public Arrow shootArrow();

    /**
     * Returns the amount of air that the living entity has remaining, in
     * ticks.
     *
     * @return amount of air remaining
     */
    public int getRemainingAir();

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     *
     * @param ticks amount of air remaining
     */
    public void setRemainingAir(int ticks);

    /**
     * Returns the maximum amount of air the living entity can have, in ticks.
     *
     * @return maximum amount of air
     */
    public int getMaximumAir();

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     *
     * @param ticks maximum amount of air
     */
    public void setMaximumAir(int ticks);

    /**
     * Returns the living entity's current maximum no damage ticks.
     * <p>
     * This is the maximum duration in which the living entity will not take
     * damage.
     *
     * @return maximum no damage ticks
     */
    public int getMaximumNoDamageTicks();

    /**
     * Sets the living entity's current maximum no damage ticks.
     *
     * @param ticks maximum amount of no damage ticks
     */
    public void setMaximumNoDamageTicks(int ticks);

    /**
     * Returns the living entity's last damage taken in the current no damage
     * ticks time.
     * <p>
     * Only damage higher than this amount will further damage the living
     * entity.
     *
     * @return damage taken since the last no damage ticks time period
     */
    public double getLastDamage();

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     *
     * @param damage amount of damage
     */
    public void setLastDamage(double damage);

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @return damage taken since the last no damage ticks time period
     */
    @Deprecated
    public int _INVALID_getLastDamage();

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param damage amount of damage
     */
    @Deprecated
    public void _INVALID_setLastDamage(int damage);

    /**
     * Returns the living entity's current no damage ticks.
     *
     * @return amount of no damage ticks
     */
    public int getNoDamageTicks();

    /**
     * Sets the living entity's current no damage ticks.
     *
     * @param ticks amount of no damage ticks
     */
    public void setNoDamageTicks(int ticks);

    /**
     * Gets the player identified as the killer of the living entity.
     * <p>
     * May be null.
     *
     * @return killer player, or null if none found
     */
    public Player getKiller();

    boolean addPotionEffect(PotionEffect paramPotionEffect);

    boolean addPotionEffect(PotionEffect paramPotionEffect, boolean paramBoolean);

    boolean addPotionEffect(PotionEffect paramPotionEffect, boolean paramBoolean, PotionEffectAddEvent.EffectCause paramEffectCause);

    boolean addPotionEffects(Collection<PotionEffect> paramCollection);

    boolean hasPotionEffect(PotionEffectType paramPotionEffectType);

    void removePotionEffect(PotionEffectType paramPotionEffectType);

    Collection<PotionEffect> getActivePotionEffects();

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p>
     * This uses the same algorithm that hostile mobs use to find the closest
     * player.
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(Entity other);

    /**
     * Returns if the living entity despawns when away from players or not.
     * <p>
     * By default, animals are not removed while other mobs are.
     *
     * @return true if the living entity is removed when away from players
     */
    public boolean getRemoveWhenFarAway();

    /**
     * Sets whether or not the living entity despawns when away from players
     * or not.
     *
     * @param remove the removal status
     */
    public void setRemoveWhenFarAway(boolean remove);

    /**
     * Gets the inventory with the equipment worn by the living entity.
     *
     * @return the living entity's inventory
     */
    public EntityEquipment getEquipment();

    /**
     * Gets if the living entity can pick up items.
     *
     * @return whether or not the living entity can pick up items
     */
    public boolean getCanPickupItems();

    /**
     * Sets whether or not the living entity can pick up items.
     *
     * @param pickup whether or not the living entity can pick up items
     */
    public void setCanPickupItems(boolean pickup);

    /**
     * Returns whether the entity is currently leashed.
     *
     * @return whether the entity is leashed
     */
    public boolean isLeashed();

    /**
     * Gets the entity that is currently leading this entity.
     *
     * @return the entity holding the leash
     * @throws IllegalStateException if not currently leashed
     */
    public Entity getLeashHolder() throws IllegalStateException;

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p>
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
     * Non-living entities excluding leashes will not persist as leash
     * holders.
     *
     * @param holder the entity to leash this entity to
     * @return whether the operation was successful
     */
    public boolean setLeashHolder(Entity holder);

}
