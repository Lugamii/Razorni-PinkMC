package net.minecraft.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutPlayerInfo implements Packet<PacketListenerPlayOut> {

    private PacketPlayOutPlayerInfo.EnumPlayerInfoAction a;
    private final List<PacketPlayOutPlayerInfo.PlayerInfoData> b = Lists.newArrayList();

    public PacketPlayOutPlayerInfo() {
    }

    public PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction packetplayoutplayerinfo_enumplayerinfoaction, EntityPlayer... aentityplayer) {
        this.a = packetplayoutplayerinfo_enumplayerinfoaction;

        for (EntityPlayer entityplayer : aentityplayer) {
            this.b.add(new PlayerInfoData(entityplayer.getProfile(), entityplayer.ping, entityplayer.playerInteractManager.getGameMode(), entityplayer.getPlayerListName()));
        }

    }

    public PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction packetplayoutplayerinfo_enumplayerinfoaction, Iterable<EntityPlayer> iterable) {
        this.a = packetplayoutplayerinfo_enumplayerinfoaction;

        for (EntityPlayer entityplayer : iterable) {
            this.b.add(new PlayerInfoData(entityplayer.getProfile(), entityplayer.ping, entityplayer.playerInteractManager.getGameMode(), entityplayer.getPlayerListName()));
        }

    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.a(EnumPlayerInfoAction.class);
        int i = packetdataserializer.e();

        for (int j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            WorldSettings.EnumGamemode worldsettings_enumgamemode = null;
            IChatBaseComponent ichatbasecomponent = null;

            switch (PacketPlayOutPlayerInfo.SyntheticClass_1.a[this.a.ordinal()]) {
                case 1:
                    gameprofile = new GameProfile(packetdataserializer.g(), packetdataserializer.c(16));
                    int l = packetdataserializer.e();

                    for (int i1 = 0; i1 < l; ++i1) {
                        String s = packetdataserializer.c(32767);
                        String s1 = packetdataserializer.c(32767);

                        if (packetdataserializer.readBoolean()) {
                            gameprofile.getProperties().put(s, new Property(s, s1, packetdataserializer.c(32767)));
                        } else {
                            gameprofile.getProperties().put(s, new Property(s, s1));
                        }
                    }

                    worldsettings_enumgamemode = WorldSettings.EnumGamemode.getById(packetdataserializer.e());
                    k = packetdataserializer.e();
                    if (packetdataserializer.readBoolean()) {
                        ichatbasecomponent = packetdataserializer.d();
                    }
                    break;

                case 2:
                    gameprofile = new GameProfile(packetdataserializer.g(), null);
                    worldsettings_enumgamemode = WorldSettings.EnumGamemode.getById(packetdataserializer.e());
                    break;

                case 3:
                    gameprofile = new GameProfile(packetdataserializer.g(), null);
                    k = packetdataserializer.e();
                    break;

                case 4:
                    gameprofile = new GameProfile(packetdataserializer.g(), null);
                    if (packetdataserializer.readBoolean()) {
                        ichatbasecomponent = packetdataserializer.d();
                    }
                    break;

                case 5:
                    gameprofile = new GameProfile(packetdataserializer.g(), null);
            }

            this.b.add(new PacketPlayOutPlayerInfo.PlayerInfoData(gameprofile, k, worldsettings_enumgamemode, ichatbasecomponent));
        }

    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.b(this.b.size());

        for (PlayerInfoData packetplayoutplayerinfo_playerinfodata : this.b) {
            switch (SyntheticClass_1.a[this.a.ordinal()]) {
                case 1:
                    GameProfile profile = packetplayoutplayerinfo_playerinfodata.a();
                    packetdataserializer.a(profile.getId());
                    packetdataserializer.a(profile.getName());
                    packetdataserializer.b(profile.getProperties().size());

                    for (Property property : profile.getProperties().values()) {
                        packetdataserializer.a(property.getName());
                        packetdataserializer.a(property.getValue());
                        if (property.hasSignature()) {
                            packetdataserializer.writeBoolean(true);
                            packetdataserializer.a(property.getSignature());
                        } else {
                            packetdataserializer.writeBoolean(false);
                        }
                    }

                    packetdataserializer.b(packetplayoutplayerinfo_playerinfodata.c().getId());
                    packetdataserializer.b(packetplayoutplayerinfo_playerinfodata.b());
                    if (packetplayoutplayerinfo_playerinfodata.d() == null) {
                        packetdataserializer.writeBoolean(false);
                    } else {
                        packetdataserializer.writeBoolean(true);
                        packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.d());
                    }
                    break;

                case 2:
                    packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.a().getId());
                    packetdataserializer.b(packetplayoutplayerinfo_playerinfodata.c().getId());
                    break;

                case 3:
                    packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.a().getId());
                    packetdataserializer.b(packetplayoutplayerinfo_playerinfodata.b());
                    break;

                case 4:
                    packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.a().getId());
                    if (packetplayoutplayerinfo_playerinfodata.d() == null) {
                        packetdataserializer.writeBoolean(false);
                    } else {
                        packetdataserializer.writeBoolean(true);
                        packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.d());
                    }
                    break;

                case 5:
                    packetdataserializer.a(packetplayoutplayerinfo_playerinfodata.a().getId());
            }
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("action", this.a).add("entries", this.b).toString();
    }


    static class SyntheticClass_1 {

        static final int[] a = new int[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values().length];

        static {
            try {
                PacketPlayOutPlayerInfo.SyntheticClass_1.a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                PacketPlayOutPlayerInfo.SyntheticClass_1.a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                PacketPlayOutPlayerInfo.SyntheticClass_1.a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                PacketPlayOutPlayerInfo.SyntheticClass_1.a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                PacketPlayOutPlayerInfo.SyntheticClass_1.a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }

    public class PlayerInfoData {

        private final int b;
        private final WorldSettings.EnumGamemode c;
        private final GameProfile d;
        private final IChatBaseComponent e;

        public PlayerInfoData(GameProfile gameprofile, int i, WorldSettings.EnumGamemode worldsettings_enumgamemode, IChatBaseComponent ichatbasecomponent) {
            this.d = gameprofile;
            this.b = i;
            this.c = worldsettings_enumgamemode;
            this.e = ichatbasecomponent;
        }

        public GameProfile a() {
            return this.d;
        }

        public int b() {
            return this.b;
        }

        public WorldSettings.EnumGamemode c() {
            return this.c;
        }

        public IChatBaseComponent d() {
            return this.e;
        }

        public String toString() {
            return Objects.toStringHelper(this).add("latency", this.b).add("gameMode", this.c).add("profile", this.d).add("displayName", this.e == null ? null : IChatBaseComponent.ChatSerializer.a(this.e)).toString();
        }
    }

    public static enum EnumPlayerInfoAction {

        ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;

        private EnumPlayerInfoAction() {
        }
    }
}
