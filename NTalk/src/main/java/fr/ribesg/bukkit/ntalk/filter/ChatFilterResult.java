package fr.ribesg.bukkit.ntalk.filter;
/** @author Ribesg */
public enum ChatFilterResult {

	/** Prevent the message from being sent and inform the player */
	DENY,

	/** Replace the bad word occurrence(s) by a provided replacement */
	REPLACE,

	/** Prevent the message from being sent and mute the player */
	TEMPORARY_MUTE,

	/** Prevent the message from being sent and jail the player */
	TEMPORARY_JAIL,

	/** Prevent the message from being sent and kickban the player */
	TEMPORARY_BAN,

	/** Unleash the power of gods on the player. Also Silverfishes. */
	DIVINE_PUNISHMENT
}
