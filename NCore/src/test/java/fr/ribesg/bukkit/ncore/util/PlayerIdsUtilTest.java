/***************************************************************************
 * Project file:    NPlugins - NCore - PlayerIdsUtilTest.java              *
 * Full Class name: fr.ribesg.bukkit.ncore.util.PlayerIdsUtilTest          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class PlayerIdsUtilTest {

	@Test
	public void testShortToUuid() {
		final UUID id = UUID.randomUUID();
		final String shortUuid = PlayerIdsUtil.uuidToShortUuid(id);
		final UUID id2 = PlayerIdsUtil.shortUuidToUuid(shortUuid);
		Assert.assertEquals(id, id2);
	}
}
