/***************************************************************************
 * Project file:    NPlugins - NPlayer - Punishment.java                   *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.Punishment         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
public abstract class Punishment {

	private final String         punished;
	private final PunishmentType type;
	private final long           endDate;
	private final String         reason;

	protected Punishment(final String punished, final PunishmentType type, final String reason) {
		this(punished, type, reason, -1);
	}

	protected Punishment(final String punished, final PunishmentType type, final String reason, final long endDate) {
		this.endDate = endDate;
		this.punished = punished;
		this.type = type;
		this.reason = reason;
	}

	public boolean isPermanent() {
		return endDate < 0;
	}

	public long getEndDate() {
		return endDate;
	}

	public boolean isStillActive() {
		if (isPermanent()) {
			throw new UnsupportedOperationException();
		}
		return getEndDate() > System.currentTimeMillis();
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
