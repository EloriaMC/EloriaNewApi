package fr.eloria.api.plugin.spigot.listener;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetAddress;

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


    /*
            Securité
     */

    @EventHandler (priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent event) {
        InetAddress address = event.getRealAddress();

        if (!address.getHostAddress().equals("127.0.0.1")) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("&cErreur de connexion vers le serveur... \n" +
                    "&cMerci de bien vouloir ré-essayer plus tard. \n \n");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = event.getPlayer();
            User user = getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId());

            getPlugin().getApi().getUserManager().sendUserToRedis(user);
            getPlugin().getApi().getUserManager().removeUser(user);
        });
    }

}
