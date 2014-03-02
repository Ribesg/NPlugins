/***************************************************************************
 * Project file:    NPlugins - NCore - Config.java                         *
 * Full Class name: fr.ribesg.bukkit.ncore.config.Config                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.config;
import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ribesg
 */
public class Config extends AbstractConfig<NCore> {

	private boolean           updateCheck;
	private long              updateCheckInterval;
	private List<String>      checkFor;
	private String            apiKey;
	private InetSocketAddress proxyAddress;

	private String second, seconds, minute, minutes, hour, hours, day, days, week, weeks, month, months, year, years;

	private List<String> debugEnabled;

	/**
	 * Constructor
	 *
	 * @param instance Linked plugin instance
	 */
	public Config(final NCore instance) {
		super(instance);
		this.updateCheck = true;
		this.updateCheckInterval = 43200;
		this.checkFor = new ArrayList<>();
		this.checkFor.add("NCore");
		this.checkFor.add("NCuboid");
		this.checkFor.add("NEnchantingEgg");
		this.checkFor.add("NGeneral");
		this.checkFor.add("NPlayer");
		this.checkFor.add("NTalk");
		this.checkFor.add("NTheEndAgain");
		this.checkFor.add("NWorld");
		this.apiKey = "";
		this.proxyAddress = null;

		this.second = "second";
		this.seconds = "seconds";
		this.minute = "minute";
		this.minutes = "minutes";
		this.hour = "hour";
		this.hours = "hours";
		this.day = "day";
		this.days = "days";
		this.week = "week";
		this.weeks = "weeks";
		this.month = "month";
		this.months = "months";
		this.year = "year";
		this.years = "years";

		this.debugEnabled = new ArrayList<>();
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// updateCheck. Default: true
		// Possible values: boolean
		setUpdateCheck(config.getBoolean("updateCheck", true));

		// updateCheckInterval. Default: 43200
		// Possible values: Positive or null intergers, in seconds
		setUpdateCheckInterval(config.getLong("updateCheckInterval", 43200));

		// checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
		// Possible values: any subset of the default value
		setCheckFor(config.getStringList("checkFor"));

		// apiKey. Default: empty
		setApiKey(config.getString("apiKey", ""));

		// proxyAddress
		final String proxyHost = config.getString("proxyHost", null);
		final int proxyPort = config.getInt("proxyPort", -1);
		if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1) {
			setProxyAddress(new InetSocketAddress(proxyHost, proxyPort));
		}

		// translatableUnits
		setSecondTimeUnit(config.getString("second", "second"));
		setSecondsTimeUnit(config.getString("seconds", "seconds"));
		setMinuteTimeUnit(config.getString("minute", "minute"));
		setMinutesTimeUnit(config.getString("minutes", "minutes"));
		setHourTimeUnit(config.getString("hour", "hour"));
		setHoursTimeUnit(config.getString("hours", "hours"));
		setDayTimeUnit(config.getString("day", "day"));
		setDaysTimeUnit(config.getString("days", "days"));
		setWeekTimeUnit(config.getString("week", "week"));
		setWeeksTimeUnit(config.getString("weeks", "weeks"));
		setMonthTimeUnit(config.getString("month", "month"));
		setMonthsTimeUnit(config.getString("months", "months"));
		setYearTimeUnit(config.getString("year", "year"));
		setYearsTimeUnit(config.getString("years", "years"));

		TimeUtils.setTranslatedUnit("second", this.second);
		TimeUtils.setTranslatedUnit("seconds", this.seconds);
		TimeUtils.setTranslatedUnit("minute", this.minute);
		TimeUtils.setTranslatedUnit("minutes", this.minutes);
		TimeUtils.setTranslatedUnit("hour", this.hour);
		TimeUtils.setTranslatedUnit("hours", this.hours);
		TimeUtils.setTranslatedUnit("day", this.day);
		TimeUtils.setTranslatedUnit("days", this.days);
		TimeUtils.setTranslatedUnit("week", this.week);
		TimeUtils.setTranslatedUnit("weeks", this.weeks);
		TimeUtils.setTranslatedUnit("month", this.month);
		TimeUtils.setTranslatedUnit("months", this.months);
		TimeUtils.setTranslatedUnit("year", this.year);
		TimeUtils.setTranslatedUnit("years", this.years);

