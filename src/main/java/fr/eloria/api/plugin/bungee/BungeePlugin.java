package fr.eloria.api.plugin.bungee;

import fr.eloria.api.Api;
import fr.eloria.api.plugin.bungee.listener.ProxyListener;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeePlugin extends Plugin {

    private Api api;

    @Override
    public void onEnable() {
        this.api = new Api();
        getProxy().getPluginManager().registerListener(this, new ProxyListener(this));
    }

    @Override
    public void onDisable() {
       getApi().unload();
    }


}
