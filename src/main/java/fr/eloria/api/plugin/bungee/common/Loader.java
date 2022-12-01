package fr.eloria.api.plugin.bungee.common;

import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.packet.TestPacket;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.plugin.bungee.command.DevCommand;
import fr.eloria.api.plugin.bungee.common.matchmaking.MatchMakingManager;
import fr.eloria.api.plugin.bungee.common.redis.TestPacketListener;
import fr.eloria.api.plugin.bungee.listener.ProxyListener;
import fr.eloria.api.utils.AbstractHandler;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

import java.util.Arrays;

@Getter
public class Loader extends AbstractHandler {

    private final BungeePlugin plugin;

    private final MatchMakingManager matchMakingManager;

    public Loader(BungeePlugin plugin) {
        this.plugin = plugin;
        this.matchMakingManager = new MatchMakingManager(plugin);

        this.load();
    }

    @Override
    public void load() {
        getMatchMakingManager().loadQueues();

        getPlugin().getApi().getRedisManager().getRedisMessenger().register("test", TypeToken.get(TestPacket.class), true);
        getPlugin().getApi().getRedisManager().getRedisMessenger().getChannel("test", TestPacket.class).addListener(new TestPacketListener(getPlugin()));

        registerCommands(new DevCommand(getPlugin()));
        registerListeners(new ProxyListener(getPlugin()));
    }

    private void registerListeners(Listener... listeners) {
        Arrays.asList(listeners)
                .forEach(listener -> getPlugin().getProxy().getPluginManager().registerListener(getPlugin(), listener));
    }

    private void registerCommands(Command... commands) {
        Arrays.asList(commands)
                .forEach(command -> getPlugin().getProxy().getPluginManager().registerCommand(getPlugin(), command));
    }

    @Override
    public void unload() {
        getMatchMakingManager().saveQueues();
    }

}
