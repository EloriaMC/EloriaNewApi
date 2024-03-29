package fr.eloria.api.plugin.bungee.common.matchmaking;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchQueue {

    @BsonProperty(value = "_id")
    private String name;

    private List<UUID> queuedPlayer;

    public MatchQueue(String name) {
        this.name = name;
        this.queuedPlayer = Lists.newLinkedList();
    }

    public ProxiedPlayer getPlayer(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(getQueuedPlayer().stream().filter(uuid::equals).findFirst().orElse(null));
    }

    public UUID getFirstPlayer() {
        return getQueuedPlayer().get(0);
    }

    public void addPlayer(UUID uuid) {
        getQueuedPlayer().add(uuid);
        System.out.println("[MatchMakingManager] Added " + ProxyServer.getInstance().getPlayer(uuid).getName() + " to " + getName() + " queue");
    }

    public void removePlayer(UUID uuid) {
        getQueuedPlayer().remove(uuid);
        System.out.println("[MatchMakingManager] Remove " + uuid.toString() + " from " + getName() + " queue");
    }

    public boolean contains(UUID uuid) {
        return getQueuedPlayer().contains(uuid);
    }

    public int getSize() {
        return getQueuedPlayer().size();
    }

    public int getPosition(UUID uuid) {
        return getQueuedPlayer().indexOf(uuid) + 1;
    }

    public boolean isEmpty() {
        return getQueuedPlayer().isEmpty();
    }

}
