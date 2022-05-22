package com.goyanov.translations.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommanLanguageTabCompleter implements TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            List<String> list = new ArrayList<>();
            String arg1 = args[0].toLowerCase();
            if ("ru".startsWith(arg1)) list.add("ru");
            if ("en".startsWith(arg1)) list.add("en");
            return list;
        }
        return new ArrayList<>();
    }
}
