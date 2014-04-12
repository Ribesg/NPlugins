/***************************************************************************
 * Project file:    NPlugins - NCore - Message.java                        *
 * Full Class name: fr.ribesg.bukkit.ncore.lang.Message                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.lang;

/**
 * Represents a Message, with is format and params
 *
 * @author Ribesg
 */
public class Message {

	private final MessageId id;
	private final String    defaultMessage;
	private final String    configMessage;
	private final String[]  awaitedArgs;
	private final boolean   defaultUseHeader;
	private final boolean   useHeader;

	/**
	 * @param id               The Message Id
	 * @param defaultMessage   The default Message (in English)
	 * @param defaultUseHeader The default value for useHeader
	 * @param awaitedArgs      The awaited arguments used while parsing the message
	 */
	public Message(final MessageId id, final String defaultMessage, final boolean defaultUseHeader, final String[] awaitedArgs) {
		this.id = id;
		this.defaultMessage = defaultMessage;
		this.awaitedArgs = awaitedArgs;
		this.configMessage = null;
		this.defaultUseHeader = defaultUseHeader;
		this.useHeader = defaultUseHeader;
	}

	/**
	 * @param id               The Message Id
	 * @param defaultMessage   The default Message (in English)
	 * @param awaitedArgs      The awaited arguments used while parsing the message
	 * @param configMessage    The Message found in the configuration file
	 * @param defaultUseHeader The default value for useHeader
	 * @param useHeader        Defines if we should prepend the header or not
	 */
	public Message(final MessageId id, final String defaultMessage, final String[] awaitedArgs, final String configMessage, final boolean defaultUseHeader, final boolean useHeader) {
		this.id = id;
		this.defaultMessage = defaultMessage;
		this.awaitedArgs = awaitedArgs;
		this.configMessage = configMessage;
		this.defaultUseHeader = defaultUseHeader;
		this.useHeader = useHeader;
	}

	/**
	 * @return a String representation of what arguments were awaited
	 */
	public String getAwaitedArgsString() {
		if (awaitedArgs == null) {
			return "none";
		} else {
			final StringBuilder s = new StringBuilder();
			for (final String arg : awaitedArgs) {
				s.append(arg);
				s.append(" ; ");
			}
			return s.toString().substring(0, s.length() - 3);
		}
	}

	/**
	 * @return the number of arguments awaited
	 */
	public int getAwaitedArgsNb() {
		if (awaitedArgs == null) {
			return 0;
		} else {
			return awaitedArgs.length;
		}
	}

	public MessageId getId() {
		return this.id;
	}

	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	public String getConfigMessage() {
		return this.configMessage;
	}

	public String[] getAwaitedArgs() {
		return this.awaitedArgs;
	}

	public boolean defaultUseHeader() {
		return this.defaultUseHeader;
	}

	public boolean useHeader() {
		return this.useHeader;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Message other = (Message) obj;
		return id == other.id;
	}
}
