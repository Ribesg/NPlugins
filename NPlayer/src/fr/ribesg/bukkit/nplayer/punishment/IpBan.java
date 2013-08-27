package fr.ribesg.bukkit.nplayer.punishment;
public class IpBan extends Punishment {

	public IpBan(String banned, String reason) {
		super(banned, PunishmentType.IPBAN, reason);
	}

	public IpBan(String banned, String reason, long endDate) {
		super(banned, PunishmentType.IPBAN, reason, endDate);
	}
}
