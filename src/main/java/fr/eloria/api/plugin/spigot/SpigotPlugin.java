package fr.eloria.api.plugin.spigot;

import fr.eloria.api.Api;
import fr.eloria.api.plugin.spigot.common.Loader;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotPlugin extends JavaPlugin {

    private Api api;
    private Loader loader;

    @Override
    public void onEnable() {
        this.api = new Api(false);
        this.loader = new Loader(this);

        this.loader.load();
    }

    @Override
    public void onDisable() {
        getLoader().unload();
        getApi().unload();
    }

}


