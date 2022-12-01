package fr.eloria.api.plugin.bungee.command;

import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

@Getter
public class DevCommand extends Command {

    private final BungeePlugin plugin;

    public DevCommand(BungeePlugin plugin) {
        super("redis");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }

}
