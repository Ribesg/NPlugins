package fr.ribesg.bukkit.nenchantingegg.lang;

import lombok.Getter;

import fr.ribesg.bukkit.nenchantingegg.lang.Messages.MessageId;

public class Message {
    @Getter private final MessageId id;
    @Getter private final String    defaultMessage;
    @Getter private final String    configMessage;
    @Getter private final String[]  awaitedArgs;

    public Message(final MessageId id, final String defaultMessage, final String[] awaitedArgs) {
        this.id = id;
        this.defaultMessage = defaultMessage;
        this.awaitedArgs = awaitedArgs;
        configMessage = null;
    }

    public Message(final MessageId id, final String defaultMessage, final String[] awaitedArgs, final String configMessage) {
        this.id = id;
        this.defaultMessage = defaultMessage;
        this.awaitedArgs = awaitedArgs;
        this.configMessage = configMessage;
    }

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

    public int getAwaitedArgsNb() {
        if (awaitedArgs == null) {
            return 0;
        } else {
            return awaitedArgs.length;
        }
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
        if (id != other.id) {
            return false;
        }
        return true;
    }
}
