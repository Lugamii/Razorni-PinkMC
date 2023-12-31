package net.minecraft.server;

import org.bukkit.event.hanging.HangingPlaceEvent;

import java.util.List;

public class ItemLeash extends Item {

    public ItemLeash() {
        this.a(CreativeModeTab.i);
    }

    public static boolean a(EntityHuman entityhuman, World world, BlockPosition blockposition) {
        EntityLeash entityleash = EntityLeash.b(world, blockposition);
        boolean flag = false;
        double d0 = 7.0D;
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        List<EntityInsentient> list = world.a(EntityInsentient.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));

        for (EntityInsentient entityinsentient : list) {
            if (entityinsentient.cc() && entityinsentient.getLeashHolder() == entityhuman) {
                if (entityleash == null) {
                    entityleash = EntityLeash.a(world, blockposition);

                    // CraftBukkit start - fire HangingPlaceEvent
                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityleash.getBukkitEntity(), entityhuman != null ? (org.bukkit.entity.Player) entityhuman.getBukkitEntity() : null, world.getWorld().getBlockAt(i, j, k), org.bukkit.block.BlockFace.SELF);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        entityleash.die();
                        return false;
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, entityleash, entityhuman).isCancelled()) {
                    continue;
                }
                // CraftBukkit end

                entityinsentient.setLeashHolder(entityleash, true);
                flag = true;
            }
        }

        return flag;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2) {
        if (world.getType(blockposition).getBlock() instanceof BlockFence) {
            a(entityhuman, world, blockposition);
            return true;
        } else {
            return false;
        }
    }
}
