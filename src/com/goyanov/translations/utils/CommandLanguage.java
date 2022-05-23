package com.goyanov.translations.utils;

import com.goyanov.translations.main.MultiLanguageAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static com.goyanov.translations.utils.LocalesManager.*;

public class CommandLanguage implements CommandExecutor
{
    private void showHelp(CommandSender sender)
    {
        sender.sendMessage("§8§l| §3/language <EN | RU>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§4Команда только для игроков!");
            return true;
        }

        if (args.length != 1)
        {
            showHelp(sender);
            return true;
        }

        Player p = (Player) sender;
        Locale newLocale;

        if (args[0].equalsIgnoreCase("ru"))
        {
            newLocale = Locale.RU;
            sender.sendMessage(MultiLanguageAPI.inst().getConfig().getString("language-changed-messages.ru").replace("&","§"));
        }
        else if (args[0].equalsIgnoreCase("en"))
        {
            newLocale = Locale.EN;
            sender.sendMessage(MultiLanguageAPI.inst().getConfig().getString("language-changed-messages.en").replace("&","§"));
        }
        else
        {
            showHelp(sender);
            return true;
        }
        setPlayerLocale(p, newLocale);
        LocalesManager.playersLocalesConfig.set(p.getUniqueId().toString(), newLocale.toString());
        LocalesManager.savePlayersLocalesConfig();
        return true;
    }
}
