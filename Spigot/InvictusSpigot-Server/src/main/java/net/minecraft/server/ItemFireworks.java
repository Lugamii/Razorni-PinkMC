package net.minecraft.server;

public class ItemFireworks extends Item {

    public ItemFireworks() {
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2) {
        EntityFireworks entityfireworks = new EntityFireworks(world, (float) blockposition.getX() + f, (float) blockposition.getY() + f1, (float) blockposition.getZ() + f2, itemstack);

        world.addEntity(entityfireworks);
        if (!entityhuman.abilities.canInstantlyBuild) {
            --itemstack.count;
        }

        return true;
    }
}
