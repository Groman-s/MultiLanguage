package com.goyanov.translations.main;

import com.goyanov.translations.utils.CommanLanguageTabCompleter;
import com.goyanov.translations.utils.CommandLanguage;
import com.goyanov.translations.utils.LocalesManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MultiLanguageAPI extends JavaPlugin
{
    private static MultiLanguageAPI instance;
    public static MultiLanguageAPI inst() { return instance; }

    @Override
    public void onEnable()
    {
        instance = this;

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) saveDefaultConfig();
        reloadConfig(); // TODO сделать команду под релоад конфига

        for (Player p : Bukkit.getOnlinePlayers())
        {
            LocalesManager.loadPlayerLocaleFromConfig(p);
        }

        getServer().getPluginManager().registerEvents(new LocalesManager(), this);

        getCommand("language").setExecutor(new CommandLanguage());
        getCommand("language").setTabCompleter(new CommanLanguageTabCompleter());
    }
}
