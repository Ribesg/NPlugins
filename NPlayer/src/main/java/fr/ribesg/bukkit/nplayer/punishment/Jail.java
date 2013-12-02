/***************************************************************************
 * Project file:    NPlugins - NPlayer - Jail.java                         *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.Jail               *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
