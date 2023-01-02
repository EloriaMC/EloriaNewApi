package fr.eloria.api.plugin.bungee.command;

import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.BungeeUtils;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.Collectors;

@Getter
public class DevCommand extends Command {

    private final BungeePlugin plugin;

    public DevCommand(BungeePlugin plugin) {
        super("slist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new TextComponent("Servers :"));
        getPlugin().getLoader().getServerManager().getServers()
                .forEach(gameServer -> sender.sendMessage(new TextComponent(gameServer.getName() + " - (" + gameServer.getOnlinePlayers() + "/" + gameServer.getType().getMaxPlayers() + ")")));

        sender.sendMessage(new TextComponent("Match Queue :"));
        getPlugin().getLoader().getMatchMakingManager().getQueues()
                        .forEach(queue -> sender.sendMessage(new TextComponent(queue.getName() + " - (" + queue.getSize() + ") - " + queue.getQueuedPlayer().stream().map(getPlugin().getProxy()::getPlayer).map(ProxiedPlayer::getName).collect(Collectors.joining(", ")))));

        sender.sendMessage(new TextComponent("Requested Servers :"));
        getPlugin().getLoader().getMatchMakingManager().getRequestedServer()
                .forEach(s -> sender.sendMessage(new TextComponent(s)));
    }

}
