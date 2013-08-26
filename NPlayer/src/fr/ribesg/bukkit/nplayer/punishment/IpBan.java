package fr.ribesg.bukkit.nplayer.punishment;
public class IpBan extends Punishment {

	public IpBan(String punished, String reason) {
		super(punished, PunishmentType.IPBAN, reason);
	}

	public IpBan(String punished, String reason, long endDate) {
		super(punished, PunishmentType.IPBAN, reason, endDate);
	}
}
