package fr.eloria.api.plugin.spigot;

import fr.eloria.api.Api;
import fr.eloria.api.utils.command.ECommandHandler;
import lombok.Getter;
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

    @Override
    public void onDisable() {
        getApi().unload();
    }

}


