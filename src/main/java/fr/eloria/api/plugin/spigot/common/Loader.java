package fr.eloria.api.plugin.spigot.common;

import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.packet.TestPacket;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.plugin.spigot.command.RankCommand;
import fr.eloria.api.plugin.spigot.command.ServerInfoCommand;
import fr.eloria.api.plugin.spigot.listener.PlayerListener;
import fr.eloria.api.utils.AbstractHandler;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import fr.eloria.api.utils.command.converter.PlayerConvertor;
import fr.eloria.api.utils.command.converter.RankConvertor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

@Getter
public class Loader extends AbstractHandler {

    private final SpigotPlugin plugin;
    private final ECommandHandler commandHandler;

    public Loader(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.commandHandler = new ECommandHandler(plugin, "api");

        this.load();
    }

    @Override
    public void load() {
        getPlugin().getApi().getRedisManager().getRedisMessenger().register("test", TypeToken.get(TestPacket.class), false);

        getCommandHandler().registerConverters(new RankConvertor(getPlugin()), new PlayerConvertor());
        getCommandHandler().registerCommands(this, new ServerInfoCommand(getPlugin()), new RankCommand(getPlugin()));

        registerListeners(new PlayerListener(getPlugin()));
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        getPlugin().getApi().getRankManager().getRanksOrdainedByPower().forEach(rank -> sender.sendMessage("Grade " + rank.getName() + "#" + rank.getPower()));
        getPlugin().getApi().getUserManager().getUsers().get(sender.getUniqueId()).setRank(getPlugin().getApi().getRankManager().getRank("Admin"));
    }

    @ECommand(name = "redisTest")
    public void executeRedisTest(CommandSender sender) {
        getPlugin().getApi().getRedisManager().getRedisMessenger().getChannel("test", TestPacket.class).sendMessage(new TestPacket("hello redis !"));
    }

    public void registerListeners(Listener... listeners) {
        Arrays.asList(listeners)
                .forEach(this::registerListener);
    }

    public void registerListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
    }

    @Override
    public void unload() {

    }

}
