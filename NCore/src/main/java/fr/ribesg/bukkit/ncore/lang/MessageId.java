/***************************************************************************
 * Project file:    NPlugins - NCore - MessageId.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.lang.MessageId                  *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.lang;

/**
 * @author Ribesg
 */
public enum MessageId {

    // ######################### //
    // ## Not plugin-specific ## //
    // ######################### //

    noPermissionForCommand,
    noPlayerFoundForGivenName,

    cmdOnlyAvailableForPlayers,
    missingWorldArg,
    unknownWorld,

    cmdReloadConfig,
    cmdReloadMessages,
    cmdReloadError,

    // ##################### //
    // ## NCuboid related ## //
    // ##################### //

    cuboid_actionCancelledByCuboid,
    cuboid_doesNotExist,
    cuboid_notCuboidOwner,
    cuboid_notCuboidAdmin,

    cuboid_blockInSelection,
    cuboid_blockNotInSelection,
    cuboid_blockNotProtected,
    cuboid_blockProtectedMultipleRegions,
    cuboid_blockProtectedOneRegion,
    cuboid_firstPointSelected,
    cuboid_noSelection,
    cuboid_secondPointSelected,
    cuboid_selectionReset,

    cuboid_cmdReloadRegions,

    cuboid_cmdCreateAlreadyExists,
    cuboid_cmdCreateCreated,
    cuboid_cmdCreateNoValidSelection,
    cuboid_cmdCreateForbiddenName,
    cuboid_cmdCreateTooMuchRegions,
    cuboid_cmdCreateRegionTooLong,
    cuboid_cmdCreateRegionTooBig,
    cuboid_cmdCreateOverlap,

    cuboid_cmdDeleteDeleted,
    cuboid_cmdDeleteNoPermission,

    cuboid_cmdFlagUnknownFlag,
    cuboid_cmdFlagUnknownValue,
    cuboid_cmdFlagAlreadySet,
    cuboid_cmdFlagSet,
    cuboid_cmdFlagNoPermission,
    cuboid_cmdFlagValue,

    cuboid_cmdAttUnknownFlagAtt,
    cuboid_cmdAttNoPermission,
    cuboid_cmdAttValue,
    cuboid_cmdFlagAttSet,

    cuboid_cmdAdminAdded,
    cuboid_cmdAdminAlreadyAdmin,
    cuboid_cmdAdminRemoved,
    cuboid_cmdAdminNotAdmin,

    cuboid_cmdUserAdded,
    cuboid_cmdUserAlreadyUser,
    cuboid_cmdUserRemoved,
    cuboid_cmdUserNotUser,

    cuboid_cmdGroupAdded,
    cuboid_cmdGroupAlreadyGroup,
    cuboid_cmdGroupRemoved,
    cuboid_cmdGroupNotGroup,

    cuboid_cmdJailNotJailCuboid,
    cuboid_cmdJailNotInRegion,
    cuboid_cmdJailCreated,
    cuboid_cmdJailAlreadyExists,
    cuboid_cmdJailRemoved,
    cuboid_cmdJailUnknown,

    cuboid_enteringPvpArea,
    cuboid_exitingPvpArea,

    // ########################## //
    // ## NTheEndAgain related ## //
    // ########################## //

    theEndAgain_unkownSubCmd,

    theEndAgain_regenerating,
    theEndAgain_respawned1,
    theEndAgain_respawnedX,
    theEndAgain_nbAlive0,
    theEndAgain_nbAlive1,
    theEndAgain_nbAliveX,
    theEndAgain_notInAnEndWorld,

    theEndAgain_protectedChunkInfo,
    theEndAgain_protectedChunkProtect,
    theEndAgain_protectedChunkUnprotect,
    theEndAgain_unprotectedChunkInfo,
    theEndAgain_unprotectedChunkProtect,
    theEndAgain_unprotectedChunkUnprotect,

    theEndAgain_worldRegenerating,

    theEndAgain_receivedXP,
    theEndAgain_receivedDragonEgg,
    theEndAgain_droppedDragonEgg,
    theEndAgain_receivedDrop,
    theEndAgain_droppedDrop,

    theEndAgain_playerKilledADragon,
    theEndAgain_playersKilledADragon,
    theEndAgain_playersKilledADragon_line,
    theEndAgain_playerKilledTheDragon,
    theEndAgain_playersKilledTheDragon,
    theEndAgain_playersKilledTheDragon_line,

