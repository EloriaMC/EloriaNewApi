package fr.eloria.api.utils;

import be.alexandre01.dnplugin.api.objects.server.DNServer;
import be.alexandre01.dnplugin.plugins.bungeecord.api.DNBungeeAPI;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BungeeUtils {

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

    public Comparator<DNServer> onlinePlayersComparator() {
        return Comparator.comparingInt(s -> s.getPlayers().size());
    }

}
