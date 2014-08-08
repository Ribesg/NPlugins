/***************************************************************************
 * Project file:    NPlugins - NPermissions - WorldPlayerPermissions.java  *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.WorldPlayerPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class WorldPlayerPermissions extends PlayerPermissions {

    private final String            worldName;
    private final PlayerPermissions worldPlayer;

    /**
     * World Player Permissions constructor.
     *
     * @param worldName   the world name
     * @param worldPlayer the player
     * @param priority    the priority of this Permissions Set
     */
    public WorldPlayerPermissions(final String worldName, final PlayerPermissions worldPlayer, final int priority) {
        super(worldPlayer.manager, worldPlayer.getPlayerUuid(), worldPlayer.getPlayerName(), priority, worldPlayer.getMainGroup());
        this.worldName = worldName;
        this.worldPlayer = worldPlayer;
    }

    /**
     * World Player Permissions constructor using a WorldLegacyPlayerPermissionsSet.
     *
     * @param playerUuid              the Universally Unique Identifier of the Player
     * @param legacyPlayerPermissions the Legacy Player's permissions
     */
    public WorldPlayerPermissions(final UUID playerUuid, final PlayerPermissions worldPlayer, final WorldLegacyPlayerPermissions legacyPlayerPermissions) {
        super(playerUuid, legacyPlayerPermissions);
        this.worldName = legacyPlayerPermissions.getWorldName();
        this.worldPlayer = worldPlayer;
    }

    /**
     * @see PermissionsSet#computePermissions(Map)
     */
    @Override
    public Map<String, Boolean> computePermissions(final Map<String, Boolean> resultMap) {
        // Create a data structure to store all PermissionsSet related to the Player, grouped and sorted by priority
        final SortedMap<Integer, Set<PermissionsSet>> prioritizedPermissions = new TreeMap<>();

        // Populate it with all the PermissionsSet related to the Player

        final Map<String, WorldGroupPermissions> worldGroupPermissionsMap = this.manager.getWorldGroups().get(this.worldName);

        // 1) Main group
        Set<PermissionsSet> set = new HashSet<>();
        GroupPermissions mainGroupPermissionsSet = worldGroupPermissionsMap.get(this.mainGroup);
        if (mainGroupPermissionsSet == null) {
            mainGroupPermissionsSet = this.manager.getGroups().get(this.mainGroup);
        }
        set.add(mainGroupPermissionsSet);
        prioritizedPermissions.put(mainGroupPermissionsSet.getPriority(), set);

        // 2) Secondary groups
        for (final String groupName : this.groups) {
            GroupPermissions group = worldGroupPermissionsMap.get(groupName);
            if (group == null) {
                group = this.manager.getGroups().get(groupName);
            }
            set = prioritizedPermissions.get(group.getPriority());
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(group);
            prioritizedPermissions.put(group.getPriority(), set);
        }

        // 3) Player Permissions
        set = prioritizedPermissions.get(this.worldPlayer.getPriority());
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(this.worldPlayer);
        prioritizedPermissions.put(this.worldPlayer.getPriority(), set);

        // 4) World specific Player Permissions
        set = prioritizedPermissions.get(this.getPriority());
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(this);
        prioritizedPermissions.put(this.getPriority(), set);

        // Now, read all those permissions and apply them
        for (final Map.Entry<Integer, Set<PermissionsSet>> entry : prioritizedPermissions.entrySet()) {
            if (entry.getKey() == this.getPriority()) {
                for (final PermissionsSet perms : entry.getValue()) {
                    if (perms == this) {
                        // Special for the World specific Player Permissions case, we don't want to recursively call this method
                        resultMap.putAll(this.permissions);
                    } else if (perms == this.worldPlayer) {
                        // Special for the Player Permissions case, we don't want to recursively call this method
                        resultMap.putAll(this.worldPlayer.permissions);
                    } else {
                        resultMap.putAll(perms.getComputedPermissions());
                    }
                }
            } else {
                for (final PermissionsSet perms : entry.getValue()) {
                    resultMap.putAll(perms.getComputedPermissions());
                }
            }
        }

        return resultMap;
    }
}