    // #################### //
    // ## NWorld related ## //
    // #################### //

    world_availableWorlds,
    world_teleportedToWorld,

    world_notLoaded,
    world_warpToThisWorldDisallowed,

    world_alreadyExists,
    world_alreadyLoaded,

    world_creatingWorldMayBeLaggy,
    world_created,

    world_loadingWorldMayBeLaggy,
    world_loaded,

    world_unloadingWorldMayBeLaggy,
    world_unloaded,

    world_worldHiddenTrue,
    world_worldHiddenFalse,

    world_changedWorldRequiredPermission,

    world_worldNetherEnabled,
    world_worldNetherDisabled,
    world_worldNetherAlreadyEnabled,
    world_worldNetherAlreadyDisabled,

    world_worldEndEnabled,
    world_worldEndDisabled,
    world_worldEndAlreadyEnabled,
    world_worldEndAlreadyDisabled,

    world_alreadyAllowedWorldWarp,
    world_allowedWorldWarp,
    world_alreadyDisallowedWorldWarp,
    world_disallowedWorldWarp,

    world_teleportingToSpawn,
    world_settingSpawnPoint,
    world_invalidFirstSpawnPoint,
    world_teleportingToFirstSpawn,
    world_firstSpawnPointChanged,

    world_teleportedToWarp,

    world_availableWarps,

    world_warpToThisWarpDisallowed,
    world_warpUnloadedWorld,

    world_unknownWarp,

    world_warpHiddenTrue,
    world_warpHiddenFalse,

    world_changedWarpRequiredPermission,

    world_settingWarpPoint,
    world_warpRemoved,

    world_teleportedBecauseOfWorldUnload,

    // ################### //
    // ## NTalk related ## //
    // ################### //

    talk_nobodyToRespond,

    talk_youNickNamed,
    talk_youDeNickNamed,
    talk_youWereNickNamed,
    talk_youWereDeNickNamed,
    talk_invalidUsername,
    talk_invalidNickname,

    talk_filterMutedReason,
    talk_filterBannedReason,
    talk_filterJailedReason,

    // ##################### //
    // ## NPlayer related ## //
    // ##################### //

    player_welcomeToTheServer,
    player_registerFirst,
    player_loginFirst,
    player_welcomeBack,
    player_wrongPassword,
    player_passwordChanged,
    player_alreadyRegistered,
    player_autoLogoutEnabled,
    player_autoLogoutDisabled,
    player_loggedOut,
    player_unknownUser,
    player_userHasNoHome,
    player_teleportingToUserHome,
    player_youHaveNoHome,
    player_teleportingToYourHome,
    player_userHomeSet,
    player_yourHomeSet,

    player_authenticationDisabled,
    player_pleaseRegister,
    player_autoLogged,
    player_pleaseLogin,

    player_kickMessage,
    player_broadcastedKickMessage,

    player_noPermissionForPermanent,

    player_kickPermBanned,
    player_permBannedBroadcast,
    player_kickTempBanned,
    player_tempBannedBroadcast,

    player_unknownIp,
    player_kickPermIpBanned,
    player_permIpBannedBroadcast,
    player_kickTempIpBanned,
    player_tempIpBannedBroadcast,

    player_permMuted,
    player_permMutedBroadcast,
    player_tempMuted,
    player_tempMutedBroadcast,

    player_unBannedBroadcast,
    player_notBanned,
    player_unBannedIpBroadcast,
    player_notBannedIp,
    player_unMutedBroadcast,
    player_notMuted,

    player_deniedPermBanned,
    player_deniedTempBanned,
    player_deniedPermIpBanned,
    player_deniedTempIpBanned,
    player_deniedPermMuted,
    player_deniedTempMuted,

    player_alreadyBanned,
    player_alreadyBannedIp,
    player_alreadyMuted,

    player_cuboidNodeRequired,
    player_unknownJail,
    player_alreadyJailed,
    player_permJailed,
    player_permJailedBroadcast,
    player_tempJailed,
    player_tempJailedBroadcast,
    player_unJailedBroadcast,
    player_notJailed,

    player_standardKickMessage,

    player_loginAttemptsKickMessage,
    player_loginAttemptsBroadcastedKickMessage,
    player_loginAttemptsTempBanMessage,
    player_loginAttemptsBroadcastedTempBanMessage,
    player_loginAttemptsPermBanMessage,
    player_loginAttemptsBroadcastedPermBanMessage,
    player_loginAttemptsTooMany,

