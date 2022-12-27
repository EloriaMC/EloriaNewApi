package fr.eloria.api.plugin.spigot.command;

import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.command.annotation.ECommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Getter
@AllArgsConstructor
public class ServerInfoCommand {

    private final SpigotPlugin plugin;

    @ECommand(name = "serverinfo", aliases = {"info"}, permission = "eloria.command.serverinfo")
    public void execute(CommandSender sender) {
        SpigotUtils.sendMessages(sender,
                "&7» &eInformations",
                "",
                "&7» &eServeur: &b" + getPlugin().getLoader().getServerManager().getServer().getName(),
                "&7» &eJoueurs: &b" + getPlugin().getLoader().getServerManager().getServer().getOnlinePlayers() + "&7/&b" + getPlugin().getLoader().getServerManager().getServer().getType().getMaxPlayers(),
                "&7» &eMap: &b" + Bukkit.getWorlds().get(0).getName(),
                "&7» &eType: &b" + getPlugin().getLoader().getServerManager().getServer().getType().getName(),
                "&7» &eStatus: &b" + getPlugin().getLoader().getServerManager().getServer().getStatus().getStatus(),
                "&7» &eVersion: &b" + Bukkit.getServer().getBukkitVersion().split("-")[0],
                "&7» &eTPS: " + getTps()
                );
    }

    private String getTps() {
        double[] tps = MinecraftServer.getServer().recentTps;
        String[] avgTps = new String[tps.length];

        for (int i = 0; i < tps.length; i++)
            avgTps[i] = tpsFormat(tps[i]);

        return String.join(", ", avgTps);
    }

    private String tpsFormat(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : (tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED)) + (tps > 20.0 ? "*" : "") + Math.min((double) Math.round(tps * 100.0) / 100.0, 20.0);
    }

}
