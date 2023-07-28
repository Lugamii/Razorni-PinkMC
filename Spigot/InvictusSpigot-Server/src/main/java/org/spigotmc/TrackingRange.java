package org.spigotmc;

import net.minecraft.server.*;

public class TrackingRange
{

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange)
    {
        SpigotWorldConfig config = entity.world.spigotConfig;
        if ( entity instanceof EntityPlayer )
        {
            return config.playerTrackingRange;
        }  else if ( entity.activationType == 1 )
        {
            return config.monsterTrackingRange;
        } else if ( entity instanceof EntityGhast )
        {
            if ( config.monsterTrackingRange > config.monsterActivationRange )
            {
                return config.monsterTrackingRange;
            } else
            {
                return config.monsterActivationRange;
            }
        } else if ( entity.activationType == 2 )
        {
            return config.animalTrackingRange;
        } else if ( entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityItem || entity instanceof EntityExperienceOrb )
        {
            return config.miscTrackingRange;
        } else 
        {
            return config.otherTrackingRange;
        }
    }
    
    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @return
     */
    public static int getEntityTrackingRange(Entity entity) {
        SpigotWorldConfig config = entity.world.spigotConfig;
        if (entity instanceof EntityPlayer) {
            return config.playerTrackingRange;
        } else if (entity.activationType == 1) {
            return config.monsterTrackingRange;
        } else if (entity instanceof EntityGhast) {
            if (config.monsterTrackingRange > config.monsterActivationRange) {
                return config.monsterTrackingRange;
            } else {
                return config.monsterActivationRange;
            }
        } else if (entity.activationType == 2 ) {
            return config.animalTrackingRange;
        } else if (entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityItem || entity instanceof EntityExperienceOrb) {
            return config.miscTrackingRange;
        } else  {
            return config.otherTrackingRange;
        }
    }
}
