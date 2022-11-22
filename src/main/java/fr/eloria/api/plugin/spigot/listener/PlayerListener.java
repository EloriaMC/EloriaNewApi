package fr.eloria.api.plugin.spigot.listener;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.MultiThreading;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
@AllArgsConstructor
public class PlayerListener implements Listener {

    private final SpigotPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MultiThreading.runAsync(() -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUserFromRedis(player.getUniqueId());

            getPlugin().getApi().getUserManager().addUser(user);
        });

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MultiThreading.runAsync(() -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId());

            getPlugin().getApi().getUserManager().sendUserToRedis(user);
            getPlugin().getApi().getUserManager().removeUser(user);
        });
    }

}
