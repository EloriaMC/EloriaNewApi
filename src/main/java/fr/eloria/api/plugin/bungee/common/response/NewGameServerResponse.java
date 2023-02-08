package fr.eloria.api.plugin.bungee.common.response;

import be.alexandre01.dnplugin.api.request.RequestType;
import be.alexandre01.dnplugin.api.request.communication.ClientResponse;
import be.alexandre01.dnplugin.utils.messages.Message;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.wrapper.BooleanWrapper;

public class NewGameServerResponse extends ClientResponse {

    public NewGameServerResponse(BungeePlugin plugin) {
        addRequestInterceptor(RequestType.BUNGEECORD_REGISTER_SERVER, (message, ctx) ->
                        BooleanWrapper.of(plugin.getLoader().getMatchMakingManager().getRequestedServer().contains(getServerName(message)))
                                .ifTrue(() -> plugin.getLoader().getMatchMakingManager().removeRequestServer(getServerName(message))));
    }

    private String getServerName(Message message) {
        return message.getString("PROCESSNAME").split("-")[0];
    }

}
