package net.minecraft.server;

import java.util.Random;

public class WorldGenClay extends WorldGenerator {

    private final Block a;
    private final int b;

    public WorldGenClay(int i) {
        this.a = Blocks.CLAY;
        this.b = i;
    }

    public boolean generate(World world, Random random, BlockPosition blockposition) {
        if (world.getType(blockposition).getBlock().getMaterial() != Material.WATER) {
            return false;
        } else {
            int i = random.nextInt(this.b - 2) + 2;
            byte b0 = 1;

            for (int j = blockposition.getX() - i; j <= blockposition.getX() + i; ++j) {
                for (int k = blockposition.getZ() - i; k <= blockposition.getZ() + i; ++k) {
                    int l = j - blockposition.getX();
                    int i1 = k - blockposition.getZ();

                    if (l * l + i1 * i1 <= i * i) {
                        for (int j1 = blockposition.getY() - b0; j1 <= blockposition.getY() + b0; ++j1) {
                            Block block = world.getType(j, j1, k).getBlock();

                            if (block == Blocks.DIRT || block == Blocks.CLAY) {
                                world.setTypeAndData(new BlockPosition(j, j1, k), this.a.getBlockData(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