		// debugEnabled. Default: empty
		setDebugEnabled(config.getStringList("debugEnabled"));
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NCore plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// updateCheck. Default: true
		content.append("# Allows you to disable the Updater system globally. Default : true\n");
		content.append("# If this is set to false, everything related to updater will not work:\n");
		content.append("# - Automatic check for updates\n");
		content.append("# - The whole /updater command\n");
		content.append("# - File download\n");
		content.append("updateCheck: " + isUpdateCheck() + "\n\n");

		// updateCheckInterval: Default: 43200
		content.append("# Interval between Update check, in seconds. Default : 43200\n");
		content.append("#\n");
		content.append("# Here are some example values:\n");
		content.append("#   Value   --   Description\n");
		content.append("#       1800: 30 minutes\n");
		content.append("#       3600: 1 hour\n");
		content.append("#       7200: 2 hours\n");
		content.append("#      10800: 3 hours\n");
		content.append("#      14400: 4 hours\n");
		content.append("#      21600: 6 hours\n");
		content.append("#      28800: 8 hours\n");
		content.append("#      43200: 12 hours\n");
		content.append("#      86400: 24 hours - 1 day\n");
		content.append("#     172800: 48 hours - 2 days\n");
		content.append("#\n");
		content.append("# Note: Set this to 0 to disable automatic update check completely.\n");
		content.append("# Note: This is not persistent through restarts.\n");
		content.append("# Note: Values below 15 minutes are useless because of an internal cache.\n");
		content.append("#\n");
		content.append("updateCheckInterval: " + getUpdateCheckInterval() + "\n\n");

		// checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
		content.append("# Enable update check for each specific node. Default: all nodes\n");
		content.append("# Note: Will not consider unknown plugins nor unused nodes.\n");
		content.append("checkFor:\n");
		for (final String pluginName : this.checkFor) {
			content.append("- " + pluginName + "\n");
		}
		content.append('\n');

		// apiKey. Default: empty
		content.append("# An API key is not required for the Updater to work,\n");
		content.append("# but you can define one here. Default: empty\n");
		content.append("#\n");
		content.append("# For now, the API key has no use, but there may be an\n");
		content.append("# anonymous request limitation in the future. If the Updater\n");
		content.append("# starts to fail a lot for no reason, populate this.\n");
		content.append("#\n");
		content.append("# Note: Generate a key from https://dev.bukkit.org/home/servermods-apikey/\n");
		content.append("#       You must be logged in.\n");
		content.append("apiKey: " + this.apiKey + "\n\n");

		// proxyAddress. Default: empty
		content.append("# Proxy informations for the Updater. Default: empty\n");
		if (this.proxyAddress == null) {
			content.append("proxyHost: \"\"\n");
			content.append("proxyPort: \"\"\n");
		} else {
			content.append("proxyHost: \"" + this.proxyAddress.getHostName() + "\"\n");
			content.append("proxyPort: " + this.proxyAddress.getPort() + "\n");
		}
		content.append('\n');

		// translatableUnits
		content.append("# Here you can translate what's written in every plugin when\n");
		content.append("# a duration is printed.\n");
		content.append("second: " + getSecondTimeUnit() + '\n');
		content.append("seconds: " + getSecondsTimeUnit() + '\n');
		content.append("minute: " + getMinuteTimeUnit() + '\n');
		content.append("minutes: " + getMinutesTimeUnit() + '\n');
		content.append("hour: " + getHourTimeUnit() + '\n');
		content.append("hours: " + getHoursTimeUnit() + '\n');
		content.append("day: " + getDayTimeUnit() + '\n');
		content.append("days: " + getDaysTimeUnit() + '\n');
		content.append("week: " + getWeekTimeUnit() + '\n');
		content.append("weeks: " + getWeeksTimeUnit() + '\n');
		content.append("month: " + getMonthTimeUnit() + '\n');
		content.append("months: " + getMonthsTimeUnit() + '\n');
		content.append("year: " + getYearTimeUnit() + '\n');
		content.append("years: " + getYearsTimeUnit() + '\n');
		content.append('\n');

