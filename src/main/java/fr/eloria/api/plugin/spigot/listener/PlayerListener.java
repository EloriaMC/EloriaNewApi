package fr.eloria.api.plugin.spigot.listener;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.handler.AbstractListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener extends AbstractListener<SpigotPlugin> {

    public PlayerListener(SpigotPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUserFromRedis(player.getUniqueId());

            getPlugin().getApi().getUserManager().addUser(user);
            getPlugin().getLoader().getServerManager().updatePlayers();

            if (getPlugin().getLoader().getServerManager().getServer().getType().getName().equals("lobby"))
                player.teleport(new Location(Bukkit.getWorld("hub"), 248, 118, -281));

            getPlugin().getLoader().getExampleBoard().onJoin(player);
        });
    }

    /*
            Test
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = getPlugin().getApi().getUserManager().getUser(player.getUniqueId());

        event.setFormat(SpigotUtils.coloredText(user.getRank().getPrefix() + " " + player.getName() + "&7: &f" + event.getMessage()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId());

            getPlugin().getLoader().getExampleBoard().onLeave(player);
            getPlugin().getApi().getUserManager().removeUser(user);
            getPlugin().getLoader().getServerManager().updatePlayers();
        });
    }

}
