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

    /**
     * @param id             The Message Id
     * @param defaultMessage The default Message (in English)
     * @param awaitedArgs    The awaited arguments used while parsing the message
     */
    public Message(final MessageId id, final String defaultMessage, final String[] awaitedArgs) {
        this.id = id;
        this.defaultMessage = defaultMessage;
        this.awaitedArgs = awaitedArgs;
        configMessage = null;
    }

    /**
     * @param id             The Message Id
     * @param defaultMessage The default Message (in English)
     * @param awaitedArgs    The awaited arguments used while parsing the message
     * @param configMessage  The Message found in the configuration file
     */
    public Message(final MessageId id, final String defaultMessage, final String[] awaitedArgs, final String configMessage) {
        this.id = id;
        this.defaultMessage = defaultMessage;
        this.awaitedArgs = awaitedArgs;
        this.configMessage = configMessage;
    }

    /** @return a String representation of what arguments were awaited */
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

    /** @return the number of arguments awaited */
    public int getAwaitedArgsNb() {
        if (awaitedArgs == null) {
            return 0;
        } else {
            return awaitedArgs.length;
        }
    }

    public MessageId getId() {
        return id;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getConfigMessage() {
        return configMessage;
    }

    public String[] getAwaitedArgs() {
        return awaitedArgs;
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
