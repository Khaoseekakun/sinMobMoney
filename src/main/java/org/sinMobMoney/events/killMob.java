package org.sinMobMoney.events;

import com.Zrips.CMI.Containers.CMIUser;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.Listener;
import org.sinMobMoney.SinMobMoney;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class killMob implements Listener {
    private final HashSet<UUID> spawnerMobs = new HashSet<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        Entity entity = event.getEntity();

        String entityName = event.getEntityType().name();
        ConfigurationSection mobSection = SinMobMoney.config.getConfigurationSection("mobs." + entityName);
        if (mobSection != null && mobSection.getBoolean("active") && !mobSection.getBoolean("onSpawner")) {
            spawnerMobs.add(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event){
        UUID entityId = event.getEntity().getUniqueId();
        spawnerMobs.remove(entityId);
    }

    @EventHandler
    public void onKillMob(EntityDeathEvent event){

        Player player = event.getEntity().getKiller();

        String entity_real_name = event.getEntity().getName();
        String entityName = event.getEntityType().name();

        ConfigurationSection mobSection = SinMobMoney.config.getConfigurationSection("mobs." + entityName);
        if (mobSection != null && mobSection.getBoolean("active")) {
            double min = mobSection.getDouble("min");
            double max = mobSection.getDouble("max");

            boolean onSpawner = mobSection.getBoolean("onSpawner", true);

            UUID entityId = event.getEntity().getUniqueId();
            if(!onSpawner){ //is false to check mob on spawn
                spawnerMobs.remove(entityId);
            }else{
                if(spawnerMobs.equals(entityId)) {
                    return;
                }
            }


            if (max < min) {
                max = min;
            }

            double reward = min + (new Random().nextDouble() * (max - min));

            String killMessage = SinMobMoney.config.getString("messages.kill_mob");
            String formattedReward = df.format(reward);

            assert player != null;
            assert killMessage != null;

            CMIUser cmiUser = CMIUser.getUser(player);
            if(cmiUser == null) return;
            cmiUser.deposit(reward);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', killMessage.replace("{amount}", formattedReward).replace("{mob}", entity_real_name)));
        }

    }
}
