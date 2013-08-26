package fr.ribesg.bukkit.nplayer.punishment;
public class Kick extends Punishment {

	public Kick(String punished, String reason) {
		super(punished, PunishmentType.KICK, reason);
	}
}
