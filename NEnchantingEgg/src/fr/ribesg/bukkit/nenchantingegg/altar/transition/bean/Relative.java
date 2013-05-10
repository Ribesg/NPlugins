package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import lombok.Getter;

import org.bukkit.util.Vector;

public abstract class Relative {

    @Getter private final Vector relativeLocation;

    public Relative(final int x, final int y, final int z) {
        relativeLocation = new Vector(x, y, z);
    }
}
