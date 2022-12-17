package fr.eloria.api.plugin.spigot.listener;

import be.alexandre01.dnplugin.plugins.spigot.api.events.server.ServerAttachedEvent;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.SpigotUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Getter
@AllArgsConstructor
public class ServerListener implements Listener {

    private final SpigotPlugin plugin;

    @EventHandler
    public void onServerStart(ServerAttachedEvent event) {
        getPlugin().getLoader().getServerManager().loadServer(SpigotUtils.getServer());
    }

}
