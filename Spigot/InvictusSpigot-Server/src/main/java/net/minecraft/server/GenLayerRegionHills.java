package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerRegionHills extends GenLayer {

    private static final Logger c = LogManager.getLogger();
    private final GenLayer d;
    private final World world;

    public GenLayerRegionHills(long i, GenLayer genlayer, GenLayer genlayer1, World world) {
        super(i);
        this.a = genlayer;
        this.d = genlayer1;
        this.world = world;
    }

    public int[] a(int i, int j, int k, int l) {
        int[] aint = this.a.a(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = this.d.a(i - 1, j - 1, k + 2, l + 2);
        int[] aint2 = IntCache.a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.a(j1 + i, (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];
                int l1 = aint1[j1 + 1 + (i1 + 1) * (k + 2)];
                boolean flag = (l1 - 2) % 29 == 0;

                if (k1 > 255) {
                    GenLayerRegionHills.c.debug("old! " + k1);
                }

                if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && k1 < 128) {
                    if (BiomeBase.getBiome(k1 + 128) != null) {
                        aint2[j1 + i1 * k] = k1 + 128;
                    } else {
                        aint2[j1 + i1 * k] = k1;
                    }
                } else if (this.a(3) != 0 && !flag) {
                    aint2[j1 + i1 * k] = k1;
                } else {
                    int i2 = k1;
                    int j2;

                    // mSpigot - generator system
                    if (k1 == BiomeBase.DESERT.id && this.world.generatorConfig.biomeDesertHills) {
                        i2 = BiomeBase.DESERT_HILLS.id;
                    } else if (k1 == BiomeBase.FOREST.id && this.world.generatorConfig.biomeForestHills) {
                        i2 = BiomeBase.FOREST_HILLS.id;
                    } else if (k1 == BiomeBase.BIRCH_FOREST.id && this.world.generatorConfig.biomeBirchForestHills) {
                        i2 = BiomeBase.BIRCH_FOREST_HILLS.id;
                    } else if (k1 == BiomeBase.ROOFED_FOREST.id && this.world.generatorConfig.biomePlains) {
                        i2 = BiomeBase.PLAINS.id;
                    } else if (k1 == BiomeBase.TAIGA.id && this.world.generatorConfig.biomeTaigaHills) {
                        i2 = BiomeBase.TAIGA_HILLS.id;
                    } else if (k1 == BiomeBase.MEGA_TAIGA.id && this.world.generatorConfig.biomeMegaTaigaHills) {
                        i2 = BiomeBase.MEGA_TAIGA_HILLS.id;
                    } else if (k1 == BiomeBase.COLD_TAIGA.id && this.world.generatorConfig.biomeColdTaigaHills) {
                        i2 = BiomeBase.COLD_TAIGA_HILLS.id;
                    } else if (k1 == BiomeBase.PLAINS.id) {
                        if (this.a(3) == 0 && this.world.generatorConfig.biomeForestHills) {
                            i2 = BiomeBase.FOREST_HILLS.id;
                        } else if (this.world.generatorConfig.biomeForest) {
                            i2 = BiomeBase.FOREST.id;
                        }
                    } else if (k1 == BiomeBase.ICE_PLAINS.id && this.world.generatorConfig.biomeIceMountains) {
                        i2 = BiomeBase.ICE_MOUNTAINS.id;
                    } else if (k1 == BiomeBase.JUNGLE.id && this.world.generatorConfig.biomeJungleHills) {
                        i2 = BiomeBase.JUNGLE_HILLS.id;
                    } else if (k1 == BiomeBase.OCEAN.id) {
                        i2 = BiomeBase.DEEP_OCEAN.id;
                    } else if (k1 == BiomeBase.EXTREME_HILLS.id && this.world.generatorConfig.biomeExtremeHillsPlus) {
                        i2 = BiomeBase.EXTREME_HILLS_PLUS.id;
                    } else if (k1 == BiomeBase.SAVANNA.id && this.world.generatorConfig.biomeSavannaPlateau) {
                        i2 = BiomeBase.SAVANNA_PLATEAU.id;
                    } else if (a(k1, BiomeBase.MESA_PLATEAU_F.id) && this.world.generatorConfig.biomeMesa) {
                        i2 = BiomeBase.MESA.id;
                    } else if (k1 == BiomeBase.DEEP_OCEAN.id && this.a(3) == 0) {
                        j2 = this.a(2);
                        if (j2 == 0 && this.world.generatorConfig.biomePlains) {
                            i2 = BiomeBase.PLAINS.id;
                        } else if (this.world.generatorConfig.biomeForest) {
                            i2 = BiomeBase.FOREST.id;
                        }
                    }

                    if (flag && i2 != k1) {
                        if (BiomeBase.getBiome(i2 + 128) != null) {
                            i2 += 128;
                        } else {
                            i2 = k1;
                        }
                    }

                    if (i2 == k1) {
                        aint2[j1 + i1 * k] = k1;
                    } else {
                        j2 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        int k2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        int l2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        int i3 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        int j3 = 0;

                        if (a(j2, k1)) {
                            ++j3;
                        }

                        if (a(k2, k1)) {
                            ++j3;
                        }

                        if (a(l2, k1)) {
                            ++j3;
                        }

                        if (a(i3, k1)) {
                            ++j3;
                        }

                        if (j3 >= 3) {
                            aint2[j1 + i1 * k] = i2;
                        } else {
                            aint2[j1 + i1 * k] = k1;
                        }
                    }
                }
            }
        }

        return aint2;
    }
}
