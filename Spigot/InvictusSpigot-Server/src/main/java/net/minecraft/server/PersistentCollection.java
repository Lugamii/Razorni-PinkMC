package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.vortexdev.invictusspigot.InvictusSpigot;

import java.io.*;
import java.util.List;
import java.util.Map;

public class PersistentCollection {

    private final IDataManager b;
    protected Map<String, PersistentBase> a = Maps.newHashMap();
    public List<PersistentBase> c = Lists.newArrayList(); // Spigot
    private final Map<String, Short> d = Maps.newHashMap();

    public PersistentCollection(IDataManager idatamanager) {
        this.b = idatamanager;
        this.b();
    }

    public PersistentBase get(Class<? extends PersistentBase> oclass, String s) {
        PersistentBase persistentbase = this.a.get(s);

        if (persistentbase == null) {
            if (this.b != null) {
                try {
                    File file = this.b.getDataFile(s);

                    if (file != null && file.exists()) {
                        try {
                            persistentbase = oclass.getConstructor(String.class).newInstance(s);
                        } catch (Exception exception) {
                            throw new RuntimeException("Failed to instantiate " + oclass.toString(), exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file);
                        NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a(fileinputstream);

                        fileinputstream.close();
                        persistentbase.a(nbttagcompound.getCompound("data"));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }

            if (persistentbase != null) {
                this.a.put(s, persistentbase);
                this.c.add(persistentbase);
            }

        }
        return persistentbase;
    }

    public void a(String s, PersistentBase persistentbase) {
        if (this.a.containsKey(s)) {
            this.c.remove(this.a.remove(s));
        }

        this.a.put(s, persistentbase);
        this.c.add(persistentbase);
    }

    public void a() {
        for (PersistentBase persistentbase : this.c) {
            if (persistentbase != null && persistentbase.d()) {
                this.a(persistentbase);
                persistentbase.a(false);
            }
        }

    }

    private void a(PersistentBase persistentbase) {
        if (this.b != null) {
            try {
                File file = this.b.getDataFile(persistentbase.id);

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    persistentbase.b(nbttagcompound);
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.set("data", nbttagcompound);

                    InvictusSpigot.INSTANCE.getThreadingManager().saveNBTFile((NBTTagCompound) (nbttagcompound1.clone()), file);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    private void b() {
        try {
            this.d.clear();
            if (this.b == null) {
                return;
            }

            File file = this.b.getDataFile("idcounts");

            if (file != null && file.exists()) {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a(datainputstream);

                datainputstream.close();

                for (String s : nbttagcompound.c()) {
                    NBTBase nbtbase = nbttagcompound.get(s);

                    if (nbtbase instanceof NBTTagShort) {
                        NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
                        short short0 = nbttagshort.e();

                        this.d.put(s, short0);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public int a(String s) {
        Short oshort = this.d.get(s);

        if (oshort == null) {
            oshort = (short) 0;
        } else {
            oshort = (short) (oshort + 1);
        }

        this.d.put(s, oshort);
        if (this.b != null) {
            try {
                File file = this.b.getDataFile("idcounts");

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    for (String s1 : this.d.keySet()) {
                        short short0 = this.d.get(s1);

                        nbttagcompound.setShort(s1, short0);
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

                    NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
                    dataoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        return oshort;
    }

}
