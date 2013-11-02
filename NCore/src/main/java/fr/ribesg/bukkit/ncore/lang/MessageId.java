package fr.ribesg.bukkit.ncore.lang;

/** @author Ribesg */
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

	incorrectValueInConfiguration,

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

	cuboid_cmdFlagAttUnknownFlagAtt,
	cuboid_cmdFlagAttNoPermission,
	cuboid_cmdFlagAttValue,
	cuboid_cmdFlagAttSet,

	cuboid_cmdAdminAdded,
	cuboid_cmdAdminAlreadyAdmin,
	cuboid_cmdAdminRemoved,
	cuboid_cmdAdminNotAdmin,

	cuboid_cmdUserAdded,
	cuboid_cmdUserAlreadyUser,
	cuboid_cmdUserRemoved,
	cuboid_cmdUserNotUser,

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

	player_standardKickMessage,

	player_loginAttemptsKickMessage,
	player_loginAttemptsBroadcastedKickMessage,
	player_loginAttemptsTempBanMessage,
	player_loginAttemptsBroadcastedTempBanMessage,
	player_loginAttemptsPermBanMessage,
	player_loginAttemptsBroadcastedPermBanMessage,

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

	general_tp_youToTarget,
	general_tp_somebodyToTarget,
	general_tp_youSomebodyToTarget,
	general_tp_somebodyToHim,
	general_tp_youSomebodyToYou,
	general_tp_noTarget,
	general_tp_youToLocation,
	general_tp_somebodyToLocation,
	general_tp_youSomebodyToLocation,
	general_tp_youNoKnownBack,
	general_tp_youBackWorldUnloaded,
	general_tp_youTeleportedBack,
	general_tp_somebodyNoKnownBack,
	general_tp_somebodyBackWorldUnloaded,
	general_tp_somebodyTeleportedYouBack,
	general_tp_youTeleportedSomebodyBack,

	general_timeSet,
	general_weatherSet,

	general_signcolors_permissionDenied,

	general_protectionsign_accessDenied,
	general_protectionsign_breakDenied,

	general_nicknameFilter_invalid,

}
