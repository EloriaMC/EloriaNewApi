package fr.eloria.api.plugin.spigot.command;

import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.command.annotation.ECommand;
import fr.eloria.api.utils.command.annotation.EParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RankCommand {

    private final SpigotPlugin plugin;

    @ECommand(name = "rank", permission = "eloria.command.rank")
    public void executeRankCommand(CommandSender sender) {
        SpigotUtils.sendMessages(sender, "&eListe des grades: " + getPlugin().getApi().getRankManager().getRanks().values().stream().map(Rank::getPrefix).collect(Collectors.joining("&7, ")));
    }

    @ECommand(name = "setrank", permission = "eloria.command.rank")
    public void executeSetRankCommand(CommandSender sender, @EParameter(name = "player") Player player, @EParameter(name = "rankName") Rank rank) {
        if (player != null) {
            sender.sendMessage("Vous avez mis le grade " + rank.getName() + " a " + player.getName());

            getPlugin().getApi().getUserManager().getUsers().get(player.getUniqueId()).setRank(rank);

            player.sendMessage("Vous avez reçus le grade " + rank.getName() + " !");
        } else sender.sendMessage("Erreur");
    }

}
