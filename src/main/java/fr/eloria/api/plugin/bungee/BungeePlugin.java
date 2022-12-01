package fr.eloria.api.plugin.bungee;

import fr.eloria.api.Api;
import fr.eloria.api.plugin.bungee.common.Loader;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeePlugin extends Plugin {

    private Api api;
    private Loader loader;

    @Override
    public void onEnable() {
        this.api = new Api(true);
        this.loader = new Loader(this);
    }

    @Override
    public void onDisable() {
        getLoader().unload();
        getApi().unload();
    }


}
