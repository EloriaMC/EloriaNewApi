package fr.eloria.api;

import be.alexandre01.dreamnetwork.api.addons.Addon;
import be.alexandre01.dreamnetwork.api.addons.DreamExtension;

public class ApiExtension extends DreamExtension {

    public ApiExtension(Addon addon) {
        super(addon);
    }

    @Override
    public void onLoad() {
        registerPluginToProxies(this);
        registerPluginToServers(this);
    }

}
