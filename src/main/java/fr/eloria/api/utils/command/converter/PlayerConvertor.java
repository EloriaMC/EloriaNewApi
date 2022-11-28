package fr.eloria.api.utils.command.converter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerConvertor implements IConvertor<Player> {

    @Override
    public Class<Player> getType() {
        return Player.class;
    }

    @Override
    public Player getFromString(CommandSender sender, String string) {
        return Bukkit.getServer().getPlayer(string) != null ? Bukkit.getServer().getPlayer(string) : null;
    }

    @Override
    public List<String> tabComplete(CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
