package fr.eloria.api.plugin.spigot.common.server;

import be.alexandre01.dnplugin.api.objects.server.DNServer;
import be.alexandre01.dnplugin.utils.Mods;
import com.google.common.collect.Lists;
import fr.eloria.api.data.server.GameServer;
import fr.eloria.api.data.server.ServerStatus;
import fr.eloria.api.data.server.ServerType;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.ServerProperties;
import fr.eloria.api.utils.wrapper.BooleanWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.List;

@Getter
@Setter
public class ServerManager {

    private final SpigotPlugin plugin;
    private GameServer server;

    public ServerManager(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    public String getRedisKey() {
        return "servers:" + getServer().getType().getName() + ":" + getServer().getName();
    }

    public void loadServer(DNServer dnServer) {
        ServerProperties.setServerProperty(ServerProperties.ServerProperty.MAX_PLAYERS, getServerType(dnServer.getRemoteService().getName()).getMaxPlayers());
        setServer(new GameServer(dnServer.getFullName(), 0, ServerStatus.OPEN, getServerType(dnServer.getRemoteService().getName())));
        sendServerToRedis();
    }

    public void unloadServer(DNServer dnServer) {
        BooleanWrapper.of(dnServer.getRemoteService().getMods().equals(Mods.DYNAMIC))
                .ifTrue(this::removeServerFromRedis)
                .ifFalse(() -> setStatus(ServerStatus.CLOSING));
    }

    public void updatePlayers() {
        getServer().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        sendServerToRedis();
    }

    public void setStatus(ServerStatus status) {
        getServer().setStatus(status);
        sendServerToRedis();
    }

    public void sendServerToRedis() {
        getPlugin().getApi().getRedisManager().set(getRedisKey(), getServer());
    }

    public void removeServerFromRedis() {
        getPlugin().getApi().getRedisManager().del(getRedisKey());
    }

    public ServerType getServerType(String typeName) {
        return getServerTypes().stream().filter(serverType -> serverType.getName().equals(typeName)).findFirst().orElse(new ServerType(typeName, 999));
    }

    public List<ServerType> getServerTypes() {
        List<ServerType> types = Lists.newLinkedList();
        getPlugin().getApi().getRedisManager().keys("serverType:*").forEach(key -> types.add(getPlugin().getApi().getRedisManager().get(key, ServerType.class)));
        return types;
    }

}
