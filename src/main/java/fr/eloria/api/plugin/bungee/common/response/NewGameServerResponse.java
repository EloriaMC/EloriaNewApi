package fr.eloria.api.plugin.bungee.common.response;

import be.alexandre01.dnplugin.api.request.RequestType;
import be.alexandre01.dnplugin.api.request.communication.ClientResponse;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.wrapper.BooleanWrapper;

public class NewGameServerResponse extends ClientResponse {

    public NewGameServerResponse(BungeePlugin plugin) {
        addRequestInterceptor(RequestType.BUNGEECORD_REGISTER_SERVER, (message, ctx) ->
                        BooleanWrapper.of(plugin.getLoader().getMatchMakingManager().getRequestedServer().contains(message.getString("PROCESSNAME").split("-")[0]))
                                .ifTrue(() -> plugin.getLoader().getMatchMakingManager().removeRequestServer(message.getString("PROCESSNAME").split("-")[0])));
    }

}
