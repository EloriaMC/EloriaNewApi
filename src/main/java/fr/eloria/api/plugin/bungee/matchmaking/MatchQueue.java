package fr.eloria.api.plugin.bungee.matchmaking;

import be.alexandre01.dnplugin.api.objects.server.DNServer;
import com.google.common.collect.Lists;
import fr.eloria.api.utils.BungeeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchQueue {

    @BsonProperty(value = "_id")
    private String name;

    private int maxPlayers;

    @BsonIgnore
    private Queue<UUID> queuedPlayer;

    public MatchQueue(String name, int maxPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.queuedPlayer = Lists.newLinkedList();
    }

    public void connect(UUID uuid) {
        DNServer bestServer = BungeeUtils.getServerWithMorePlayers(getName(), getMaxPlayers());

       if (bestServer != null) {
           getPlayer(uuid).connect(ProxyServer.getInstance().getServerInfo(bestServer.getFullName()));
       }
    }

    public ProxiedPlayer getPlayer(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(getQueuedPlayer().stream().filter(uuid::equals).findFirst().orElse(null));
    }

    public void addPlayer(UUID uuid) {
        getQueuedPlayer().add(uuid);
    }

    public void removePlayer(UUID uuid) {
        getQueuedPlayer().remove(uuid);
    }

    public int getPosition(UUID uuid) {
        return new LinkedList<>(getQueuedPlayer()).indexOf(uuid);
    }

}
