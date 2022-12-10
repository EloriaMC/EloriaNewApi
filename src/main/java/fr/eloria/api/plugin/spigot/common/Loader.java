package fr.eloria.api.plugin.spigot.common;

import fr.eloria.api.data.database.redis.packet.QueuePacket;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.plugin.spigot.command.RankCommand;
import fr.eloria.api.plugin.spigot.command.ServerInfoCommand;
import fr.eloria.api.plugin.spigot.common.redis.RedisMessenger;
import fr.eloria.api.plugin.spigot.listener.PlayerListener;
import fr.eloria.api.utils.AbstractHandler;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import fr.eloria.api.utils.command.converter.PlayerConvertor;
import fr.eloria.api.utils.command.converter.RankConvertor;
import fr.eloria.api.utils.gui.GuiManager;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

@Getter
public class Loader extends AbstractHandler {

    private final SpigotPlugin plugin;

    private final RedisMessenger redisMessenger;
    private final ECommandHandler commandHandler;

    private final GuiManager guiManager;

    public Loader(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.redisMessenger = new RedisMessenger(plugin);
        this.commandHandler = new ECommandHandler(plugin, "api");
        this.guiManager = new GuiManager(plugin);

        this.load();
    }

    @Override
    public void load() {
        getRedisMessenger().load();
        getCommandHandler().registerConverters(new RankConvertor(getPlugin()), new PlayerConvertor());
        getCommandHandler().registerCommands(this, new ServerInfoCommand(getPlugin()), new RankCommand(getPlugin()));

        registerListeners(new PlayerListener(getPlugin()));
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        getPlugin().getApi().getRankManager().getRanksOrdainedByPower().forEach(rank -> sender.sendMessage("Grade " + rank.getName() + "#" + rank.getPower()));
        getPlugin().getApi().getUserManager().getUsers().get(sender.getUniqueId()).setRank(getPlugin().getApi().getRankManager().getRank("Admin"));
    }

    @ECommand(name = "queueAdd")
    public void executeAddQueue(Player sender) {
        sender.sendMessage(GsonUtils.GSON.toJson(new QueuePacket("skywars", sender.getUniqueId(), QueuePacket.QueueAction.ADD)));
        getRedisMessenger().sendMessage("queue", new QueuePacket("skywars", sender.getUniqueId(), QueuePacket.QueueAction.ADD));
    }

    @ECommand(name = "queueRemove")
    public void executeRemoveQueue(Player sender) {
        sender.sendMessage(GsonUtils.GSON.toJson(new QueuePacket("skywars", sender.getUniqueId(), QueuePacket.QueueAction.REMOVE)));
        getRedisMessenger().sendMessage("queue", new QueuePacket("skywars", sender.getUniqueId(), QueuePacket.QueueAction.REMOVE));
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
        getRedisMessenger().unload();
    }

}
