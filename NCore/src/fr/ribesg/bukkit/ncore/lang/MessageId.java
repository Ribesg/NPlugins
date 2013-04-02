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

    // #################### //
    // ## NWorld related ## //
    // #################### //

    world_availableWorlds,
    world_teleportedTo,

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

    world_alreadyAllowed,
    world_allowedWarp,
    world_alreadyDisallowed,
    world_disallowedWarp,

    world_teleportingToSpawn,
    world_settingSpawnPoint,

    // ################### //
    // ## NTalk related ## //
    // ################### //

    talk_nobodyToRespond,

    talk_youNickNamed,
    talk_youDeNickNamed,
    talk_youWereNickNamed,
    talk_youWereDeNickNamed,

}
