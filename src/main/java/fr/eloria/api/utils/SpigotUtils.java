package fr.eloria.api.utils;

import be.alexandre01.dnplugin.api.objects.RemoteService;
import be.alexandre01.dnplugin.api.objects.server.DNServer;
import be.alexandre01.dnplugin.plugins.spigot.api.DNSpigotAPI;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class SpigotUtils {

    public DNServer getServer() {
        return DNSpigotAPI.getInstance().getServices().get(DNSpigotAPI.getInstance().getServerName()).getServers().values().stream().filter(server -> server.getFullName().equals(getServerName())).findFirst().orElse(null);
    }

    public String getServerName() {
        return DNSpigotAPI.getInstance().getServerName() + "-" + DNSpigotAPI.getInstance().getID();
    }

    public int getOnlinePlayerCount(RemoteService category) {
        AtomicInteger finalCount = new AtomicInteger(0);
        DNSpigotAPI.getInstance().getServices().get(category.getName()).getServers().forEach((integer, dnServer) -> finalCount.addAndGet(dnServer.getPlayers().size()));
        return finalCount.get();
    }

    public int getOnlinePlayerCount() {
        AtomicInteger finalCount = new AtomicInteger(0);
        DNSpigotAPI.getInstance().getServices().forEach((name, remoteService) -> remoteService.getServers().values().forEach(server -> finalCount.addAndGet(server.getPlayers().size())));
        return finalCount.get();
    }

    public int getOnlineServerCount() {
        AtomicInteger finalCount = new AtomicInteger(0);
        DNSpigotAPI.getInstance().getServices().forEach((name, remoteService) -> finalCount.addAndGet(remoteService.getServers().size()));
        return finalCount.get();
    }

    public void sendMessages(CommandSender commandSender, String... messages) {
        Arrays.asList(messages)
                .forEach(message -> commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void sendMessages(Player player, List<String> messages) {
        messages.forEach(message -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void sendMessages(Player player, String... messages) {
        Arrays.asList(messages)
                .forEach(message -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public String coloredText(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public DecimalFormat decimalFormat() {
        return new DecimalFormat("#.##");
    }

}
