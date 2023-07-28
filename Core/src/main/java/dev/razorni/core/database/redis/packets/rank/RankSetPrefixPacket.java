package dev.razorni.core.database.redis.packets.rank;

import dev.razorni.core.extras.rank.Rank;
import dev.razorni.core.util.CC;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import dev.razorni.core.profile.Profile;
import dev.razorni.core.extras.xpacket.XPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 06/09/2021 / 3:44 PM
 * Core / rip.orbit.gravity.database.redis.packets.rank
 */

@AllArgsConstructor
@Data
public class RankSetPrefixPacket implements XPacket {

	private Rank rank;
	private String newPrefix;

	@Override
	public void onReceive() {
		String name = rank.getDisplayName();
		if (rank != null) {
			rank.setPrefix(CC.translate(newPrefix));
			rank.save();

			Bukkit.broadcast(CC.translate("&6&lRank Update &f» &7" + name + " has just been reprefixed &6(Global Update) &7&o(" + newPrefix + ")"), "gravity.staff");
			Bukkit.getConsoleSender().sendMessage(CC.translate("&6&lRank Update &f» &7" + name + " has just been reprefixed &6(Global Update) &7&o(" + newPrefix + ")"));

			for (Profile p : Profile.getProfiles().values()) {
				if (p.getActiveRank().getDisplayName().equals(rank.getDisplayName())) {
					p.getPlayer().setDisplayName(rank.getPrefix() + p.getUsername() + rank.getSuffix());
				}
			}
		}
	}

	@Override
	public String getID() {
		return "Rank SetPrefix";
	}
}
