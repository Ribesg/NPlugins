/***************************************************************************
 * Project file:    NPlugins - NCore - Info.java                           *
 * Full Class name: fr.ribesg.bukkit.ncore.info.Info                       *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.info;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;

/**
 *
 */
public final class Info {

    /**
     *
     */
    public static enum Type {
        FULL,
        PLAYER,
        REGION,
        WARP,
        WORLD,
    }

    /*
     * TODO Add maingroup and groups
     */

    private boolean      hasPlayerInfo;
    private UUID         playerId;                       // NPlayer
    private String       playerNick;                     // NTalk
    private String       playerName;                     // NPlayer
    private List<String> playerNames;                    // NPlayer
    private Boolean      playerIsOp;                     // NPermissions
    private String       playerMainGroup;                // NPermissions
    private List<String> playerGroups;                   // NPermissions
    private String       playerIp;                       // NPlayer
    private String       playerIps;                      // NPlayer
    private List<String> playerRegions;                  // NCuboid
    private String       playerRegionAmount;             // NCuboid
    private String       playerAllowedRegionAmount;      // NCuboid
    private Boolean      playerGodMode;                  // NGeneral
    private Boolean      playerFlyMode;                  // NGeneral
    private GameMode     playerGameMode;                 // NGeneral
    private String       playerHomeLocation;             // NPlayer
    private String       playerFirstSeen;                // NPlayer
    private String       playerLastSeen;                 // NPlayer
    private String       playerPrefix;                   // NTalk
    private String       playerSuffix;                   // NTalk

    private boolean             hasRegionInfo;
    private String              regionName;              // NCuboid
    private String              regionCorner1Location;   // NCuboid
    private String              regionCorner2Location;   // NCuboid
    private List<String>        regionEnabledFlags;      // NCuboid
    private Map<String, String> regionAttributesValues;  // NCuboid
    private String              regionOwner;             // NCuboid
    private List<String>        regionAdmins;            // NCuboid
    private List<String>        regionGroups;            // NCuboid
    private List<String>        regionUsers;             // NCuboid

    private boolean hasWarpInfo;
    private String  warpName;                            // NWorld
    private String  warpLocation;                        // NWorld
    private String  warpPermission;                      // NWorld
    private Boolean warpHidden;                          // NWorld

    private boolean             hasWorldInfo;
    private String              worldName;               // NWorld
    private String              worldSpawnLocation;      // NWorld
    private String              worldPermission;         // NWorld
    private Boolean             worldHidden;             // NWorld
    private String              worldRegionName;         // NCuboid
    private List<String>        worldRegionEnabledFlags; // NCuboid
    private Map<String, String> worldRegionAttributes;   // NCuboid
    private List<String>        worldRegionAdmins;       // NCuboid
    private List<String>        worldRegionGroups;       // NCuboid
    private List<String>        worldRegionUsers;        // NCuboid

    private boolean      hasEntryNotes;
    private List<String> entryPublicNotes;
    private List<String> entryPrivateNotes;

    public boolean hasPlayerInfo() {
        return this.hasPlayerInfo;
    }

