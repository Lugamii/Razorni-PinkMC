package net.minecraft.server;

import com.google.common.collect.Maps;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import java.util.Map;
import java.util.Random;
// CraftBukkit end

public class BlockFire extends Block {

    public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);
    public static final BlockStateBoolean FLIP = BlockStateBoolean.of("flip");
    public static final BlockStateBoolean ALT = BlockStateBoolean.of("alt");
    public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
    public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
    public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
    public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
    public static final BlockStateInteger UPPER = BlockStateInteger.of("upper", 0, 2);
    private final Map<Block, Integer> flameChances = Maps.newIdentityHashMap();
    private final Map<Block, Integer> U = Maps.newIdentityHashMap();

    public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();

        if (!World.a(iblockaccess, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) && !e(iblockaccess, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ())) {
            boolean flag = (i + j + k & 1) == 1;
            boolean flag1 = (i / 2 + j / 2 + k / 2 & 1) == 1;
            int l = 0;

            if (this.e(iblockaccess, blockposition.getX(), blockposition.getY() + 1, blockposition.getZ())) {
                l = flag ? 1 : 2;
            }

            return iblockdata.set(BlockFire.NORTH, this.e(iblockaccess, blockposition.getX(), blockposition.getY(), blockposition.getZ() - 1)).set(BlockFire.EAST, this.e(iblockaccess, blockposition.getX() + 1, blockposition.getY(), blockposition.getZ())).set(BlockFire.SOUTH, this.e(iblockaccess, blockposition.getX(), blockposition.getY(), blockposition.getZ() + 1)).set(BlockFire.WEST, this.e(iblockaccess, blockposition.getX() - 1, blockposition.getY(), blockposition.getZ())).set(BlockFire.UPPER, l).set(BlockFire.FLIP, flag1).set(BlockFire.ALT, flag);
        } else {
            return this.getBlockData();
        }
    }

    protected BlockFire() {
        super(Material.FIRE);
        this.j(this.blockStateList.getBlockData().set(BlockFire.AGE, 0).set(BlockFire.FLIP, Boolean.FALSE).set(BlockFire.ALT, Boolean.FALSE).set(BlockFire.NORTH, Boolean.FALSE).set(BlockFire.EAST, Boolean.FALSE).set(BlockFire.SOUTH, Boolean.FALSE).set(BlockFire.WEST, Boolean.FALSE).set(BlockFire.UPPER, 0));
        this.a(true);
    }

    public static void l() {
        Blocks.FIRE.a(Blocks.PLANKS, 5, 20);
        Blocks.FIRE.a(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
        Blocks.FIRE.a(Blocks.WOODEN_SLAB, 5, 20);
        Blocks.FIRE.a(Blocks.FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.BIRCH_FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.ACACIA_FENCE_GATE, 5, 20);
        Blocks.FIRE.a(Blocks.FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.SPRUCE_FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.BIRCH_FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.JUNGLE_FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.DARK_OAK_FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.ACACIA_FENCE, 5, 20);
        Blocks.FIRE.a(Blocks.OAK_STAIRS, 5, 20);
        Blocks.FIRE.a(Blocks.BIRCH_STAIRS, 5, 20);
        Blocks.FIRE.a(Blocks.SPRUCE_STAIRS, 5, 20);
        Blocks.FIRE.a(Blocks.JUNGLE_STAIRS, 5, 20);
        Blocks.FIRE.a(Blocks.LOG, 5, 5);
        Blocks.FIRE.a(Blocks.LOG2, 5, 5);
        Blocks.FIRE.a(Blocks.LEAVES, 30, 60);
        Blocks.FIRE.a(Blocks.LEAVES2, 30, 60);
        Blocks.FIRE.a(Blocks.BOOKSHELF, 30, 20);
        Blocks.FIRE.a(Blocks.TNT, 15, 100);
        Blocks.FIRE.a(Blocks.TALLGRASS, 60, 100);
        Blocks.FIRE.a(Blocks.DOUBLE_PLANT, 60, 100);
        Blocks.FIRE.a(Blocks.YELLOW_FLOWER, 60, 100);
        Blocks.FIRE.a(Blocks.RED_FLOWER, 60, 100);
        Blocks.FIRE.a(Blocks.DEADBUSH, 60, 100);
        Blocks.FIRE.a(Blocks.WOOL, 30, 60);
        Blocks.FIRE.a(Blocks.VINE, 15, 100);
        Blocks.FIRE.a(Blocks.COAL_BLOCK, 5, 5);
        Blocks.FIRE.a(Blocks.HAY_BLOCK, 60, 20);
        Blocks.FIRE.a(Blocks.CARPET, 60, 20);
    }

    public void a(Block block, int i, int j) {
        this.flameChances.put(block, i);
        this.U.put(block, j);
    }

    public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public int a(Random random) {
        return 0;
    }

    public int a(World world) {
        return 30;
    }

    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        if (world.getGameRules().getBoolean("doFireTick")) {
            if (!this.canPlace(world, blockposition)) {
                fireExtinguished(world, blockposition); // CraftBukkit - invalid place location
            }

            Block block = world.getType(blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()).getBlock();
            boolean flag = block == Blocks.NETHERRACK;

            if (world.worldProvider instanceof WorldProviderTheEnd && block == Blocks.BEDROCK) {
                flag = true;
            }

            if (!flag && world.S() && this.e(world, blockposition)) {
                fireExtinguished(world, blockposition); // CraftBukkit - extinguished by rain
            } else {
                int i = iblockdata.get(BlockFire.AGE);

                if (i < 15) {
                    iblockdata = iblockdata.set(BlockFire.AGE, i + random.nextInt(3) / 2);
                    world.setTypeAndData(blockposition, iblockdata, 4);
                }

                world.a(blockposition, this, this.a(world) + random.nextInt(10));
                if (!flag) {
                    if (!this.f(world, blockposition)) {
                        if (!World.a(world, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) || i > 3) {
                            fireExtinguished(world, blockposition); // CraftBukkit
                        }

                        return;
                    }

                    if (!this.e(world, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) && i == 15 && random.nextInt(4) == 0) {
                        fireExtinguished(world, blockposition); // CraftBukkit
                        return;
                    }
                }

                boolean flag1 = world.D(blockposition);
                byte b0 = 0;

                if (flag1) {
                    b0 = -50;
                }

                this.a(world, blockposition.east(), 300 + b0, random, i);
                this.a(world, blockposition.west(), 300 + b0, random, i);
                this.a(world, blockposition.down(), 250 + b0, random, i);
                this.a(world, blockposition.up(), 250 + b0, random, i);
                this.a(world, blockposition.north(), 300 + b0, random, i);
                this.a(world, blockposition.south(), 300 + b0, random, i);

                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        for (int l = -1; l <= 4; ++l) {
                            if (j != 0 || l != 0 || k != 0) {
                                int i1 = 100;

                                if (l > 1) {
                                    i1 += (l - 1) * 100;
                                }

                                BlockPosition blockposition1 = blockposition.a(j, l, k);
                                int j1 = this.m(world, blockposition1);

                                if (j1 > 0) {
                                    int k1 = (j1 + 40 + world.getDifficulty().a() * 7) / (i + 30);

                                    if (flag1) {
                                        k1 /= 2;
                                    }

                                    if (k1 > 0 && random.nextInt(i1) <= k1 && (!world.S() || !this.e(world, blockposition1))) {
                                        int l1 = i + random.nextInt(5) / 4;

                                        if (l1 > 15) {
                                            l1 = 15;
                                        }

                                        // CraftBukkit start - Call to stop spread of fire
                                        if (world.getType(blockposition1).getBlock() != Blocks.FIRE) {
                                            if (CraftEventFactory.callBlockIgniteEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), blockposition.getX(), blockposition.getY(), blockposition.getZ()).isCancelled()) {
                                                continue;
                                            }

                                            org.bukkit.Server server = world.getServer();
                                            org.bukkit.World bworld = world.getWorld();
                                            org.bukkit.block.BlockState blockState = bworld.getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()).getState();
                                            blockState.setTypeId(Block.getId(this));
                                            blockState.setData(new org.bukkit.material.MaterialData(Block.getId(this), (byte) l1));

                                            BlockSpreadEvent spreadEvent = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), blockState);
                                            server.getPluginManager().callEvent(spreadEvent);

                                            if (!spreadEvent.isCancelled()) {
                                                blockState.update(true);
                                            }
                                        }
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    protected boolean e(World world, BlockPosition blockposition) {
        int x = blockposition.getX(), y = blockposition.getY(), z = blockposition.getZ();
        return world.isRainingAt(x, y, z) ||
                world.isRainingAt(x - 1, y, z) ||
                world.isRainingAt(x + 1, y, z) ||
                world.isRainingAt(x, y, z - 1) ||
                world.isRainingAt(x, y, z + 1);
    }

    public boolean N() {
        return false;
    }

    private int c(Block block) {
        Integer integer = this.U.get(block);

        return integer == null ? 0 : integer;
    }

    private int d(Block block) {
        Integer integer = this.flameChances.get(block);

        return integer == null ? 0 : integer;
    }

    private void a(World world, BlockPosition blockposition, int i, Random random, int j) {
        int k = this.c(world.getType(blockposition).getBlock());

        if (random.nextInt(i) < k) {
            IBlockData iblockdata = world.getType(blockposition);

            // CraftBukkit start
            org.bukkit.block.Block theBlock = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockBurnEvent event = new BlockBurnEvent(theBlock);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(j + 10) < 5 && !world.isRainingAt(blockposition)) {
                int l = j + random.nextInt(5) / 4;

                if (l > 15) {
                    l = 15;
                }

                world.setTypeAndData(blockposition, this.getBlockData().set(BlockFire.AGE, l), 3);
            } else {
                fireExtinguished(world, blockposition); // CraftBukkit
            }

            if (iblockdata.getBlock() == Blocks.TNT) {
                Blocks.TNT.postBreak(world, blockposition, iblockdata.set(BlockTNT.EXPLODE, Boolean.TRUE));
            }
        }

    }

    private boolean f(World world, BlockPosition blockposition) {
        for (EnumDirection enumdirection : EnumDirection.values()) {
            if (this.e(world, blockposition.getX() + enumdirection.getAdjacentX(), blockposition.getY() + enumdirection.getAdjacentY(), blockposition.getZ() + enumdirection.getAdjacentZ())) {
                return true;
            }
        }

        return false;
    }

    private int m(World world, BlockPosition blockposition) {
        if (!world.isEmpty(blockposition)) {
            return 0;
        } else {
            int i = 0;

            for (EnumDirection enumdirection : EnumDirection.values()) {
                i = Math.max(this.d(world.getType(blockposition.shift(enumdirection)).getBlock()), i);
            }

            return i;
        }
    }

    public boolean A() {
        return false;
    }

    public boolean e(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return e(iblockaccess, blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    public boolean e(IBlockAccess iblockaccess, int x, int y, int z) {
        return this.d(iblockaccess.getType(x, y, z).getBlock()) > 0;
    }

    public boolean canPlace(World world, BlockPosition blockposition) {
        return World.a(world, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) || this.f(world, blockposition);
    }

    public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        if (!World.a(world, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) && !this.f(world, blockposition)) {
            fireExtinguished(world, blockposition); // CraftBukkit - fuel block gone
        }

    }

    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (world.worldProvider.getDimension() > 0 || !Blocks.PORTAL.e(world, blockposition)) {
            if (!World.a(world, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ()) && !this.f(world, blockposition)) {
                fireExtinguished(world, blockposition); // CraftBukkit - fuel block broke
            } else {
                world.a(blockposition, this, this.a(world) + world.random.nextInt(10));
            }
        }
    }

    public MaterialMapColor g(IBlockData iblockdata) {
        return MaterialMapColor.f;
    }

    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockFire.AGE, i);
    }

    public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockFire.AGE);
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockFire.AGE, BlockFire.NORTH, BlockFire.EAST, BlockFire.SOUTH, BlockFire.WEST, BlockFire.UPPER, BlockFire.FLIP, BlockFire.ALT);
    }

    // CraftBukkit start
    private void fireExtinguished(World world, BlockPosition position) {
        if (!CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), Blocks.AIR).isCancelled()) {
            world.setAir(position);
        }
    }
    // CraftBukkit end
}
