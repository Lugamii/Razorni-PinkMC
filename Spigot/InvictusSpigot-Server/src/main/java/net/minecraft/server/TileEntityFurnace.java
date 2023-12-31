package net.minecraft.server;

// CraftBukkit start

import eu.vortexdev.invictusspigot.config.InvictusConfig;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.Arrays;
import java.util.List;

public class TileEntityFurnace extends TileEntityContainer implements IUpdatePlayerListBox, IWorldInventory {

    private static final int[] a = new int[]{0};
    private static final int[] f = new int[]{2, 1};
    private static final int[] g = new int[]{1};
    public int burnTime;
    public int cookTime;
    public List<HumanEntity> transaction = new java.util.ArrayList<>();
    private ItemStack[] items = new ItemStack[3];
    private int ticksForCurrentFuel;
    private int cookTimeTotal;
    private String m;
    // CraftBukkit start - add fields and methods
    private int lastTick = MinecraftServer.currentTick;
    private int maxStack = MAX_STACK;

    public TileEntityFurnace() {
    }

    public static int fuelTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            Item item = itemstack.getItem();

            if (item instanceof ItemBlock && Block.asBlock(item) != Blocks.AIR) {
                Block block = Block.asBlock(item);

                if (block == Blocks.WOODEN_SLAB) {
                    return 150;
                } else if (block.getMaterial() == Material.WOOD) {
                    return 300;
                } else  if (block == Blocks.COAL_BLOCK) {
                    return 16000;
                }
            }
            if(item == Items.COAL) return 1600;
            return item instanceof ItemTool && ((ItemTool) item).h().equals("WOOD") ? 200 : item instanceof ItemSword && ((ItemSword) item).h().equals("WOOD") ? 200 : item instanceof ItemHoe && ((ItemHoe) item).g().equals("WOOD") ? 200 : item == Items.STICK ? 100 : item == Items.LAVA_BUCKET ? 20000 : item == Item.getItemOf(Blocks.SAPLING) ? 100 : item == Items.BLAZE_ROD ? 2400 : 0;
        }
    }

    public static boolean isFuel(ItemStack itemstack) {
        return fuelTime(itemstack) > 0;
    }

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }
    // CraftBukkit end

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public int getSize() {
        return this.items.length;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = items[i];
        if (itemstack != null) {
            if (itemstack.count <= j) {
                this.items[i] = null;
            } else {
                if (itemstack.count == 0) {
                    this.items[i] = null;
                }
                itemstack = itemstack.cloneAndSubtract(j);
            }
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        ItemStack itemstack = this.items[i];
        if (itemstack != null) {
            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        ItemStack itemstack1 = this.items[i];
        boolean flag = itemstack != null && itemstack.doMaterialsMatch(itemstack1) && ItemStack.equals(itemstack, itemstack1);

        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        if (i == 0 && !flag) {
            this.cookTimeTotal = this.a(itemstack);
            this.cookTime = 0;
            this.update();
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.m : "container.furnace";
    }

    public boolean hasCustomName() {
        return this.m != null && this.m.length() > 0;
    }

    public void a(String s) {
        this.m = s;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.items.length) {
                this.items[b0] = ItemStack.createStack(nbttagcompound1);
            }
        }

        this.burnTime = nbttagcompound.getShort("BurnTime");
        this.cookTime = nbttagcompound.getShort("CookTime");
        this.cookTimeTotal = nbttagcompound.getShort("CookTimeTotal");
        this.ticksForCurrentFuel = fuelTime(this.items[1]);
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.m = nbttagcompound.getString("CustomName");
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short) this.burnTime);
        nbttagcompound.setShort("CookTime", (short) this.cookTime);
        nbttagcompound.setShort("CookTimeTotal", (short) this.cookTimeTotal);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            ItemStack itemstack = this.items[i];
            if (itemstack != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Slot", (byte) i);
                itemstack.save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Items", nbttaglist);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.m);
        }

    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    public void c() {
        boolean flag = (this.w() == Blocks.LIT_FURNACE); // CraftBukkit - SPIGOT-844 - Check if furnace block is lit using the block instead of burn time // PAIL: Rename
        boolean flag1 = false;

        // CraftBukkit start - Use wall time instead of ticks for cooking
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        this.lastTick = MinecraftServer.currentTick;

        // CraftBukkit - moved from below
        if (this.isBurning() && this.canBurn()) {
            this.cookTime += elapsedTicks * InvictusConfig.cookingBoost;
            if (this.cookTime >= this.cookTimeTotal) {
                this.cookTime = 0;
                this.cookTimeTotal = this.a(this.items[0]);
                this.burn();
                flag1 = true;
            }
        } else {
            this.cookTime = 0;
        }
        // CraftBukkit end

        if (this.isBurning()) {
            this.burnTime -= elapsedTicks; // CraftBukkit - use elapsedTicks in place of constant
        }

        ItemStack itemstack1 = items[1];
        if (!this.isBurning() && (itemstack1 == null || this.items[0] == null)) {
            if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }
        } else {
            if (itemstack1 != null && itemstack1.getItem() != Items.BUCKET) {
                // CraftBukkit start - Handle multiple elapsed ticks
                if (this.burnTime <= 0 && this.canBurn()) { // CraftBukkit - == to <=

                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this.world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), CraftItemStack.asCraftMirror(itemstack1), fuelTime(itemstack1));
                    this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);

                    if (furnaceBurnEvent.isCancelled()) {
                        return;
                    }

                    this.ticksForCurrentFuel = furnaceBurnEvent.getBurnTime();
                    this.burnTime += this.ticksForCurrentFuel;
                    if (this.burnTime > 0 && furnaceBurnEvent.isBurning()) {
                        // CraftBukkit end
                        flag1 = true;
                        itemstack1 = items[1];
                        if (itemstack1 != null) {
                            --itemstack1.count;
                            if (itemstack1.count == 0) {
                                Item item = itemstack1.getItem().q();

                                this.items[1] = item != null ? new ItemStack(item) : null;
                            }
                        }
                    }
                }
            }

        }

        if (flag != this.isBurning()) {
            flag1 = true;
            BlockFurnace.a(this.isBurning(), this.world, this.position);
            this.E(); // CraftBukkit - Invalidate tile entity's cached block type // PAIL: Rename
        }

        if (flag1) {
            this.update();
        }

    }

    public int a(ItemStack itemstack) {
        return 200;
    }

    private boolean canBurn() {
        if (this.items[0] == null) {
            return false;
        } else {
            ItemStack itemstack = RecipesFurnace.getInstance().getResult(this.items[0]);
            if (!this.world.isLoaded(this.position) || ((WorldServer) this.world).chunkProviderServer.unloadQueue.contains(LongHash.toLong(this.position.getX() >> 4, this.position.getZ() >> 4))) {
                return false;
            }
            // CraftBukkit - consider resultant count instead of current count
            return itemstack != null && (this.items[2] == null || (this.items[2].doMaterialsMatch(itemstack) && (this.items[2].count + itemstack.count <= this.getMaxStackSize() && this.items[2].count < this.items[2].getMaxStackSize() || this.items[2].count + itemstack.count <= itemstack.getMaxStackSize())));
        }
    }

    public void burn() {
        if (this.canBurn()) {
            ItemStack itemstack = RecipesFurnace.getInstance().getResult(this.items[0]);

            // CraftBukkit start - fire FurnaceSmeltEvent
            CraftItemStack source = CraftItemStack.asCraftMirror(this.items[0]);
            org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack);

            FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), source, result);
            this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

            if (furnaceSmeltEvent.isCancelled()) {
                return;
            }

            result = furnaceSmeltEvent.getResult();
            itemstack = CraftItemStack.asNMSCopy(result);

            if (itemstack != null) {
                if (this.items[2] == null) {
                    this.items[2] = itemstack;
                } else if (CraftItemStack.asCraftMirror(this.items[2]).isSimilar(result)) {
                    this.items[2].count += itemstack.count;
                } else {
                    return;
                }
            }

            // CraftBukkit end

            if (this.items[0].getItem() == Item.getItemOf(Blocks.SPONGE) && this.items[0].getData() == 1 && this.items[1] != null && this.items[1].getItem() == Items.BUCKET) {
                this.items[1] = new ItemStack(Items.WATER_BUCKET);
            }

            --this.items[0].count;
            if (this.items[0].count <= 0) {
                this.items[0] = null;
            }

        }
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) == this && entityhuman.e((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    public void startOpen(EntityHuman entityhuman) {
    }

    public void closeContainer(EntityHuman entityhuman) {
    }

    public boolean b(int i, ItemStack itemstack) {
        return i != 2 && (i != 1 || isFuel(itemstack) || SlotFurnaceFuel.c_(itemstack));
    }

    public int[] getSlotsForFace(EnumDirection enumdirection) {
        return enumdirection == EnumDirection.DOWN ? TileEntityFurnace.f : (enumdirection == EnumDirection.UP ? TileEntityFurnace.a : TileEntityFurnace.g);
    }

    public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
        return this.b(i, itemstack);
    }

    public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
        if (enumdirection == EnumDirection.DOWN && i == 1) {
            Item item = itemstack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    public String getContainerName() {
        return "minecraft:furnace";
    }

    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerFurnace(playerinventory, this);
    }

    public int getProperty(int i) {
        switch (i) {
            case 0:
                return this.burnTime;

            case 1:
                return this.ticksForCurrentFuel;

            case 2:
                return this.cookTime;

            case 3:
                return this.cookTimeTotal;

            default:
                return 0;
        }
    }

    public void b(int i, int j) {
        switch (i) {
            case 0:
                this.burnTime = j;
                break;

            case 1:
                this.ticksForCurrentFuel = j;
                break;

            case 2:
                this.cookTime = j;
                break;

            case 3:
                this.cookTimeTotal = j;
        }

    }

    public int g() {
        return 4;
    }

    public void l() {
        Arrays.fill(this.items, null);

    }
}
