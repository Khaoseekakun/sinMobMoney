package org.sinMobMoney;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.sinMobMoney.commands.main;
import org.sinMobMoney.events.killMob;

import java.util.Objects;

public final class SinMobMoney extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new killMob(), this);
        Objects.requireNonNull(getCommand("reload")).setExecutor(new main());
    }

    @Override
    public void onDisable() {
    }
}
