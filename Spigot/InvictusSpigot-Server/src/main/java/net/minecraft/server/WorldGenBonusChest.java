package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class WorldGenBonusChest extends WorldGenerator {

    private final List<StructurePieceTreasure> a;
    private final int b;

    public WorldGenBonusChest(List<StructurePieceTreasure> list, int i) {
        this.a = list;
        this.b = i;
    }

    public boolean generate(World world, Random random, BlockPosition blockposition) {
        Block block;

        while (((block = world.getType(blockposition).getBlock()).getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) && blockposition.getY() > 1) {
            blockposition = blockposition.down();
        }

        if (blockposition.getY() >= 1) {
            blockposition = blockposition.up();

            for (int i = 0; i < 4; ++i) {
                BlockPosition blockposition1 = blockposition.a(random.nextInt(4) - random.nextInt(4), random.nextInt(3) - random.nextInt(3), random.nextInt(4) - random.nextInt(4));

                if (world.isEmpty(blockposition1) && World.a(world, blockposition1.getX(), blockposition1.getY() - 1, blockposition1.getZ())) {
                    world.setTypeAndData(blockposition1, Blocks.CHEST.getBlockData(), 2);
                    TileEntity tileentity = world.getTileEntity(blockposition1);

                    if (tileentity instanceof TileEntityChest) {
                        StructurePieceTreasure.a(random, this.a, (TileEntityChest) tileentity, this.b);
                    }

                    BlockPosition blockposition2 = blockposition1.east();
                    BlockPosition blockposition3 = blockposition1.west();
                    BlockPosition blockposition4 = blockposition1.north();
                    BlockPosition blockposition5 = blockposition1.south();

                    if (world.isEmpty(blockposition3) && World.a(world, blockposition3.getX(), blockposition3.getY() - 1, blockposition3.getZ())) {
                        world.setTypeAndData(blockposition3, Blocks.TORCH.getBlockData(), 2);
                    }

                    if (world.isEmpty(blockposition2) && World.a(world, blockposition2.getX(), blockposition2.getY() - 1, blockposition2.getZ())) {
                        world.setTypeAndData(blockposition2, Blocks.TORCH.getBlockData(), 2);
                    }

                    if (world.isEmpty(blockposition4) && World.a(world, blockposition4.getX(), blockposition4.getY() - 1, blockposition4.getZ())) {
                        world.setTypeAndData(blockposition4, Blocks.TORCH.getBlockData(), 2);
                    }

                    if (world.isEmpty(blockposition5) && World.a(world, blockposition5.getX(), blockposition5.getY() - 1, blockposition5.getZ())) {
                        world.setTypeAndData(blockposition5, Blocks.TORCH.getBlockData(), 2);
                    }

                    return true;
                }
            }

        }
        return false;
    }
}
