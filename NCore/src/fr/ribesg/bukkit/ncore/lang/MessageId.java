package fr.ribesg.bukkit.ncore.lang;

/** @author Ribesg */
public enum MessageId {

    // ######################### //
    // ## Not plugin-specific ## //
    // ######################### //

    noPermissionForCommand,
    noPlayerFoundForGivenName,

    cmdOnlyAvailableForPlayers,

    cmdReloadConfig,
    cmdReloadMessages,

    incorrectValueInConfiguration,

    // ##################### //
    // ## NCuboid related ## //
    // ##################### //

    cuboid_actionCancelledByCuboid,

    cuboid_blockInSelection,
    cuboid_blockNotInSelection,
    cuboid_blockNotProtected,
    cuboid_blockProtectedMultipleCuboids,
    cuboid_blockProtectedOneCuboid,
    cuboid_firstPointSelected,
    cuboid_noSelection,
    cuboid_secondPointSelected,
    cuboid_selectionReset,

    cuboid_cmdReloadCuboids,

    cuboid_cmdCreateAlreadyExists,
    cuboid_cmdCreateCreated,
    cuboid_cmdCreateNoValidSelection,

    cuboid_cmdDeleteDoesNotExist,
    cuboid_cmdDeleteDeleted,
    cuboid_cmdDeleteNoPermission,

    // ########################## //
    // ## NTheEndAgain related ## //
    // ########################## //

    theEndAgain_unkownSubCmd,

    theEndAgain_unknownWorld,
    theEndAgain_regenerating,
    theEndAgain_respawned,
    theEndAgain_nbAlive,
    theEndAgain_notInAnEndWorld,
    theEndAgain_missingWorldArg,

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

    // #################### //
    // ## NWorld related ## //
    // #################### //

    world_availableWorlds,
    world_teleportedToWorld,

    world_unknownWorld,
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

    world_teleportedToWarp,

    world_availableWarps,

    world_warpToThisWarpDisallowed,

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

}
