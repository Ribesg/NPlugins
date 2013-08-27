package fr.ribesg.bukkit.nplayer.punishment;
public class Kick extends Punishment {

	public Kick(String kicked, String reason) {
		super(kicked, PunishmentType.KICK, reason);
	}
}
