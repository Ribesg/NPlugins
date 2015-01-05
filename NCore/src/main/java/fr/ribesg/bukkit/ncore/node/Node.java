/***************************************************************************
 * Project file:    NPlugins - NCore - Node.java                           *
 * Full Class name: fr.ribesg.bukkit.ncore.node.Node                       *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node;

/**
 * This interface is implemented by all nodes.
 *
 * @author Ribesg
 */
public interface Node {

    public static final String CUBOID         = "NCuboid";
    public static final String ENCHANTING_EGG = "NEnchantingEgg";
    public static final String GENERAL        = "NGeneral";
    public static final String PERMISSIONS    = "NPermissions";
    public static final String PLAYER         = "NPlayer";
    public static final String TALK           = "NTalk";
    public static final String THE_END_AGAIN  = "NTheEndAgain";
    public static final String WORLD          = "NWorld";

    public String getNodeName();
}
