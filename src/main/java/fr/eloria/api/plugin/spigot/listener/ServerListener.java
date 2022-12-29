package fr.eloria.api.plugin.spigot.listener;

import be.alexandre01.dnplugin.plugins.spigot.api.events.server.ServerAttachedEvent;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.handler.AbstractListener;
import org.bukkit.event.EventHandler;

public class ServerListener extends AbstractListener<SpigotPlugin> {

    public ServerListener(SpigotPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerStart(ServerAttachedEvent event) {
        getPlugin().getLoader().getServerManager().loadServer(SpigotUtils.getServer());
    }

}
