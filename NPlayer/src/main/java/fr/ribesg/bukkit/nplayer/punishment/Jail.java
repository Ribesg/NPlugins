/***************************************************************************
 * Project file:    NPlugins - NPlayer - Jail.java                         *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.Jail               *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;

public class Jail extends Punishment {

	private final String jailPointName;

	public Jail(final String jailed, final String reason, final String jailPointName) {
		super(jailed, PunishmentType.JAIL, reason);
		this.jailPointName = jailPointName;
	}

	public Jail(final String jailed, final String reason, final String jailPointName, final long endDate) {
		super(jailed, PunishmentType.JAIL, reason, endDate);
		this.jailPointName = jailPointName;
	}

	public String getJailPointName() {
		return this.jailPointName;
	}

	@Override
	public String toString() {
		return "Jail{" +
		       "punished='" + this.punished + '\'' +
		       ", type=" + this.type +
		       ", endDate=" + this.endDate +
		       ", reason='" + this.reason + '\'' +
		       ", jailPointName='" + this.jailPointName + '\'' +
		       '}';
	}
}
