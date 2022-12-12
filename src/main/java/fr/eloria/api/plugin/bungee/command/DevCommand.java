package fr.eloria.api.plugin.bungee.command;

import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.Collectors;

@Getter
public class DevCommand extends Command {

    private final BungeePlugin plugin;

    public DevCommand(BungeePlugin plugin) {
        super("qlist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getPlugin().getLoader().getMatchMakingManager().getQueues()
                .forEach(queue -> sender.sendMessage(new TextComponent(queue.getName() + " : (" + queue.getSize() + ") - " + queue.getQueuedPlayer().stream().map(getPlugin().getProxy()::getPlayer).map(ProxiedPlayer::getName).collect(Collectors.joining(", ")))));
     }

}
