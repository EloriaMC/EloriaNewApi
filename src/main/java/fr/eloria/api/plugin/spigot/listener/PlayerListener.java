package fr.eloria.api.plugin.spigot.listener;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.gui.GuiManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@AllArgsConstructor
public class PlayerListener implements Listener {

    private final SpigotPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUserFromRedis(player.getUniqueId());

            getPlugin().getApi().getUserManager().addUser(user);
            getPlugin().getLoader().getServerManager().updatePlayers();

            if (getPlugin().getLoader().getServerManager().getServer().getType().getName().equals("lobby"))
                player.teleport(new Location(Bukkit.getWorld("hub"), 1, 101, 2));
        });
    }

    /*
            Test
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId());

        event.setFormat(ChatColor.translateAlternateColorCodes('&', user.getRank().getPrefix() + " " + player.getName() + "&7: &f" + event.getMessage()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId());

            getPlugin().getApi().getUserManager().removeUser(user);
            getPlugin().getLoader().getServerManager().updatePlayers();
        });
    }

}
