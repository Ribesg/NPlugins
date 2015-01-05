/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeSound.java         *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSound
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
        return this.pitch;
    }

    public Sound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }
}
