package fr.eloria.api.plugin.bungee.command;

import com.google.common.collect.Lists;
import fr.eloria.api.data.rank.Rank;
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
        Rank rank = new Rank("Joueur", "&7Joueur", 0, true, Lists.newLinkedList());
        getPlugin().getApi().getRankManager().getRanks().put("Joueur", rank);
        getPlugin().getApi().getRankManager().sendRankToRedis(rank);
    }

}
