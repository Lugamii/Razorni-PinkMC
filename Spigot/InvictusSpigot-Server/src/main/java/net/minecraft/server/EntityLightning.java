package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.List;

public class EntityLightning extends EntityWeather {

    public long a;
    // CraftBukkit start
    public boolean isEffect;
    public boolean isSilent = false; // Spigot
    private int lifeTicks;
    private int c;

    public EntityLightning(World world, double d0, double d1, double d2) {
        this(world, d0, d1, d2, false);
    }

    public EntityLightning(World world, double d0, double d1, double d2, boolean isEffect) {
        // CraftBukkit end
        super(world);

        // CraftBukkit - Set isEffect
        this.isEffect = isEffect;

        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.lifeTicks = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;
        BlockPosition blockposition = new BlockPosition(this);

        // CraftBukkit - add "!isEffect"
        if (!isEffect && world.getGameRules().getBoolean("doFireTick") && (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD) && world.areChunksLoaded(blockposition, 10)) {
            if (world.getType(blockposition).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, blockposition)) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                    world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
                }
                // CraftBukkit end
            }

            for (int i = 0; i < 4; ++i) {
                BlockPosition blockposition1 = blockposition.a(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

                if (world.getType(blockposition1).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, blockposition1)) {
                    // CraftBukkit start
                    if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), this).isCancelled()) {
                        world.setTypeUpdate(blockposition1, Blocks.FIRE.getBlockData());
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    // Spigot start
    public EntityLightning(World world, double d0, double d1, double d2, boolean isEffect, boolean isSilent) {
        this(world, d0, d1, d2, isEffect);
        this.isSilent = isSilent;
    }
    // Spigot end

    public void t_() {
        super.t_();
        if (!isSilent && this.lifeTicks == 2) { // Spigot
            // CraftBukkit start - Use relative location for far away sounds
            //this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            float pitch = 0.8F + this.random.nextFloat() * 0.2F;
            int viewDistance = this.world.getServer().getViewDistance() * 16;
            int squaredDistance = viewDistance * viewDistance;
            for (EntityPlayer player : (List<EntityPlayer>) (List) this.world.players) {
                double deltaX = this.locX - player.locX;
                double deltaZ = this.locZ - player.locZ;
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > squaredDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.locX + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.locZ + (deltaZ / deltaLength) * viewDistance;
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", relativeX, this.locY, relativeZ, 10000.0F, pitch));
                } else {
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", this.locX, this.locY, this.locZ, 10000.0F, pitch));
                }
            }
            // CraftBukkit end
            this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            if (this.c == 0) {
                this.die();
            } else if (this.lifeTicks < -this.random.nextInt(10)) {
                --this.c;
                this.lifeTicks = 1;
                this.a = this.random.nextLong();
                BlockPosition blockposition = new BlockPosition(this);

                // CraftBukkit - add "!isEffect"
                if (world.getGameRules().getBoolean("doFireTick") && this.world.areChunksLoaded(blockposition, 10) && this.world.getType(blockposition).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(this.world, blockposition)) {
                    // CraftBukkit start
                    if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                        this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
                    }
                    // CraftBukkit end
                }
            }
        }

        if (this.lifeTicks >= 0 && !this.isEffect) { // CraftBukkit - add !this.isEffect
            double d0 = 3.0D;
            List<Entity> list = this.world.getEntities(this, new AxisAlignedBB(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));

            for (Entity entity : list) {
                entity.onLightningStrike(this);
            }
        }

    }

    protected void h() {
    }

    protected void a(NBTTagCompound nbttagcompound) {
    }

    protected void b(NBTTagCompound nbttagcompound) {
    }
}
