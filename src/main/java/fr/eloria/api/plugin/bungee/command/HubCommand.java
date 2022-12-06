package fr.eloria.api.plugin.bungee.command;

import be.alexandre01.dnplugin.api.objects.server.DNServer;
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
public class HubCommand extends Command {

    private final BungeePlugin plugin;

    public HubCommand(BungeePlugin plugin) {
        super("hub", "", "lobby");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (args.length == 0)
                if (!player.getServer().getInfo().getName().startsWith("lobby"))
                    player.connect(ProxyServer.getInstance().getServerInfo(BungeeUtils.getServerWithLessPlayers("lobby").getFullName()));

            if (args.length == 1)
                if (args[0].startsWith("lobby")) {
                    DNServer requestLobby = BungeeUtils.getServer(args[0]);

                    if (requestLobby != null)
                        if (!requestLobby.getFullName().equals(player.getServer().getInfo().getName()))
                            player.connect(ProxyServer.getInstance().getServerInfo(requestLobby.getFullName()));
                        else
                            player.sendMessage(new TextComponent("Vous êtes déjà sur ce lobby"));

                    else
                        player.sendMessage(new TextComponent("Le lobby " + args[0] + " n'existe pas !"));

                } else
                    player.sendMessage(new TextComponent("Liste des lobbys: " + BungeeUtils.getServers("lobby").stream().map(DNServer::getFullName).collect(Collectors.joining(", "))));
        }
    }

}
