package fr.eloria.api.plugin.spigot.common;

import fr.eloria.api.data.database.redis.packet.QueuePacket;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.plugin.spigot.command.RankCommand;
import fr.eloria.api.plugin.spigot.command.ServerInfoCommand;
import fr.eloria.api.plugin.spigot.common.redis.SpigotMessenger;
import fr.eloria.api.plugin.spigot.common.server.ServerManager;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import fr.eloria.api.utils.command.converter.PlayerConvertor;
import fr.eloria.api.utils.command.converter.RankConvertor;
import fr.eloria.api.utils.handler.AbstractHandler;
import fr.eloria.api.utils.handler.AbstractListener;
import fr.eloria.api.utils.item.CustomItems;
import fr.eloria.api.utils.item.ItemBuilder;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Getter
public class Loader extends AbstractHandler {

    private final SpigotPlugin plugin;

    private final SpigotMessenger redisMessenger;
    private final ServerManager serverManager;
    private final ECommandHandler commandHandler;

    private ExampleBoard exampleBoard;

    public Loader(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.redisMessenger = new SpigotMessenger(plugin);
        this.serverManager = new ServerManager(plugin);
        this.commandHandler = new ECommandHandler(plugin);
        this.exampleBoard = new ExampleBoard(plugin);
    }

    @Override
    public void load() {
        getRedisMessenger().load();
        getCommandHandler().registerConverters(new RankConvertor(getPlugin()), new PlayerConvertor());
        getCommandHandler().registerCommands(this, new ServerInfoCommand(getPlugin()), new RankCommand(getPlugin()));
        registerListeners("fr.eloria.api.plugin.spigot.listener");
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        ItemStack guts = new ItemBuilder(Material.GOLD_SWORD).setName("&6&lGUTS SWORD").setLeftClick(event -> event.getPlayer().sendMessage("RIGHT CLICK on GOLDEN SWORD")).toItemStack();

        sender.getInventory().addItem(guts);
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

    @ECommand(name = "testRedisPacket")
    public void executeUpdate(Player sender) {
        getRedisMessenger().sendMessage("test", "Hello world!");
    }

    public void registerListeners(Listener... listeners) {
        Arrays.asList(listeners)
                .forEach(this::registerListener);
    }

    public void registerListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
    }

    public void registerListeners(String... packageNames) {
        Arrays.asList(packageNames)
                .forEach(this::registerListeners);
    }

    public void registerListeners(String packageName) {
        new Reflections(packageName).getSubTypesOf(AbstractListener.class).forEach(clazz -> {
            try {
                registerListener(clazz.getDeclaredConstructor(getPlugin().getClass()).newInstance(getPlugin()));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unload() {
        getServerManager().unloadServer(SpigotUtils.getServer());
        getRedisMessenger().unload();
        CustomItems.getCustomItems().clear();
    }

}
