package fr.ribesg.bukkit.nplayer.punishment;
public class Ban extends Punishment {

	public Ban(String banned, String reason) {
		super(banned, PunishmentType.BAN, reason);
	}

	public Ban(String punished, String reason, long endDate) {
		super(punished, PunishmentType.BAN, reason, endDate);
	}
}
