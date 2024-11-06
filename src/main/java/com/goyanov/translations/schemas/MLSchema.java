package com.goyanov.translations.schemas;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLSchema
{
    private final JavaPlugin plugin;
    private final String fileName;
    private final HashMap<String, FileConfiguration> localesConfigs = new HashMap<>();

    public MLSchema(JavaPlugin plugin, String fileName)
    {
        this.plugin = plugin;
        this.fileName = fileName.replaceAll("(_..)?(\\.yml)?", "");
        this.reloadConfigs();
    }

    public Collection<FileConfiguration> getAllConfigs()
    {
        return localesConfigs.values();
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
        if (localesConfigs.isEmpty())
        {
            localesConfigs.put("default", YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + fileName + ".yml")));
        }
    }

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

    public String getConfigString(Player p, String configKey)
    {
        String message = getLocalizedConfig(p).getString(configKey);
        if (message != null) message = getColoredMessage(message);
        return message;
    }

    public List<String> getConfigStringList(Player p, String configKey)
    {
        List<String> messages = getLocalizedConfig(p).getStringList(configKey);
        messages.replaceAll(this::getColoredMessage);
        return messages;
    }
}
