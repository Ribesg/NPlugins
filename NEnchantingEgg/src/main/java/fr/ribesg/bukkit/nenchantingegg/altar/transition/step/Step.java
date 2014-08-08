/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Step.java                  *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;

public abstract class Step {

    private final int delay;

    public Step(final int delay) {
        this.delay = delay;
    }

    public abstract void doStep(final Altar altar);

    public int getDelay() {
        return this.delay;
    }
}
