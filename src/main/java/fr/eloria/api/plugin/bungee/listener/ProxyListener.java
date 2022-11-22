package fr.eloria.api.plugin.bungee.listener;

import fr.eloria.api.Api;
import fr.eloria.api.data.user.User;
import fr.eloria.api.data.user.data.UserSettings;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@Getter
@AllArgsConstructor
public class ProxyListener implements Listener {

    private final BungeePlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(getPlugin(), () -> {
            ProxiedPlayer player = event.getPlayer();
            User user = plugin.getApi().getUserManager().userExistInMongo(player.getUniqueId()) ? plugin.getApi().getUserManager().getUserFromMongo(event.getPlayer().getUniqueId()) : Api.getInstance().getUserManager().getNewUser(player.getUniqueId());

            plugin.getApi().getUserManager().sendUserToRedis(user);
            plugin.getApi().getUserManager().addUser(user);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(getPlugin(), () -> {
            ProxiedPlayer player = event.getPlayer();
            User user = plugin.getApi().getUserManager().getUserFromRedis(player.getUniqueId());

            plugin.getApi().getUserManager().removeUserFromRedis(user);
            plugin.getApi().getUserManager().sendUserToMongo(user);

            plugin.getApi().getUserManager().removeUser(user);
        });
    }

}
