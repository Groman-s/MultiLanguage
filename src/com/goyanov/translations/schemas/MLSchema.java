package com.goyanov.translations.schemas;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLSchema
{
    private final JavaPlugin plugin;
    private final String fileName;
    private final boolean replaceColorCodes;
    private static final HashMap<String, FileConfiguration> localesConfigs = new HashMap<>();

    public MLSchema(JavaPlugin plugin, String fileName)
    {
        this(plugin, fileName, true);
    }

    public MLSchema(JavaPlugin plugin, String fileName, boolean replaceColorCodes)
    {
        this.plugin = plugin;
        this.fileName = fileName.replace(".yml", "");
        this.replaceColorCodes = replaceColorCodes;
        this.reloadConfigs();
    }

    public void reloadConfigs()
    {
        for (File file : plugin.getDataFolder().listFiles())
        {
            String name = file.getName();
            if (name.matches(fileName + "_..\\.yml"))
            {
                localesConfigs.put(name.substring(name.indexOf("_") + 1, name.indexOf(".")), YamlConfiguration.loadConfiguration(file));
            }
        }
        if (localesConfigs.size() == 0)
        {
            localesConfigs.put("default", YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + fileName + ".yml")));
        }
    }

    public static String getColoredMessage(String message)
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

    public FileConfiguration getLocalizedConfig(Player p)
    {
        String locale = p.getLocale().split("_")[0];
        FileConfiguration config;

        if
        (
            (config = localesConfigs.get(locale)) != null       ||
            (config = localesConfigs.get("en")) != null         ||
            (config = localesConfigs.get("ru")) != null         ||
            (config = localesConfigs.get("default")) != null
        )
        {
            return config;
        }

        return null;
    }

    public String getConfigMessage(Player p, String messageKey)
    {
        String message = null;
        String locale = p.getLocale().split("_")[0];
        FileConfiguration config;

        if
        (
            (config = localesConfigs.get(locale)) != null       ||
            (config = localesConfigs.get("en")) != null         ||
            (config = localesConfigs.get("ru")) != null         ||
            (config = localesConfigs.get("default")) != null
        )
        {
            message = config.getString(messageKey);
        }

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

    public final void sendTitleMessage(Player p, String messageKey, int duration)
    {
        String message = getConfigMessage(p, messageKey);
        if (message != null) p.sendTitle(" ", message, 5, duration, 5);
    }
}
