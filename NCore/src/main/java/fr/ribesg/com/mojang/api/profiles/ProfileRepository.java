/***************************************************************************
 * Project file:    NPlugins - NCore - ProfileRepository.java              *
 * Full Class name: fr.ribesg.com.mojang.api.profiles.ProfileRepository    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.profiles;

public interface ProfileRepository {

	public Profile[] findProfilesByNames(String... names);
}
