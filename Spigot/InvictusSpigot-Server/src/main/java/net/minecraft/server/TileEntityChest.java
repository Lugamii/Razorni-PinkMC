package net.minecraft.server;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import java.util.Arrays;
import java.util.List;
// CraftBukkit end

public class TileEntityChest extends TileEntityContainer implements IInventory { // PaperSpigot - remove IUpdatePlayerListBox

    private ItemStack[] items = new ItemStack[27];
    public boolean a;
    public TileEntityChest f; // PaperSpigot - adjacentChestZNeg
    public TileEntityChest g; // PaperSpigot - adjacentChestXPos
    public TileEntityChest h; // PaperSpigot - adjacentChestXNeg
    public TileEntityChest i; // PaperSpigot - adjacentChestZPos
    public float j; // PaperSpigot - lidAngle
    public float k;
    public int l; // PaperSpigot - numPlayersUsing

    private int o = -1;
    private String p;

    public TileEntityChest() {}

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public int getSize() {
        return 27;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
            } else {
                itemstack = this.items[i].cloneAndSubtract(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

            }
            this.update();
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public String getName() {
        return this.hasCustomName() ? this.p : "container.chest";
    }

    public boolean hasCustomName() {
        return this.p != null && this.p.length() > 0;
    }

    public void a(String s) {
        this.p = s;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);

        this.items = new ItemStack[this.getSize()];
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.p = nbttagcompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j < this.items.length) {
                this.items[j] = ItemStack.createStack(nbttagcompound1);
            }
        }

    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Slot", (byte) i);
                this.items[i].save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Items", nbttaglist);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.p);
        }

    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityHuman entityhuman) {
        if (this.world == null) return true; // CraftBukkit
        return this.world.getTileEntity(this.position) == this && entityhuman.e((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    public void E() {
        super.E();
        this.a = false;
    }

    private void a(TileEntityChest tileentitychest, EnumDirection enumdirection) {
        if (tileentitychest.x()) {
            this.a = false;
        } else if (this.a) {
            switch (TileEntityChest.SyntheticClass_1.a[enumdirection.ordinal()]) {
            case 1:
                if (this.f != tileentitychest) {
                    this.a = false;
                }
                break;

            case 2:
                if (this.i != tileentitychest) {
                    this.a = false;
                }
                break;

            case 3:
                if (this.g != tileentitychest) {
                    this.a = false;
                }
                break;

            case 4:
                if (this.h != tileentitychest) {
                    this.a = false;
                }
            }
        }

    }

    public void m() {
        if (!this.a) {
            this.a = true;
            this.h = this.a(EnumDirection.WEST);
            this.g = this.a(EnumDirection.EAST);
            this.f = this.a(EnumDirection.NORTH);
            this.i = this.a(EnumDirection.SOUTH);
        }
    }

    protected TileEntityChest a(EnumDirection enumdirection) {
        BlockPosition blockposition = this.position.shift(enumdirection);

        if (this.b(blockposition)) {
            TileEntity tileentity = this.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                TileEntityChest tileentitychest = (TileEntityChest) tileentity;

                tileentitychest.a(this, enumdirection.opposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean b(BlockPosition blockposition) {
        if (this.world == null) {
            return false;
        } else {
            Block block = this.world.getType(blockposition).getBlock();

            return block instanceof BlockChest && ((BlockChest) block).b == this.n();
        }
    }

    public void c() {

    }

    public boolean c(int i, int j) {
        if (i == 1) {
            this.l = j;
            return true;
        } else {
            return super.c(i, j);
        }
    }

    public void startOpen(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator()) {
            if (this.l < 0) {
                this.l = 0;
            }
            int oldPower = Math.min(15, this.l); // CraftBukkit - Get power before new viewer is added

            ++this.l;
            if (this.world == null) return; // CraftBukkit

            // PaperSpigot start - Move chest open sound out of the tick loop
            this.m();

            if (this.l > 0 && this.j == 0.0F && this.f == null && this.h == null) {
                this.j = 0.7F;

                double d0 = (double) this.position.getZ() + 0.5D;
                double d1 = (double) this.position.getX() + 0.5D;

                if (this.i != null) {
                    d0 += 0.5D;
                }

                if (this.g != null) {
                    d1 += 0.5D;
                }

                this.world.makeSound(d1, (double) this.position.getY() + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
            // PaperSpigot end

            this.world.playBlockAction(this.position, this.w(), 1, this.l);

            // CraftBukkit start - Call redstone event
            if (this.w() == Blocks.TRAPPED_CHEST) {
                int newPower = Math.max(0, Math.min(15, this.l));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, position.getX(), position.getY(), position.getZ(), oldPower, newPower);
                }
            }
            // CraftBukkit end
            world.applyPhysics(position, w());
            world.applyPhysics(position.getX(), position.getY() - 1, position.getZ(), w());
        }

    }

    public void closeContainer(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator() && this.w() instanceof BlockChest) {
            int oldPower = Math.max(0, Math.min(15, this.l)); // CraftBukkit - Get power before new viewer is added
            --this.l;
            if (this.world == null) return; // CraftBukkit

            // PaperSpigot start - Move chest close sound handling out of the tick loop
            if (this.l == 0 && this.j > 0.0F || this.l > 0 && this.j < 1.0F) {
                float f = 0.1F;

                if (this.l > 0) {
                    this.j += f;
                } else {
                    this.j -= f;
                }

                double d0 = (double) this.getPosition().getX() + 0.5D;
                double d2 = (double) this.getPosition().getZ() + 0.5D;

                if (this.i != null) {
                    d2 += 0.5D;
                }

                if (this.g != null) {
                    d0 += 0.5D;
                }

                this.world.makeSound(d0, (double) this.getPosition().getY() + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
                this.j = 0.0F;
            }
            // PaperSpigot end

            this.world.playBlockAction(this.position, this.w(), 1, this.l);

            // CraftBukkit start - Call redstone event
            if (this.w() == Blocks.TRAPPED_CHEST) {
                int newPower = Math.max(0, Math.min(15, this.l));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, position.getX(), position.getY(), position.getZ(), oldPower, newPower);
                }
            }
            // CraftBukkit end
            world.applyPhysics(position, w());
            world.applyPhysics(position.getX(), position.getY() - 1, position.getZ(), w());
        }

    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public void y() {
        super.y();
        this.E();
        this.m();
    }

    public int n() {
        if (this.o == -1) {
            if (this.world == null || !(this.w() instanceof BlockChest)) {
                return 0;
            }

            this.o = ((BlockChest) this.w()).b;
        }

        return this.o;
    }

    public String getContainerName() {
        return "minecraft:chest";
    }

    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerChest(playerinventory, this, entityhuman);
    }

    public int getProperty(int i) {
        return 0;
    }

    public void b(int i, int j) {}

    public int g() {
        return 0;
    }

    public void l() {
        Arrays.fill(this.items, null);

    }

    // CraftBukkit start
    // PAIL
    @Override
    public boolean F() {
        return true;
    }
    // CraftBukkit end

    static class SyntheticClass_1 {

        static final int[] a = new int[EnumDirection.values().length];

        static {
            try {
                TileEntityChest.SyntheticClass_1.a[EnumDirection.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                TileEntityChest.SyntheticClass_1.a[EnumDirection.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                TileEntityChest.SyntheticClass_1.a[EnumDirection.EAST.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                TileEntityChest.SyntheticClass_1.a[EnumDirection.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {

            }

        }
    }
}
