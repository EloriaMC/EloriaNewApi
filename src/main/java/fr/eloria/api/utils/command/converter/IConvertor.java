package fr.eloria.api.utils.command.converter;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface IConvertor<T> {

    Class<T> getType();

    T getFromString(CommandSender sender, String string);

    List<String> tabComplete(CommandSender sender);

}
