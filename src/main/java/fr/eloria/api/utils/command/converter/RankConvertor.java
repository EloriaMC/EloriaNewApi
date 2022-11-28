package fr.eloria.api.utils.command.converter;

import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RankConvertor implements IConvertor<Rank> {

    private final SpigotPlugin plugin;

    @Override
    public Class<Rank> getType() {
        return Rank.class;
    }

    @Override
    public Rank getFromString(CommandSender sender, String string) {
        return getPlugin().getApi().getRankManager().getRank(string);
    }

    @Override
    public List<String> tabComplete(CommandSender sender) {
        return getPlugin().getApi().getRankManager().getRanks().values().stream().map(Rank::getName).collect(Collectors.toList());
    }

}
