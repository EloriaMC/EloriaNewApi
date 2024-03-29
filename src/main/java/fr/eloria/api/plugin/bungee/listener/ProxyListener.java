package fr.eloria.api.plugin.bungee.listener;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@Getter
@AllArgsConstructor
public class ProxyListener implements Listener {

    private final BungeePlugin plugin;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(getPlugin(), () -> {
            ProxiedPlayer player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().userExistInMongo(player.getUniqueId()) ? getPlugin().getApi().getUserManager().getUserFromMongo(event.getPlayer().getUniqueId()) : getPlugin().getApi().getUserManager().getNewUser(player.getUniqueId());

            getPlugin().getApi().getUserManager().sendUserToRedis(user);
            getPlugin().getApi().getUserManager().addUser(user);
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(getPlugin(), () -> {
            ProxiedPlayer player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUserFromRedis(player.getUniqueId());

            getPlugin().getApi().getUserManager().sendUserToMongo(user);

            getPlugin().getLoader().getMatchMakingManager().onLogout(player.getUniqueId());
            getPlugin().getApi().getUserManager().removeUser(user);
            getPlugin().getApi().getUserManager().removeUserFromRedis(user);
        });
    }

}
