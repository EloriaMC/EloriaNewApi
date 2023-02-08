package fr.eloria.api.utils;

import be.alexandre01.dnplugin.api.objects.server.DNServer;
import be.alexandre01.dnplugin.plugins.bungeecord.api.DNBungeeAPI;
import fr.eloria.api.data.server.GameServer;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class BungeeUtils {

    public DNServer getServer(String serverName) {
        return getServers(serverName.split("-")[0]).stream().filter(server -> serverName.equals(server.getFullName())).findFirst().orElse(null);
    }

    public List<DNServer> getServers(String serviceName) {
        return new ArrayList<>(DNBungeeAPI.getInstance().getServices().get(serviceName).getServers().values());
    }

    public List<DNServer> getServers(String serviceName, int maxPlayers) {
        return DNBungeeAPI.getInstance().getServices().get(serviceName).getServers().values().stream().filter(server -> maxPlayers != server.getPlayers().size()).collect(Collectors.toList());
    }

    public List<DNServer> getServersOrdainedByPlayers(String serviceName, int maxPlayer) {
        return getServers(serviceName, maxPlayer).stream().sorted(onlinePlayersComparator().reversed()).collect(Collectors.toList());
    }

    public List<DNServer> getServersOrdainedByPlayers(String serviceName) {
        return getServers(serviceName).stream().sorted(onlinePlayersComparator().reversed()).collect(Collectors.toList());
    }

    public DNServer getServerWithMorePlayers(String serviceName) {
        return getServersOrdainedByPlayers(serviceName).stream().findFirst().orElse(null);
    }

    public DNServer getServerWithMorePlayers(String serviceName, int maxPlayers) {
        return getServersOrdainedByPlayers(serviceName, maxPlayers).stream().findFirst().orElse(null);
    }

    public DNServer getServerWithLessPlayers(String serviceName) {
        return getServersOrdainedByPlayers(serviceName).stream().min(onlinePlayersComparator()).orElse(null);
    }

    public DNServer getServerWithLessPlayers(String serviceName, int maxPlayers) {
        return getServersOrdainedByPlayers(serviceName, maxPlayers).stream().min(onlinePlayersComparator()).orElse(null);
    }

    public void connectPlayerTo(UUID uuid, GameServer gameServer) {
        ProxyServer.getInstance().getPlayer(uuid).connect(ProxyServer.getInstance().getServerInfo(gameServer.getName()));
    }

    public void connectPlayerTo(ProxiedPlayer player, GameServer gameServer) {
        connectPlayerTo(player.getUniqueId(), gameServer);
    }

    public Comparator<DNServer> onlinePlayersComparator() {
        return Comparator.comparingInt(s -> s.getPlayers().size());
    }

}
