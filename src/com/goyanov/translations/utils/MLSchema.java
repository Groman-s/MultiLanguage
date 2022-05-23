package com.goyanov.translations.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import static com.goyanov.translations.utils.LocalesManager.*;

import java.io.File;

public abstract class MLSchema
{
    private FileConfiguration englishConfig;
    private FileConfiguration russianConfig;
    private final boolean replaceColorCodes;

    public MLSchema(JavaPlugin plugin)
    {
        this(plugin, true);
    }

    public MLSchema(JavaPlugin plugin, boolean replaceColorCodes)
    {
        if (getEnglishFileName() != null)
        {
            englishConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + getEnglishFileName()));
        }

        if (getRussianFileName() != null)
        {
            russianConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + getRussianFileName()));
        }

        this.replaceColorCodes = replaceColorCodes;
    }

    protected abstract String getEnglishFileName();

    protected abstract String getRussianFileName();

    public String getConfigMessage(Player p, String messageKey)
    {
        String message = null;
        if (getPlayerLocale(p) == Locale.EN && englishConfig != null) message = englishConfig.getString(messageKey);
        else if (getPlayerLocale(p) == Locale.RU && russianConfig != null) message = russianConfig.getString(messageKey);
        if (message != null && replaceColorCodes) message = message.replace("&", "§");
        return message;
    }

    public final void sendChatMessage(Player p, String messageKey)
    {
        String message = getConfigMessage(p, messageKey);
        if (message != null) p.sendMessage(message);
    }

    public final void sendActionBarMessage(Player p, String messageKey)
    {
        String message = getConfigMessage(p, messageKey);
        if (message != null) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }
}
