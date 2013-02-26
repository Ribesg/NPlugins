package fr.ribesg.bukkit.nchat;

import lombok.Getter;

public class Format {

    public enum FormatType {
        GROUP,
        PLAYER,
    }

    @Getter private final FormatType type;
    @Getter private final String     name;
    @Getter private final String     prefix;
    @Getter private final String     suffix;

    public Format(final FormatType type, final String name, final String prefix, final String suffix) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
