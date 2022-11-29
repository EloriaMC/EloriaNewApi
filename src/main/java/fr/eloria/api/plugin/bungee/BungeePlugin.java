package fr.eloria.api.plugin.bungee;

import fr.eloria.api.Api;
import fr.eloria.api.plugin.bungee.listener.ProxyListener;
import fr.eloria.api.plugin.bungee.matchmaking.MatchMakingManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeePlugin extends Plugin {

    private Api api;

    private MatchMakingManager matchMakingManager;

    @Override
    public void onEnable() {
        this.api = new Api(true);
        this.matchMakingManager = new MatchMakingManager(this);
        this.matchMakingManager.loadQueues();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new QueueCommand(this));
        getProxy().getPluginManager().registerListener(this, new ProxyListener(this));
    }

    @Override
    public void onDisable() {
        getMatchMakingManager().saveQueues();
        getApi().unload();
    }


}
