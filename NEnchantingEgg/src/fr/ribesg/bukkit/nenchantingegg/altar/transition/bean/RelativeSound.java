package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import lombok.Getter;

import org.bukkit.Sound;

public class RelativeSound extends Relative {

    @Getter private final Sound sound;
    @Getter private final float volume;
    @Getter private final float pitch;

    public RelativeSound(final int x, final int y, final int z, final Sound sound, final float volume, final float pitch) {
        super(x, y, z);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

}
