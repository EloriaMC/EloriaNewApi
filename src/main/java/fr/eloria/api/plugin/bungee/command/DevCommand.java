package fr.eloria.api.plugin.bungee.command;

import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

@Getter
public class DevCommand extends Command {

    private final BungeePlugin plugin;

    public DevCommand(BungeePlugin plugin) {
        super("qlist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getPlugin().getLoader().getServerManager().getServerTypes()
                .forEach(serverType -> sender.sendMessage(new TextComponent(serverType.getName() + " - " + serverType.getMaxPlayers())));
    }

}
