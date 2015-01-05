/***************************************************************************
 * Project file:    NPlugins - NPlayer - Config.java                       *
 * Full Class name: fr.ribesg.bukkit.nplayer.Config                        *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends AbstractConfig<NPlayer> {

    private int authenticationMode;
    private int maximumLoginAttempts;
    private int tooManyAttemptsPunishment;
    private int tooManyAttemptsPunishmentDuration;

    public Config(final NPlayer instance) {
        super(instance);
        this.setAuthenticationMode(1);
        this.setMaximumLoginAttempts(3);
        this.setTooManyAttemptsPunishment(1);
        this.setTooManyAttemptsPunishmentDuration(300);
    }

    @Override
    protected void handleValues(final YamlConfiguration config) {

        // authenticationMode. Default: 1.
        // Possible values: 0, 1, 2
        this.setAuthenticationMode(config.getInt("authenticationMode", 1));
        if (!this.match(this.authenticationMode, 0, 2)) {
            this.wrongValue("config.yml", "authenticationMode", this.authenticationMode, 1);
            this.setAuthenticationMode(1);
        }

        // maximumLoginAttempts. Default: 3.
        // Possible values: positive integers
        this.setMaximumLoginAttempts(config.getInt("maximumLoginAttempts", 3));
        if (!this.match(this.maximumLoginAttempts, 1, Integer.MAX_VALUE)) {
            this.wrongValue("config.yml", "maximumLoginAttempts", this.maximumLoginAttempts, 3);
            this.setMaximumLoginAttempts(3);
        }

        // tooManyAttemptsPunishment. Default: 1.
        // Possible values: 0, 1, 2
        this.setTooManyAttemptsPunishment(config.getInt("tooManyAttemptsPunishment", 1));
        if (!this.match(this.tooManyAttemptsPunishment, 0, 6)) {
            this.wrongValue("config.yml", "tooManyAttemptsPunishment", this.tooManyAttemptsPunishment, 1);
            this.setTooManyAttemptsPunishment(1);
        }

        // tooManyAttemptsPunishmentDuration. Default: 300.
        // Possible values: positive integers
        this.setTooManyAttemptsPunishmentDuration(config.getInt("tooManyAttemptsPunishmentDuration", 300));
        if (!this.match(this.tooManyAttemptsPunishmentDuration, 1, Integer.MAX_VALUE)) {
            this.wrongValue("config.yml", "tooManyAttemptsPunishmentDuration", this.tooManyAttemptsPunishmentDuration, 300);
            this.setTooManyAttemptsPunishmentDuration(300);
        }
    }

    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        final FrameBuilder frame;

        // Header
        frame = new FrameBuilder();
        frame.addLine("Config file for NPlayer plugin", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line + '\n');
        }

        // Enable authentication
        content.append("# Defines if authentication is enabled. Possibles values: 0 or 1.\n");
        content.append("# Default: 1\n");
        content.append("#\n");
        content.append("#   Value | Action\n");
        content.append("#       0 : Enabled\n");
        content.append("#       1 : Disabled\n");
        content.append("#       2 : Optional\n");
        content.append("#\n");
        content.append("authenticationMode: " + this.authenticationMode + "\n\n");

        // Maximum Login attempts
        content.append("# Maximum login attempts before punishment. Possible values: Positive integers\n");
        content.append("# Default : 3\n");
        content.append("maximumLoginAttempts: " + this.maximumLoginAttempts + "\n\n");

        // Too Many Attempts Punishment
        content.append("# How do we punish people after too many attempts? Possible values: 0, 1, 2\n");
        content.append("# Default : 1\n");
        content.append("#\n");
        content.append("#   Value | Action\n");
        content.append("#       0 : Kick\n");
        content.append("#       1 : Tempban\n");
        content.append("#       2 : Ban\n");
        content.append("#\n");
        content.append("tooManyAttemptsPunishment: " + this.tooManyAttemptsPunishment + "\n\n");

        // Too Many Attempts Punishment Duration
        content.append("# The duration of the punishment, if applicable. Possible values: Positive integers\n");
        content.append("# Default : 300\n");
        content.append("#\n");
        content.append("# Here are some example values:\n");
        content.append("#      Value | Description\n");
        content.append("#         30 : 30 seconds\n");
        content.append("#         60 : 1 minute\n");
        content.append("#        300 : 5 minutes\n");
        content.append("#        600 : 10 minutes\n");
        content.append("#       1800 : 30 minutes\n");
        content.append("#       1800 : 30 minutes\n");
        content.append("#       1800 : 30 minutes\n");
        content.append("#       3600 : 1 hour\n");
        content.append("#       7200 : 2 hours\n");
        content.append("#      10800 : 3 hours\n");
        content.append("#      14400 : 4 hours\n");
        content.append("#      21600 : 6 hours\n");
        content.append("#      28800 : 8 hours\n");
        content.append("#      43200 : 12 hours\n");
        content.append("#      86400 : 24 hours - 1 day\n");
        content.append("#     172800 : 48 hours - 2 days\n");
        content.append("#     604800 : 7 days\n");
        content.append("#\n");
        content.append("# You can use *any* strictly positive value you want, just be sure to convert it to seconds.\n");
        content.append("# Note: only applies to TempBan punishment for now.\n");
        content.append("#\n");
        content.append("tooManyAttemptsPunishmentDuration: " + this.tooManyAttemptsPunishmentDuration + "\n\n");

        return content.toString();
    }

    public int getAuthenticationMode() {
        return this.authenticationMode;
    }

    public void setAuthenticationMode(final int authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public int getMaximumLoginAttempts() {
        return this.maximumLoginAttempts;
    }

    public void setMaximumLoginAttempts(final int maximumLoginAttempts) {
        this.maximumLoginAttempts = maximumLoginAttempts;
    }

    public int getTooManyAttemptsPunishment() {
        return this.tooManyAttemptsPunishment;
    }

    public void setTooManyAttemptsPunishment(final int tooManyAttemptsPunishment) {
        this.tooManyAttemptsPunishment = tooManyAttemptsPunishment;
    }

    public int getTooManyAttemptsPunishmentDuration() {
        return this.tooManyAttemptsPunishmentDuration;
    }

    public void setTooManyAttemptsPunishmentDuration(final int tooManyAttemptsPunishmentDuration) {
        this.tooManyAttemptsPunishmentDuration = tooManyAttemptsPunishmentDuration;
    }
}
