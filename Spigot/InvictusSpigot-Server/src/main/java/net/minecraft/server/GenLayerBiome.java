package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class GenLayerBiome extends GenLayer {

    private BiomeBase[] c;
    private final BiomeBase[] d;
    private final BiomeBase[] e;
    private final BiomeBase[] f;
    private final CustomWorldSettingsFinal g;
    private final World world;

    public GenLayerBiome(long i, GenLayer genlayer, WorldType worldtype, String s, World world) {
        super(i);
        this.world = world;
        List<BiomeBase> list = new ArrayList<>();
        if (world.generatorConfig.biomeDesert) {
            list.add(BiomeBase.DESERT);
            list.add(BiomeBase.DESERT);
            list.add(BiomeBase.DESERT);
        }
        if (world.generatorConfig.biomeSavanna) {
            list.add(BiomeBase.SAVANNA);
            list.add(BiomeBase.SAVANNA);
        }
        if (world.generatorConfig.biomePlains)
            list.add(BiomeBase.PLAINS);
        this.c = list.toArray(new BiomeBase[0]);
        list.clear();
        if (world.generatorConfig.biomeForest)
            list.add(BiomeBase.FOREST);
        if (world.generatorConfig.biomeRoofedForest)
            list.add(BiomeBase.ROOFED_FOREST);
        if (world.generatorConfig.biomeExtremeHills)
            list.add(BiomeBase.EXTREME_HILLS);
        if (world.generatorConfig.biomePlains)
            list.add(BiomeBase.PLAINS);
        if (world.generatorConfig.biomeBirchForest)
            list.add(BiomeBase.BIRCH_FOREST);
        if (world.generatorConfig.biomeSwampland)
            list.add(BiomeBase.SWAMPLAND);
        if (list.isEmpty() && world.generatorConfig.biomeJungle)
            list.add(BiomeBase.JUNGLE);
        this.d = list.toArray(new BiomeBase[0]);
        list.clear();
        if (world.generatorConfig.biomeForest)
            list.add(BiomeBase.FOREST);
        if (world.generatorConfig.biomeExtremeHills)
            list.add(BiomeBase.EXTREME_HILLS);
        if (world.generatorConfig.biomeTaiga)
            list.add(BiomeBase.TAIGA);
        if (world.generatorConfig.biomePlains)
            list.add(BiomeBase.PLAINS);
        this.e = list.toArray(new BiomeBase[0]);
        list.clear();
        if (world.generatorConfig.biomeIcePlains) {
            list.add(BiomeBase.ICE_PLAINS);
            list.add(BiomeBase.ICE_PLAINS);
            list.add(BiomeBase.ICE_PLAINS);
        }
        if (world.generatorConfig.biomeColdTaiga)
            list.add(BiomeBase.COLD_TAIGA);
        this.f = list.toArray(new BiomeBase[0]);
        this.a = genlayer;
        if (worldtype == WorldType.NORMAL_1_1) {
            this.c = new BiomeBase[]{BiomeBase.DESERT, BiomeBase.FOREST, BiomeBase.EXTREME_HILLS, BiomeBase.SWAMPLAND, BiomeBase.PLAINS, BiomeBase.TAIGA};
            this.g = null;
        } else if (worldtype == WorldType.CUSTOMIZED) {
            this.g = CustomWorldSettingsFinal.CustomWorldSettings.a(s).b();
        } else {
            this.g = null;
        }

    }

    public int[] a(int i, int j, int k, int l) {
        int[] aint = this.a.a(i, j, k, l);
        int[] aint1 = IntCache.a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.a(j1 + i, (long) (i1 + j));
                int k1 = aint[j1 + i1 * k];
                int l1 = (k1 & 3840) >> 8;

                k1 &= -3841;
                if (this.g != null && this.g.F >= 0) {
                    aint1[j1 + i1 * k] = this.g.F;
                } else if (b(k1)) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == BiomeBase.MUSHROOM_ISLAND.id) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == 1) {
                    BiomeBase[] biomes = this.firstNonEmpty(this.c, this.d, this.e, this.f);
                    if (l1 > 0) {
                        if (this.a(3) == 0 && this.world.generatorConfig.biomeMesaPlateau) {
                            aint1[j1 + i1 * k] = BiomeBase.MESA_PLATEAU.id;
                        } else if (world.generatorConfig.biomeMesaPlateauF) {
                            aint1[j1 + i1 * k] = BiomeBase.MESA_PLATEAU_F.id;
                        } else if (world.generatorConfig.biomeMesa) {
                            aint1[j1 + i1 * k] = BiomeBase.MESA.id;
                        } else {
                            aint1[j1 + i1 * k] = biomes[this.a(biomes.length)].id;
                        }
                    } else {
                        aint1[j1 + i1 * k] = biomes[this.a(biomes.length)].id;
                    }
                } else if (k1 == 2) {
                    BiomeBase[] biomes = this.firstNonEmpty(this.d, this.e, this.f, this.c);
                    if (l1 > 0 && this.world.generatorConfig.biomeJungle)  {
                        aint1[j1 + i1 * k] = BiomeBase.JUNGLE.id;
                    } else {
                        aint1[j1 + i1 * k] = biomes[this.a(biomes.length)].id;
                    }
                } else if (k1 == 3) {
                    BiomeBase[] biomes = this.firstNonEmpty(this.e, this.f, this.c, this.d);
                    if (l1 > 0 && this.world.generatorConfig.biomeMegaTaiga) {
                        aint1[j1 + i1 * k] = BiomeBase.MEGA_TAIGA.id;
                    } else {
                        aint1[j1 + i1 * k] = biomes[this.a(biomes.length)].id;
                    }
                } else if (k1 == 4) {
                    BiomeBase[] biomes = this.firstNonEmpty(this.f, this.c, this.d, this.e);
                    aint1[j1 + i1 * k] = biomes[this.a(biomes.length)].id;
                } else {
                    aint1[j1 + i1 * k] = BiomeBase.MUSHROOM_ISLAND.id;
                }
            }
        }

        return aint1;
    }


    private BiomeBase[] firstNonEmpty(BiomeBase[]... options) {
        for (BiomeBase[] option : options) {
            if (option.length > 0) {
                return option;
            }
        }
        return new BiomeBase[]{BiomeBase.PLAINS};
    }
}
