package fr.eloria.api.plugin.spigot;

import fr.eloria.api.Api;
import fr.eloria.api.data.user.User;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotPlugin extends JavaPlugin {

    private Api api;

    private ECommandHandler commandHandler;

    @Override
    public void onEnable() {
        this.api = new Api();
        this.commandHandler = new ECommandHandler(this, "api");
        this.commandHandler.registerCommand(this);
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        User user = getApi().getUserManager().getUserFromRedis(sender.getUniqueId());

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', user.getRank().getPrefix() + " " + sender.getName() + "&7: &fSalut !"));
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

}


