package fr.ribesg.bukkit.nenchantingegg.altar;

import lombok.Getter;

import org.bukkit.Location;

public class Altar {

    @Getter private final Location loc;

    public Altar(final Location loc) {
        this.loc = loc;
    }
}
