package net.minecraft.server;

import java.util.Random;

public class WorldGenWaterLily extends WorldGenerator {

    public WorldGenWaterLily() {
    }

    public boolean generate(World world, Random random, BlockPosition blockposition) {
        for (int i = 0; i < 10; ++i) {
            int j = blockposition.getX() + random.nextInt(8) - random.nextInt(8);
            int k = blockposition.getY() + random.nextInt(4) - random.nextInt(4);
            int l = blockposition.getZ() + random.nextInt(8) - random.nextInt(8);

            BlockPosition position = new BlockPosition(j, k, l);
            if (world.isEmpty(j, k, l) && Blocks.WATERLILY.canPlace(world, position)) {
                world.setTypeAndData(position, Blocks.WATERLILY.getBlockData(), 2);
            }
        }

        return true;
    }
}