		// debugEnabled. Default: empty
		content.append("# Enables debug mode for each specific node. Default: empty\n");
		content.append("debugEnabled:\n");
		for (final String debugged : this.debugEnabled) {
			content.append("- " + debugged + "\n");
		}

		return content.toString();
	}

	public boolean isUpdateCheck() {
		return updateCheck;
	}

	public void setUpdateCheck(final boolean updateCheck) {
		this.updateCheck = updateCheck;
	}

	public long getUpdateCheckInterval() {
		return updateCheckInterval;
	}

	public void setUpdateCheckInterval(final long updateCheckInterval) {
		this.updateCheckInterval = updateCheckInterval;
	}

	public List<String> getCheckFor() {
		return checkFor;
	}

	public void setCheckFor(final List<String> checkFor) {
		this.checkFor = checkFor;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public InetSocketAddress getProxyAddress() {
		return proxyAddress;
	}

	public void setProxyAddress(final InetSocketAddress proxyAddress) {
		this.proxyAddress = proxyAddress;
	}

	public Proxy getProxy() {
		if (this.proxyAddress != null) {
			return new Proxy(Proxy.Type.HTTP, this.proxyAddress);
		} else {
			return null;
		}
	}

	public String getSecondTimeUnit() {
		return second;
	}

	public void setSecondTimeUnit(final String second) {
		this.second = second;
	}

	public String getSecondsTimeUnit() {
		return seconds;
	}

	public void setSecondsTimeUnit(final String seconds) {
		this.seconds = seconds;
	}

	public String getMinuteTimeUnit() {
		return minute;
	}

	public void setMinuteTimeUnit(final String minute) {
		this.minute = minute;
	}

	public String getMinutesTimeUnit() {
		return minutes;
	}

	public void setMinutesTimeUnit(final String minutes) {
		this.minutes = minutes;
	}

	public String getHourTimeUnit() {
		return hour;
	}

	public void setHourTimeUnit(final String hour) {
		this.hour = hour;
	}

	public String getHoursTimeUnit() {
		return hours;
	}

	public void setHoursTimeUnit(final String hours) {
		this.hours = hours;
	}

	public String getDayTimeUnit() {
		return day;
	}

	public void setDayTimeUnit(final String day) {
		this.day = day;
	}

	public String getDaysTimeUnit() {
		return days;
	}

	public void setDaysTimeUnit(final String days) {
		this.days = days;
	}

	public String getWeekTimeUnit() {
		return week;
	}

	public void setWeekTimeUnit(final String week) {
		this.week = week;
	}

	public String getWeeksTimeUnit() {
		return weeks;
	}

	public void setWeeksTimeUnit(final String weeks) {
		this.weeks = weeks;
	}

	public String getMonthTimeUnit() {
		return month;
	}

	public void setMonthTimeUnit(final String month) {
		this.month = month;
	}

	public String getMonthsTimeUnit() {
		return months;
	}

	public void setMonthsTimeUnit(final String months) {
		this.months = months;
	}

	public String getYearTimeUnit() {
		return year;
	}

	public void setYearTimeUnit(final String year) {
		this.year = year;
	}

	public String getYearsTimeUnit() {
		return years;
	}

	public void setYearsTimeUnit(final String years) {
		this.years = years;
	}

	public List<String> getDebugEnabled() {
		return this.debugEnabled;
	}

	public boolean isDebugEnabled(final String nodeName) {
		return this.debugEnabled.contains(nodeName);
	}

	public void setDebugEnabled(final List<String> debugEnabled) {
		this.debugEnabled = debugEnabled;
	}
}
