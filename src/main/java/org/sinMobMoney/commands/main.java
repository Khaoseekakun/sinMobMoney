package org.sinMobMoney.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sinMobMoney.SinMobMoney;
import org.sinMobMoney.untils.Color;

import java.io.File;

public class main implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {


        if(commandSender instanceof Player player){
            if(player.hasPermission("sinMobMoney.reload")){
                reloadConfig();
                String reload_message = SinMobMoney.config.getString("messages.reload");
                player.sendMessage(Color.ChangeColor(reload_message));
            }else{
                String you_not_have_permission = SinMobMoney.config.getString("messages.no_permission");
                player.sendMessage(Color.ChangeColor(you_not_have_permission));
            }
        }else{
            reloadConfig();
            String reload_message = SinMobMoney.config.getString("messages.reload");
            commandSender.sendMessage(Color.ChangeColor(reload_message));
        }

        return false;

    }


    public static void reloadConfig(){
        File configFile = new File("plugins/sinMobMoney/", "config.yml");
        SinMobMoney.config = YamlConfiguration.loadConfiguration(configFile);
    }

}
