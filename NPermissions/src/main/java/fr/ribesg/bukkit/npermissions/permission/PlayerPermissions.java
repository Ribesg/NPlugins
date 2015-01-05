/***************************************************************************
 * Project file:    NPlugins - NPermissions - PlayerPermissions.java       *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PlayerPermissions
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents the Permissions attached to a Player.
 *
 * @author Ribesg
 */
public class PlayerPermissions extends PermissionsSet {

    /**
     * The default priority for players is 1
     */
    protected static final int DEFAULT_PLAYER_PRIORITY = 1;

    /**
     * This Player's UUID
     */
    protected final UUID playerUuid;

    /**
     * The main group of attached to this Player
     */
    protected String mainGroup;

    /**
     * Groups this Player belongs to, not counting the main one
     */
    protected final Set<String> groups;

    /**
     * Player Permissions constructor.
     * <p/>
     * This constructor will also add the maingroup.groupname Permission to
     * this Permissions Set.
     *
     * @param manager    the Permissions Manager
     * @param playerUuid the Universally Unique Identifier of the Player
     * @param playerName the last known name of the Player
     * @param priority   the priority of this Permissions Set
     * @param mainGroup  the main Group of the Player
     */
    public PlayerPermissions(final PermissionsManager manager, final UUID playerUuid, final String playerName, final int priority, final String mainGroup) {
        super(manager, playerName, priority);
        this.playerUuid = playerUuid;
        this.mainGroup = mainGroup.toLowerCase();
        this.groups = new LinkedHashSet<>();

        this.permissions.put("maingroup." + mainGroup.toLowerCase(), true);
        this.permissions.put("group." + mainGroup.toLowerCase(), true);
    }

    /**
     * Player Permissions constructor using a LegacyPlayerPermissionsSet.
     *
     * @param playerUuid              the Universally Unique Identifier of the Player
     * @param legacyPlayerPermissions the Legacy Player's permissions
     */
    public PlayerPermissions(final UUID playerUuid, final LegacyPlayerPermissions legacyPlayerPermissions) {
        super(legacyPlayerPermissions.manager, legacyPlayerPermissions.name, legacyPlayerPermissions.priority);
        this.playerUuid = playerUuid;
        this.mainGroup = legacyPlayerPermissions.getMainGroup();
        this.groups = legacyPlayerPermissions.getGroups();
        this.permissions.putAll(legacyPlayerPermissions.permissions);
    }

    /**
     * Gets the Player's Universally Unique Identifier.
     *
     * @return the Player's Universally Unique Identifier
     */
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    /**
     * Gets the name of the Player this PlayerPermissions represents.
     *
     * @return the name of the Player this PlayerPermissions represents
     */
    public String getPlayerName() {
        return this.getName();
    }

    /**
     * Sets the name of the Player this PlayerPermissions represents.
     *
     * @param playerName the new name of the Player this PlayerPermissions
     *                   represents
     */
    public void setPlayerName(final String playerName) {
        this.name = playerName;
    }

    /**
     * Gets the main Group Permissions Set for this Player.
     *
     * @return the main Group Permissions Set for this Player
     */
    public String getMainGroup() {
        return this.mainGroup;
    }

    /**
     * Sets the main Group Permissions Set for this Player.
     *
     * @param mainGroup the new main Group Permissions Set for this Player
     */
    public void setMainGroup(final String mainGroup) {
        this.permissions.remove("maingroup." + this.mainGroup);
        this.permissions.remove("group." + this.mainGroup);
        this.mainGroup = mainGroup.toLowerCase();
        this.permissions.put("maingroup." + this.mainGroup, true);
        this.permissions.put("group." + this.mainGroup, true);
    }

    /**
     * Adds a Group to the Groups for this Player
     *
     * @param group a new additional Group for this Player
     */
    public void addGroup(final String group) {
        this.groups.add(group.toLowerCase());
    }

    /**
     * Gets the Groups for this Player, main Group excluded.
     *
     * @return the Groups for this Player, main Group excluded
     */
    public Set<String> getGroups() {
        return this.groups;
    }

    /**
     * Gets all 'group.x' permissions this Player has.
     *
     * @return all 'group.x' permissions this Player has
     */
    public SortedSet<String> getAllGroupPerms() {
        final SortedSet<String> result = new TreeSet<>();
        result.addAll(this.manager.getGroups().get(this.mainGroup).getAllGroupPerms());
        for (final String secondaryGroup : new TreeSet<>(this.groups)) {
            result.addAll(this.manager.getGroups().get(secondaryGroup).getAllGroupPerms());
        }
        return result;
    }

    /**
     * Saves a representation of this PlayerPermissions under a
     * CongigurationSection, usually the root of a players.yml file.
     *
     * @param parentSection the ConfigurationSection under which this
     *                      PlayerPermissions representation will be saved
     */
    public void save(final ConfigurationSection parentSection) {
        final ConfigurationSection thisSection = parentSection.createSection(this.playerUuid.toString());
        thisSection.set("playerName", this.name);
        thisSection.set("mainGroup", this.mainGroup);
        if (!this.groups.isEmpty()) {
            thisSection.set("groups", new LinkedList<>(this.groups));
        }
        super.saveCommon(thisSection);
    }

    @Override
    public int getDefaultPriority() {
        return DEFAULT_PLAYER_PRIORITY;
    }

    @Override
    public Map<String, Boolean> computePermissions(final Map<String, Boolean> resultMap) {
        // Create a data structure to store all PermissionsSet related to the Player, grouped and sorted by priority
        final SortedMap<Integer, Set<PermissionsSet>> prioritizedPermissions = new TreeMap<>();

        // Populate it with all the PermissionsSet related to the Player

        // 1) Main group
        final GroupPermissions mainGroupPermissionsSet = this.manager.getGroups().get(this.mainGroup);
        Set<PermissionsSet> set = new HashSet<>();
        set.add(mainGroupPermissionsSet);
        prioritizedPermissions.put(mainGroupPermissionsSet.getPriority(), set);

        // 2) Secondary groups
        for (final String groupName : this.groups) {
            final GroupPermissions group = this.manager.getGroups().get(groupName);
            set = prioritizedPermissions.get(group.getPriority());
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(group);
            prioritizedPermissions.put(group.getPriority(), set);
        }

        // 3) Player Permissions
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
                        // Special for the Player Permissions case, we don't want to recursively call this method
                        resultMap.putAll(this.permissions);
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