    player_youForcedLogin,
    player_somebodyForcedLoginYou,

    // ############################ //
    // ## NEnchantingEgg related ## //
    // ############################ //

    egg_altarCreated,
    egg_altarDestroyed,
    egg_altarProtectedSkullAtNight,
    egg_altarProtectedBlock,
    egg_cantPlaceOnAltar,
    egg_altarTooClose,
    egg_altarEggProvided,

    // ###################### //
    // ## NGeneral related ## //
    // ###################### //

    general_welcome,
    general_welcome_gameMode_survival,
    general_welcome_gameMode_creative,
    general_welcome_gameMode_adventure,
    general_welcome_worldType_normal,
    general_welcome_worldType_nether,
    general_welcome_worldType_end,
    general_welcome_difficulty_peaceful,
    general_welcome_difficulty_easy,
    general_welcome_difficulty_normal,
    general_welcome_difficulty_hard,

    general_god_enabled,
    general_god_disabled,
    general_god_enabledBy,
    general_god_disabledBy,
    general_god_enabledFor,
    general_god_disabledFor,

    general_fly_enabled,
    general_fly_disabled,
    general_fly_enabledBy,
    general_fly_disabledBy,
    general_fly_enabledFor,
    general_fly_disabledFor,

    general_flySpeed_set,
    general_flySpeed_reset,
    general_flySpeed_setFor,
    general_flySpeed_setBy,

    general_walkSpeed_set,
    general_walkSpeed_reset,
    general_walkSpeed_setFor,
    general_walkSpeed_setBy,

    general_afk_nowAfkBroadcast,
    general_afk_noLongerAfkBroadcast,
    general_afk_nowAfkBroadcastReason,
    general_afk_noLongerAfkBroadcastReason,

    general_busy_nowBusyBroadcast,
    general_busy_noLongerBusyBroadcast,
    general_busy_nowBusyBroadcastReason,
    general_busy_noLongerBusyBroadcastReason,

    general_tp_youToTarget,
    general_tp_somebodyToTarget,
    general_tp_youSomebodyToTarget,
    general_tp_somebodyToHim,
    general_tp_youSomebodyToYou,
    general_tp_noTarget,
    general_tp_youToLocation,
    general_tp_somebodyToLocation,
    general_tp_youSomebodyToLocation,
    general_tp_worldNotFound,
    general_tp_youToWorld,
    general_tp_somebodyToWorld,
    general_tp_youSomebodyToWorld,
    general_tp_youNoKnownBack,
    general_tp_youBackWorldUnloaded,
    general_tp_youTeleportedBack,
    general_tp_somebodyNoKnownBack,
    general_tp_somebodyBackWorldUnloaded,
    general_tp_somebodyTeleportedYouBack,
    general_tp_youTeleportedSomebodyBack,

    general_heal_autoHeal,
    general_heal_healedBy,
    general_heal_healed,
    general_feed_autoFeed,
    general_feed_fedBy,
    general_feed_fed,
    general_health_autoSet,
    general_health_setBy,
    general_health_set,
    general_food_autoSet,
    general_food_setBy,
    general_food_set,

    general_timeSet,
    general_weatherSet,

    general_repair_cannot,
    general_repair_done,

    general_nightvision_enabled,
    general_nightvision_disabled,

    general_signcolors_permissionDenied,

    general_protectionsign_accessDenied,
    general_protectionsign_breakDenied,

    general_nicknameFilter_invalid,

    general_itemnetwork_youNeedToBeCreator,
    general_itemnetwork_alreadyExists,
    general_itemnetwork_unknown,
    general_itemnetwork_created,
    general_itemnetwork_deleted,

    general_spy_disabled,
    general_spy_enabled,
    general_spy_enabledPlayer,

    // ########################## //
    // ## NPermissions related ## //
    // ########################## //

    cmdReloadGroups,
    cmdReloadPlayers,

    permissions_unknown,
    permissions_unknownGroup,
    permissions_newPlayer,
    permissions_unknownUuid,
    permissions_changedGroup,
    permissions_newLegacyPlayer,
    permissions_unknownPlayer,
    permissions_changedLegacyGroup,
    permissions_alreadyMainGroup,

}
