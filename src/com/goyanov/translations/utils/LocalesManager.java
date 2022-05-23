package com.goyanov.translations.utils;

import com.goyanov.translations.main.MultiLanguageAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LocalesManager implements Listener
{
    protected enum Locale { EN, RU }

    private static Locale defaultLocale;

    private static final File usersLocalesFile;
    protected static final FileConfiguration playersLocalesConfig;

    private static final HashMap<Player, Locale> PLAYERS_LOCALES = new HashMap<>();

    protected static Locale getPlayerLocale(Player p)
    {
        return PLAYERS_LOCALES.getOrDefault(p, defaultLocale);
    }

    protected static void setPlayerLocale(Player p, Locale locale) { PLAYERS_LOCALES.put(p, locale); }

    protected static void savePlayersLocalesConfig()
    {
        try
        {
            playersLocalesConfig.save(usersLocalesFile);
        } catch (IOException e) { e.printStackTrace(); }
    }

    static
    {
        usersLocalesFile = new File(MultiLanguageAPI.inst().getDataFolder() + File.separator + "users-locales.yml");
        if (!usersLocalesFile.exists())
        {
            try
            {
                usersLocalesFile.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }
        playersLocalesConfig = YamlConfiguration.loadConfiguration(usersLocalesFile);

        try
        {
            defaultLocale = Locale.valueOf(MultiLanguageAPI.inst().getConfig().getString("default-locale").toUpperCase());
        } catch (Exception e)
        {
            MultiLanguageAPI.inst().getServer().getConsoleSender().sendMessage("§4Ошибка в настройке локализации по умолчанию (ключ - default-locale). Ставим на RU.");
            defaultLocale = Locale.RU;
        }
    }

    @EventHandler
    public void loadLocalesOnJoin(PlayerJoinEvent e)
    {
        String key = e.getPlayer().getUniqueId().toString();
        if (playersLocalesConfig.contains(key))
        {
            PLAYERS_LOCALES.put(e.getPlayer(), Locale.valueOf(playersLocalesConfig.getString(key)));
        }
        else
        {
            PLAYERS_LOCALES.put(e.getPlayer(), defaultLocale);
        }
    }

    @EventHandler
    public void unloadLocalesOnQuit(PlayerQuitEvent e)
    {
        PLAYERS_LOCALES.remove(e.getPlayer());
    }
}
