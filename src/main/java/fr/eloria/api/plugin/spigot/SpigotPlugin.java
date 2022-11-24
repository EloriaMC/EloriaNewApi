package fr.eloria.api.plugin.spigot;

import fr.eloria.api.Api;
import fr.eloria.api.plugin.spigot.listener.PlayerListener;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotPlugin extends JavaPlugin {

    private Api api;

    private ECommandHandler commandHandler;

    @Override
    public void onEnable() {
        this.api = new Api(false);
        this.commandHandler = new ECommandHandler(this, "api");
        this.commandHandler.registerCommand(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        getApi().getRankManager().getRanksOrdainedByPower().forEach(rank -> sender.sendMessage("Grade " + rank.getName() + "#" + rank.getPower()));
        getApi().getUserManager().getUsers().get(sender.getUniqueId()).setRank(getApi().getRankManager().getRank("Admin"));
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

}


