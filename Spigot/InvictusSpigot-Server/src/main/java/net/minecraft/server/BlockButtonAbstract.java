package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import java.util.List;
import java.util.Random;
// CraftBukkit end

public abstract class BlockButtonAbstract extends Block {

    public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    private final boolean N;

    protected BlockButtonAbstract(boolean flag) {
        super(Material.ORIENTABLE);
        this.j(this.blockStateList.getBlockData().set(BlockButtonAbstract.FACING, EnumDirection.NORTH).set(BlockButtonAbstract.POWERED, Boolean.FALSE));
        this.a(true);
        this.a(CreativeModeTab.d);
        this.N = flag;
    }

    protected static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition blockposition1 = blockposition.shift(enumdirection);

        return enumdirection == EnumDirection.DOWN ? World.a(world, blockposition1) : world.getType(blockposition1).getBlock().isOccluding();
    }

    public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return null;
    }

    public int a(World world) {
        return this.N ? 30 : 20;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return a(world, blockposition, enumdirection.opposite());
    }

    public boolean canPlace(World world, BlockPosition blockposition) {
        for (EnumDirection enumdirection : EnumDirection.values()) {
            if (a(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        return a(world, blockposition, enumdirection.opposite()) ? this.getBlockData().set(BlockButtonAbstract.FACING, enumdirection).set(BlockButtonAbstract.POWERED, Boolean.FALSE) : this.getBlockData().set(BlockButtonAbstract.FACING, EnumDirection.DOWN).set(BlockButtonAbstract.POWERED, Boolean.FALSE);
    }

    public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        if (this.e(world, blockposition, iblockdata) && !a(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING).opposite())) {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
        }

    }

    private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (this.canPlace(world, blockposition)) {
            return true;
        } else {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
            return false;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
        this.d(iblockaccess.getType(blockposition));
    }

    private void d(IBlockData iblockdata) {
        EnumDirection enumdirection = iblockdata.get(BlockButtonAbstract.FACING);
        boolean flag = iblockdata.get(BlockButtonAbstract.POWERED);

        float f2 = (float) (flag ? 1 : 2) / 16.0F;

        switch (BlockButtonAbstract.SyntheticClass_1.a[enumdirection.ordinal()]) {
            case 1:
                this.a(0.0F, 0.375F, 0.3125F, f2, 0.625F, 0.6875F);
                break;

            case 2:
                this.a(1.0F - f2, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
                break;

            case 3:
                this.a(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, f2);
                break;

            case 4:
                this.a(0.3125F, 0.375F, 1.0F - f2, 0.6875F, 0.625F, 1.0F);
                break;

            case 5:
                this.a(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + f2, 0.625F);
                break;

            case 6:
                this.a(0.3125F, 1.0F - f2, 0.375F, 0.6875F, 1.0F, 0.625F);
        }

    }

    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (!iblockdata.get(BlockButtonAbstract.POWERED)) {
            // CraftBukkit start
            boolean powered = iblockdata.get(POWERED);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) == (powered)) {
                return true;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, true), 3);
            world.b(blockposition, blockposition);
            world.makeSound((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.a(blockposition, this, this.a(world));
        }
        return true;
    }

    public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (iblockdata.get(BlockButtonAbstract.POWERED)) {
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
        }

        super.remove(world, blockposition, iblockdata);
    }

    public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return iblockdata.get(BlockButtonAbstract.POWERED) ? 15 : 0;
    }

    public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return !iblockdata.get(BlockButtonAbstract.POWERED) ? 0 : (iblockdata.get(BlockButtonAbstract.FACING) == enumdirection ? 15 : 0);
    }

    public boolean isPowerSource() {
        return true;
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
    }

    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        if (iblockdata.get(BlockButtonAbstract.POWERED)) {
            if (this.N) {
                this.f(world, blockposition, iblockdata);
            } else {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, false));
                this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
                world.makeSound((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
                world.b(blockposition, blockposition);
            }

        }
    }

    public void j() {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity) {
        if (this.N) {
            if (!iblockdata.get(BlockButtonAbstract.POWERED)) {
                this.f(world, blockposition, iblockdata);
            }
        }
    }

    private void f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        this.d(iblockdata);
        List<Entity> list = world.a(EntityArrow.class, new AxisAlignedBB((double) blockposition.getX() + this.minX, (double) blockposition.getY() + this.minY, (double) blockposition.getZ() + this.minZ, (double) blockposition.getX() + this.maxX, (double) blockposition.getY() + this.maxY, (double) blockposition.getZ() + this.maxZ));
        boolean flag = !list.isEmpty();
        boolean flag1 = iblockdata.get(BlockButtonAbstract.POWERED);

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag1 != flag && flag) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Entity entity : list) {
                EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(), block);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag && !flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 0, 15);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() <= 0) {
                return;
            }
            // CraftBukkit end
            world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, true));
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.b(blockposition, blockposition);
            world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag && flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() > 0) {
                return;
            }
            // CraftBukkit end
            world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, false));
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.b(blockposition, blockposition);
            world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag) {
            world.a(blockposition, this, this.a(world));
        }

    }

    private void c(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        world.applyPhysics(blockposition, this);
        world.applyPhysics(blockposition.shift(enumdirection.opposite()), this);
    }

    public IBlockData fromLegacyData(int i) {
        EnumDirection enumdirection;

        switch (i & 7) {
            case 0:
                enumdirection = EnumDirection.DOWN;
                break;

            case 1:
                enumdirection = EnumDirection.EAST;
                break;

            case 2:
                enumdirection = EnumDirection.WEST;
                break;

            case 3:
                enumdirection = EnumDirection.SOUTH;
                break;

            case 4:
                enumdirection = EnumDirection.NORTH;
                break;

            case 5:
            default:
                enumdirection = EnumDirection.UP;
        }

        return this.getBlockData().set(BlockButtonAbstract.FACING, enumdirection).set(BlockButtonAbstract.POWERED, (i & 8) > 0);
    }

    public int toLegacyData(IBlockData iblockdata) {
        int i;

        switch (BlockButtonAbstract.SyntheticClass_1.a[iblockdata.get(BlockButtonAbstract.FACING).ordinal()]) {
            case 1:
                i = 1;
                break;

            case 2:
                i = 2;
                break;

            case 3:
                i = 3;
                break;

            case 4:
                i = 4;
                break;

            case 5:
            default:
                i = 5;
                break;

            case 6:
                i = 0;
        }

        if (iblockdata.get(BlockButtonAbstract.POWERED)) {
            i |= 8;
        }

        return i;
    }

    public BlockStateList getStateList() {
        return new BlockStateList(this, BlockButtonAbstract.FACING, BlockButtonAbstract.POWERED);
    }

    static class SyntheticClass_1 {

        static final int[] a = new int[EnumDirection.values().length];

        static {
            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.EAST.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.WEST.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.NORTH.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.UP.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                BlockButtonAbstract.SyntheticClass_1.a[EnumDirection.DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

        }
    }
}
