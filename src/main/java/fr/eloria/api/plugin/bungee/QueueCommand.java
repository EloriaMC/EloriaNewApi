package fr.eloria.api.plugin.bungee;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

@Getter
public class QueueCommand extends Command {

    private final BungeePlugin plugin;

    public QueueCommand(BungeePlugin plugin) {
        super("queue");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }

}
