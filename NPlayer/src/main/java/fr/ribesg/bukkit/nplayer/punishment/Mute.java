package fr.ribesg.bukkit.nplayer.punishment;
public class Mute extends Punishment {

	public Mute(String muted, String reason) {
		super(muted, PunishmentType.MUTE, reason);
	}

	public Mute(String muted, String reason, long endDate) {
		super(muted, PunishmentType.MUTE, reason, endDate);
	}
}
