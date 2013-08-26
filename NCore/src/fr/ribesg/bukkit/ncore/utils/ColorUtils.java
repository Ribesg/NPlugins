package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.ChatColor;

public class ColorUtils {

	public static final char   ALTERNATE_COLOR_CHAR        = '&';
	public static final String ALTERNATE_COLOR_CHAR_STRING = Character.toString(ALTERNATE_COLOR_CHAR);

	public static String colorize(String toColorize) {
		return ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CHAR, toColorize);
	}

}
