package fr.eloria.api.plugin.spigot;

import fr.eloria.api.Api;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import lombok.Getter;
import org.bukkit.command.CommandSender;
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
    }

    @ECommand(name = "dev")
    public void execute(CommandSender sender) {
        getApi().getRankManager().getRanksOrdainedByPower()
                .forEach(rank -> sender.sendMessage("Grade " + rank.getName() + "#" + rank.getPower()));
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

}


