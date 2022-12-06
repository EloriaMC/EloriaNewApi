package fr.eloria.api.plugin.bungee.command;

import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.plugin.bungee.common.matchmaking.MatchQueue;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
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
    public void execute(CommandSender commandSender, String[] args) {
        commandSender.sendMessage(new TextComponent(getPlugin().getLoader().getMatchMakingManager().getQueues().stream().map(MatchQueue::getName).collect(Collectors.joining(", "))));
     }

}
