package com.goyanov.translations.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import static com.goyanov.translations.utils.LocalesManager.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MLSchema
{
    private final JavaPlugin plugin;
    private FileConfiguration englishConfig;
    private FileConfiguration russianConfig;
    private final boolean replaceColorCodes;

    public MLSchema(JavaPlugin plugin)
    {
        this(plugin, true);
    }

    public MLSchema(JavaPlugin plugin, boolean replaceColorCodes)
    {
        this.plugin = plugin;
        this.reloadConfigs();
        this.replaceColorCodes = replaceColorCodes;
    }

    public void reloadConfigs()
    {
        if (getEnglishFileName() != null)
        {
            englishConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + getEnglishFileName()));
        }

        if (getRussianFileName() != null)
        {
            russianConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + getRussianFileName()));
        }
    }

    protected abstract String getEnglishFileName();

    protected abstract String getRussianFileName();

    private String getColoredMessage(String message)
    {
        message = message.replace("&", "§");
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find())
        {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color)+"");
            matcher = pattern.matcher(message);
        }
        return message;
    }

    public String getConfigMessage(Player p, String messageKey)
    {
        String message = null;
        if (getPlayerLocale(p) == Locale.EN && englishConfig != null) message = englishConfig.getString(messageKey);
        else if (getPlayerLocale(p) == Locale.RU && russianConfig != null) message = russianConfig.getString(messageKey);
        if (message != null && replaceColorCodes) message = getColoredMessage(message);
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
