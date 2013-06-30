package fr.ribesg.bukkit.ntalk.format;

public class Format {

    public enum FormatType {
        GROUP,
        PLAYER,
    }

    private final FormatType type;
    private final String     name;
    private final String     prefix;
    private final String     suffix;

    public Format(final FormatType type, final String name, final String prefix, final String suffix) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public FormatType getType() {
        return type;
    }
}
