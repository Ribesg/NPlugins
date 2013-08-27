package fr.ribesg.bukkit.nplayer.punishment;
public class Jail extends Punishment {

	private final String jailPointName;

	public Jail(String jailed, String reason, String jailPointName) {
		super(jailed, PunishmentType.JAIL, reason);
		this.jailPointName = jailPointName;
	}

	public Jail(String jailed, String reason, String jailPointName, long endDate) {
		super(jailed, PunishmentType.JAIL, reason, endDate);
		this.jailPointName = jailPointName;
	}

	public String getJailPointName() {
		return jailPointName;
	}
}
