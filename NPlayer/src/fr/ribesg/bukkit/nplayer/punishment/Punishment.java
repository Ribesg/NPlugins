package fr.ribesg.bukkit.nplayer.punishment;
public abstract class Punishment {

	private final String         punished;
	private final PunishmentType type;
	private final long           endDate;
	private final String         reason;

	protected Punishment(String punished, PunishmentType type, String reason) {
		this(punished, type, reason, -1);
	}

	protected Punishment(String punished, PunishmentType type, String reason, long endDate) {
		this.endDate = endDate;
		this.punished = punished;
		this.type = type;
		this.reason = reason;
	}

	public boolean hasEndDate() {
		return endDate > 0;
	}

	public long getEndDate() {
		return endDate;
	}

	public PunishmentType getType() {
		return type;
	}

	public String getPunished() {
		return punished;
	}

	public String getReason() {
		return reason;
	}
}