    public void setHasPlayerInfo(final boolean hasPlayerInfo) {
        this.hasPlayerInfo = hasPlayerInfo;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(final UUID playerId) {
        this.playerId = playerId;
    }

    public String getPlayerNick() {
        return this.playerNick;
    }

    public void setPlayerNick(final String playerNick) {
        this.playerNick = playerNick;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public Boolean isPlayerOp() {
        return this.playerIsOp;
    }

    public void setPlayerIsOp(final Boolean playerIsOp) {
        this.playerIsOp = playerIsOp;
    }

    public String getPlayerMainGroup() {
        return this.playerMainGroup;
    }

    public void setPlayerMainGroup(final String playerMainGroup) {
        this.playerMainGroup = playerMainGroup;
    }

    public List<String> getPlayerGroups() {
        return this.playerGroups;
    }

    public void setPlayerGroups(final List<String> playerGroups) {
        this.playerGroups = playerGroups;
    }

    public List<String> getPlayerNames() {
        return this.playerNames;
    }

    public void setPlayerNames(final List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public String getPlayerIp() {
        return this.playerIp;
    }

    public void setPlayerIp(final String playerIp) {
        this.playerIp = playerIp;
    }

    public String getPlayerIps() {
        return this.playerIps;
    }

    public void setPlayerIps(final String playerIps) {
        this.playerIps = playerIps;
    }

    public List<String> getPlayerRegions() {
        return this.playerRegions;
    }

    public void setPlayerRegions(final List<String> playerRegions) {
        this.playerRegions = playerRegions;
    }

    public String getPlayerRegionAmount() {
        return this.playerRegionAmount;
    }

    public void setPlayerRegionAmount(final String playerRegionAmount) {
        this.playerRegionAmount = playerRegionAmount;
    }

    public String getPlayerAllowedRegionAmount() {
        return this.playerAllowedRegionAmount;
    }

    public void setPlayerAllowedRegionAmount(final String playerAllowedRegionAmount) {
        this.playerAllowedRegionAmount = playerAllowedRegionAmount;
    }

    public Boolean isPlayerGodMode() {
        return this.playerGodMode;
    }

    public void setPlayerGodMode(final boolean playerGodMode) {
        this.playerGodMode = playerGodMode;
    }

    public Boolean isPlayerFlyMode() {
        return this.playerFlyMode;
    }

    public void setPlayerFlyMode(final boolean playerFlyMode) {
        this.playerFlyMode = playerFlyMode;
    }

    public GameMode getPlayerGameMode() {
        return this.playerGameMode;
    }

    public void setPlayerGameMode(final GameMode playerGameMode) {
        this.playerGameMode = playerGameMode;
    }

    public String getPlayerHomeLocation() {
        return this.playerHomeLocation;
    }

    public void setPlayerHomeLocation(final String playerHomeLocation) {
        this.playerHomeLocation = playerHomeLocation;
    }

    public String getPlayerFirstSeen() {
        return this.playerFirstSeen;
    }

    public void setPlayerFirstSeen(final String playerFirstSeen) {
        this.playerFirstSeen = playerFirstSeen;
    }

    public String getPlayerLastSeen() {
        return this.playerLastSeen;
    }

    public void setPlayerLastSeen(final String playerLastSeen) {
        this.playerLastSeen = playerLastSeen;
    }

    public String getPlayerPrefix() {
        return this.playerPrefix;
    }

    public void setPlayerPrefix(final String playerPrefix) {
        this.playerPrefix = playerPrefix;
    }

    public String getPlayerSuffix() {
        return this.playerSuffix;
    }

    public void setPlayerSuffix(final String playerSuffix) {
        this.playerSuffix = playerSuffix;
    }

    public Boolean hasRegionInfo() {
        return this.hasRegionInfo;
    }

    public void setHasRegionInfo(final boolean hasRegionInfo) {
        this.hasRegionInfo = hasRegionInfo;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCorner1Location() {
        return this.regionCorner1Location;
    }

    public void setRegionCorner1Location(final String regionCorner1Location) {
        this.regionCorner1Location = regionCorner1Location;
    }

    public String getRegionCorner2Location() {
        return this.regionCorner2Location;
    }

    public void setRegionCorner2Location(final String regionCorner2Location) {
        this.regionCorner2Location = regionCorner2Location;
    }

    public List<String> getRegionEnabledFlags() {
        return this.regionEnabledFlags;
    }

    public void setRegionEnabledFlags(final List<String> regionEnabledFlags) {
        this.regionEnabledFlags = regionEnabledFlags;
    }

    public Map<String, String> getRegionAttributesValues() {
        return this.regionAttributesValues;
    }

    public void setRegionAttributesValues(final Map<String, String> regionAttributesValues) {
        this.regionAttributesValues = regionAttributesValues;
    }

    public String getRegionOwner() {
        return this.regionOwner;
    }

    public void setRegionOwner(final String regionOwner) {
        this.regionOwner = regionOwner;
    }

    public List<String> getRegionAdmins() {
        return this.regionAdmins;
    }

    public void setRegionAdmins(final List<String> regionAdmins) {
        this.regionAdmins = regionAdmins;
    }

    public List<String> getRegionGroups() {
        return this.regionGroups;
    }

    public void setRegionGroups(final List<String> regionGroups) {
        this.regionGroups = regionGroups;
    }

    public List<String> getRegionUsers() {
        return this.regionUsers;
    }

    public void setRegionUsers(final List<String> regionUsers) {
        this.regionUsers = regionUsers;
    }

    public Boolean hasWarpInfo() {
        return this.hasWarpInfo;
    }

    public void setHasWarpInfo(final boolean hasWarpInfo) {
        this.hasWarpInfo = hasWarpInfo;
    }

    public String getWarpName() {
        return this.warpName;
    }

    public void setWarpName(final String warpName) {
        this.warpName = warpName;
    }

    public String getWarpLocation() {
        return this.warpLocation;
    }

    public void setWarpLocation(final String warpLocation) {
        this.warpLocation = warpLocation;
    }

    public String getWarpPermission() {
        return this.warpPermission;
    }

    public void setWarpPermission(final String warpPermission) {
        this.warpPermission = warpPermission;
    }

    public Boolean isWarpHidden() {
        return this.warpHidden;
    }

    public void setWarpHidden(final boolean warpHidden) {
        this.warpHidden = warpHidden;
    }

    public Boolean hasWorldInfo() {
        return this.hasWorldInfo;
    }

    public void setHasWorldInfo(final boolean hasWorldInfo) {
        this.hasWorldInfo = hasWorldInfo;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public String getWorldSpawnLocation() {
        return this.worldSpawnLocation;
    }

    public void setWorldSpawnLocation(final String worldSpawnLocation) {
        this.worldSpawnLocation = worldSpawnLocation;
    }

    public String getWorldPermission() {
        return this.worldPermission;
    }

    public void setWorldPermission(final String worldPermission) {
        this.worldPermission = worldPermission;
    }

    public Boolean isWorldHidden() {
        return this.worldHidden;
    }

    public void setWorldHidden(final boolean worldHidden) {
        this.worldHidden = worldHidden;
    }

    public String getWorldRegionName() {
        return this.worldRegionName;
    }

    public void setWorldRegionName(final String worldRegionName) {
        this.worldRegionName = worldRegionName;
    }

    public List<String> getWorldRegionEnabledFlags() {
        return this.worldRegionEnabledFlags;
    }

    public void setWorldRegionEnabledFlags(final List<String> worldRegionEnabledFlags) {
        this.worldRegionEnabledFlags = worldRegionEnabledFlags;
    }

    public Map<String, String> getWorldRegionAttributes() {
        return this.worldRegionAttributes;
    }

    public void setWorldRegionAttributes(final Map<String, String> worldRegionAttributes) {
        this.worldRegionAttributes = worldRegionAttributes;
    }

    public List<String> getWorldRegionAdmins() {
        return this.worldRegionAdmins;
    }

    public void setWorldRegionAdmins(final List<String> worldRegionAdmins) {
        this.worldRegionAdmins = worldRegionAdmins;
    }

    public List<String> getWorldRegionGroups() {
        return this.worldRegionGroups;
    }

    public void setWorldRegionGroups(final List<String> worldRegionGroups) {
        this.worldRegionGroups = worldRegionGroups;
    }

    public List<String> getWorldRegionUsers() {
        return this.worldRegionUsers;
    }

    public void setWorldRegionUsers(final List<String> worldRegionUsers) {
        this.worldRegionUsers = worldRegionUsers;
    }

    public boolean hasEntryNotes() {
        return this.hasEntryNotes;
    }

    public void setHasEntryNotes(final boolean hasEntryNotes) {
        this.hasEntryNotes = hasEntryNotes;
    }

    public List<String> getEntryPublicNotes() {
        return this.entryPublicNotes;
    }

    public void setEntryPublicNotes(final List<String> entryPublicNotes) {
        this.entryPublicNotes = entryPublicNotes;
    }

    public List<String> getEntryPrivateNotes() {
        return this.entryPrivateNotes;
    }

    public void setEntryPrivateNotes(final List<String> entryPrivateNotes) {
        this.entryPrivateNotes = entryPrivateNotes;
    }
}
