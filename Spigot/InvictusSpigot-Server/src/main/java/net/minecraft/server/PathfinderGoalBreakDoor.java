package net.minecraft.server;

public class PathfinderGoalBreakDoor extends PathfinderGoalDoorInteract {

    private int g;
    private int h = -1;

    public PathfinderGoalBreakDoor(EntityInsentient entityinsentient) {
        super(entityinsentient);
    }

    public boolean a() {
        if (!super.a()) {
            return false;
        } else if (!this.a.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
        } else {
            return !BlockDoor.f(this.a.world, this.b);
        }
    }

    public void c() {
        super.c();
        this.g = 0;
    }

    public boolean b() {
        double d0 = this.a.b(this.b);

        if (this.g <= 240) {
            return !BlockDoor.f(this.a.world, this.b) && d0 < 4.0D;
        }

        return false;
    }

    public void d() {
        super.d();
        this.a.world.c(this.a.getId(), this.b, -1);
    }

    public void e() {
        super.e();
        if (this.a.bc().nextInt(20) == 0) {
            this.a.world.triggerEffect(1010, this.b, 0);
        }

        ++this.g;
        int i = (int) ((float) this.g / 240.0F * 10.0F);

        if (i != this.h) {
            this.a.world.c(this.a.getId(), this.b, i);
            this.h = i;
        }

        if (this.g == 240 && this.a.world.getDifficulty() == EnumDifficulty.HARD) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityBreakDoorEvent(this.a, this.b.getX(), this.b.getY(), this.b.getZ()).isCancelled()) {
                this.c();
                return;
            }
            // CraftBukkit end
            this.a.world.setAir(this.b);
            this.a.world.triggerEffect(1012, this.b, 0);
            this.a.world.triggerEffect(2001, this.b, Block.getId(this.c));
        }

    }
}
