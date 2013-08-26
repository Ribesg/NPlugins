package fr.ribesg.bukkit.nplayer.punishment;
public class Mute extends Punishment {

	public Mute(String punished, String reason) {
		super(punished, PunishmentType.MUTE, reason);
	}

	public Mute(String punished, String reason, long endDate) {
		super(punished, PunishmentType.MUTE, reason, endDate);
	}
}
