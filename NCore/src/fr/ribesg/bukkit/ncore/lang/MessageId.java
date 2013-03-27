package fr.ribesg.bukkit.ncore.lang;

/**
 * @author Ribesg
 */
public enum MessageId {

    // ######################### //
    // ## Not plugin-specific ## //
    // ######################### //

    noPermissionForCommand,

    cmdOnlyAvailableForPlayers,

    cmdReloadConfig,
    cmdReloadMessages,

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

    theEndAgain_incorrectValueInConfiguration,

}
