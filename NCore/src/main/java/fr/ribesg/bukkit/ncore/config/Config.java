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
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.TimeUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
        this.checkFor.add("NPermissions");
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
        this.setUpdateCheck(config.getBoolean("updateCheck", true));

        // updateCheckInterval. Default: 43200
        // Possible values: Positive or null intergers, in seconds
        this.setUpdateCheckInterval(config.getLong("updateCheckInterval", 43200));

        // checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
        // Possible values: any subset of the default value
        this.setCheckFor(config.getStringList("checkFor"));

        // apiKey. Default: empty
        this.setApiKey(config.getString("apiKey", ""));

        // proxyAddress
        final String proxyHost = config.getString("proxyHost", null);
        final int proxyPort = config.getInt("proxyPort", -1);
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1) {
            this.setProxyAddress(new InetSocketAddress(proxyHost, proxyPort));
        }

        // translatableUnits
        this.setSecondTimeUnit(config.getString("second", "second"));
        this.setSecondsTimeUnit(config.getString("seconds", "seconds"));
        this.setMinuteTimeUnit(config.getString("minute", "minute"));
        this.setMinutesTimeUnit(config.getString("minutes", "minutes"));
        this.setHourTimeUnit(config.getString("hour", "hour"));
        this.setHoursTimeUnit(config.getString("hours", "hours"));
        this.setDayTimeUnit(config.getString("day", "day"));
        this.setDaysTimeUnit(config.getString("days", "days"));
        this.setWeekTimeUnit(config.getString("week", "week"));
        this.setWeeksTimeUnit(config.getString("weeks", "weeks"));
        this.setMonthTimeUnit(config.getString("month", "month"));
        this.setMonthsTimeUnit(config.getString("months", "months"));
        this.setYearTimeUnit(config.getString("year", "year"));
        this.setYearsTimeUnit(config.getString("years", "years"));

        TimeUtil.setTranslatedUnit("second", this.second);
        TimeUtil.setTranslatedUnit("seconds", this.seconds);
        TimeUtil.setTranslatedUnit("minute", this.minute);
        TimeUtil.setTranslatedUnit("minutes", this.minutes);
        TimeUtil.setTranslatedUnit("hour", this.hour);
        TimeUtil.setTranslatedUnit("hours", this.hours);
        TimeUtil.setTranslatedUnit("day", this.day);
        TimeUtil.setTranslatedUnit("days", this.days);
        TimeUtil.setTranslatedUnit("week", this.week);
        TimeUtil.setTranslatedUnit("weeks", this.weeks);
        TimeUtil.setTranslatedUnit("month", this.month);
        TimeUtil.setTranslatedUnit("months", this.months);
        TimeUtil.setTranslatedUnit("year", this.year);
        TimeUtil.setTranslatedUnit("years", this.years);

        // debugEnabled. Default: empty
        this.setDebugEnabled(config.getStringList("debugEnabled"));
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
        content.append("updateCheck: " + this.updateCheck + "\n\n");

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
        content.append("updateCheckInterval: " + this.updateCheckInterval + "\n\n");

        // checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
        content.append("# Enable update check for each specific node. Default: all nodes\n");
        content.append("# Note: Will not consider unknown plugins nor unused nodes.\n");
        content.append("checkFor:\n");
        for (final String pluginName : this.checkFor) {
            content.append("- " + pluginName + '\n');
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
            content.append("proxyPort: " + this.proxyAddress.getPort() + '\n');
        }
        content.append('\n');

        // translatableUnits
        content.append("# Here you can translate what's written in every plugin when\n");
        content.append("# a duration is printed.\n");
        content.append("second: " + this.second + '\n');
        content.append("seconds: " + this.seconds + '\n');
        content.append("minute: " + this.minute + '\n');
        content.append("minutes: " + this.minutes + '\n');
        content.append("hour: " + this.hour + '\n');
        content.append("hours: " + this.hours + '\n');
        content.append("day: " + this.day + '\n');
        content.append("days: " + this.days + '\n');
        content.append("week: " + this.week + '\n');
        content.append("weeks: " + this.weeks + '\n');
        content.append("month: " + this.month + '\n');
        content.append("months: " + this.months + '\n');
        content.append("year: " + this.year + '\n');
        content.append("years: " + this.years + '\n');
        content.append('\n');

        // debugEnabled. Default: empty
        content.append("# Enables debug mode for each specific node. Default: empty\n");
        content.append("debugEnabled:\n");
        for (final String debugged : this.debugEnabled) {
            content.append("- " + debugged + '\n');
        }

        return content.toString();
    }

    public boolean isUpdateCheck() {
        return this.updateCheck;
    }

    public void setUpdateCheck(final boolean updateCheck) {
        this.updateCheck = updateCheck;
    }

    public long getUpdateCheckInterval() {
        return this.updateCheckInterval;
    }

    public void setUpdateCheckInterval(final long updateCheckInterval) {
        this.updateCheckInterval = updateCheckInterval;
    }

    public List<String> getCheckFor() {
        return this.checkFor;
    }

    public void setCheckFor(final List<String> checkFor) {
        this.checkFor = checkFor;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public InetSocketAddress getProxyAddress() {
        return this.proxyAddress;
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
        return this.second;
    }

    public void setSecondTimeUnit(final String second) {
        this.second = second;
    }

    public String getSecondsTimeUnit() {
        return this.seconds;
    }

    public void setSecondsTimeUnit(final String seconds) {
        this.seconds = seconds;
    }

    public String getMinuteTimeUnit() {
        return this.minute;
    }

    public void setMinuteTimeUnit(final String minute) {
        this.minute = minute;
    }

    public String getMinutesTimeUnit() {
        return this.minutes;
    }

    public void setMinutesTimeUnit(final String minutes) {
        this.minutes = minutes;
    }

    public String getHourTimeUnit() {
        return this.hour;
    }

    public void setHourTimeUnit(final String hour) {
        this.hour = hour;
    }

    public String getHoursTimeUnit() {
        return this.hours;
    }

    public void setHoursTimeUnit(final String hours) {
        this.hours = hours;
    }

    public String getDayTimeUnit() {
        return this.day;
    }

    public void setDayTimeUnit(final String day) {
        this.day = day;
    }

    public String getDaysTimeUnit() {
        return this.days;
    }

    public void setDaysTimeUnit(final String days) {
        this.days = days;
    }

    public String getWeekTimeUnit() {
        return this.week;
    }

    public void setWeekTimeUnit(final String week) {
        this.week = week;
    }

    public String getWeeksTimeUnit() {
        return this.weeks;
    }

    public void setWeeksTimeUnit(final String weeks) {
        this.weeks = weeks;
    }

    public String getMonthTimeUnit() {
        return this.month;
    }

    public void setMonthTimeUnit(final String month) {
        this.month = month;
    }

    public String getMonthsTimeUnit() {
        return this.months;
    }

    public void setMonthsTimeUnit(final String months) {
        this.months = months;
    }

    public String getYearTimeUnit() {
        return this.year;
    }

    public void setYearTimeUnit(final String year) {
        this.year = year;
    }

    public String getYearsTimeUnit() {
        return this.years;
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
