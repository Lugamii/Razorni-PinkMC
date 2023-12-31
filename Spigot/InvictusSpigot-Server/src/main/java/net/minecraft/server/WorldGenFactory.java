package net.minecraft.server;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WorldGenFactory {

    private static final Logger a = LogManager.getLogger();
    private static final Map<String, Class<? extends StructureStart>> b = Maps.newHashMap();
    private static final Map<Class<? extends StructureStart>, String> c = Maps.newHashMap();
    private static final Map<String, Class<? extends StructurePiece>> d = Maps.newHashMap();
    private static final Map<Class<? extends StructurePiece>, String> e = Maps.newHashMap();

    private static void b(Class<? extends StructureStart> oclass, String s) {
        WorldGenFactory.b.put(s, oclass);
        WorldGenFactory.c.put(oclass, s);
    }

    static void a(Class<? extends StructurePiece> oclass, String s) {
        WorldGenFactory.d.put(s, oclass);
        WorldGenFactory.e.put(oclass, s);
    }

    public static String a(StructureStart structurestart) {
        return WorldGenFactory.c.get(structurestart.getClass());
    }

    public static String a(StructurePiece structurepiece) {
        return WorldGenFactory.e.get(structurepiece.getClass());
    }

    public static StructureStart a(NBTTagCompound nbttagcompound, World world) {
        StructureStart structurestart = null;

        try {
            Class<? extends StructureStart> oclass = WorldGenFactory.b.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                structurestart = oclass.newInstance();
            }
        } catch (Exception exception) {
            WorldGenFactory.a.warn("Failed Start with id " + nbttagcompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurestart != null) {
            structurestart.a(world, nbttagcompound);
        } else {
            WorldGenFactory.a.warn("Skipping Structure with id " + nbttagcompound.getString("id"));
        }

        return structurestart;
    }

    public static StructurePiece b(NBTTagCompound nbttagcompound, World world) {
        StructurePiece structurepiece = null;

        try {
            Class<? extends StructurePiece> oclass = WorldGenFactory.d.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                structurepiece = oclass.newInstance();
            }
        } catch (Exception exception) {
            WorldGenFactory.a.warn("Failed Piece with id " + nbttagcompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurepiece != null) {
            structurepiece.a(world, nbttagcompound);
        } else {
            WorldGenFactory.a.warn("Skipping Piece with id " + nbttagcompound.getString("id"));
        }

        return structurepiece;
    }

    static {
        b(WorldGenMineshaftStart.class, "Mineshaft");
        b(WorldGenVillage.WorldGenVillageStart.class, "Village");
        b(WorldGenNether.WorldGenNetherStart.class, "Fortress");
        b(WorldGenStronghold.WorldGenStronghold2Start.class, "Stronghold");
        b(WorldGenLargeFeature.WorldGenLargeFeatureStart.class, "Temple");
        b(WorldGenMonument.WorldGenMonumentStart.class, "Monument");
        WorldGenMineshaftPieces.a();
        WorldGenVillagePieces.a();
        WorldGenNetherPieces.a();
        WorldGenStrongholdPieces.a();
        WorldGenRegistration.a();
        WorldGenMonumentPieces.a();
    }
}
