package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Sound;

public class RelativeSound extends RelativeLocation {

	private final Sound sound;
	private final float volume;
	private final float pitch;

	public RelativeSound(final double x, final double y, final double z, final Sound sound, final float volume, final float pitch) {
		super(x, y, z);
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}

	public Sound getSound() {
		return sound;
	}

	public float getVolume() {
		return volume;
	}
}
