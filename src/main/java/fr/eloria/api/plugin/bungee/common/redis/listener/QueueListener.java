package fr.eloria.api.plugin.bungee.common.redis.listener;

import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.packet.QueuePacket;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import fr.eloria.api.utils.wrapper.BooleanWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class QueueListener implements RedisListener {

    private final BungeePlugin plugin;

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public Consumer<String> onMessage() {
        return message -> {
            QueuePacket packet = GsonUtils.GSON.fromJson(message, QueuePacket.class);

            BooleanWrapper.of(packet.getAction().equals(QueuePacket.QueueAction.ADD))
                    .ifTrue(() -> getPlugin().getLoader().getMatchMakingManager().addPlayer(packet.getGame(), packet.getPlayer()))
                    .ifFalse(() -> getPlugin().getLoader().getMatchMakingManager().removePlayer(packet.getGame(), packet.getPlayer()));
        };
    }

}
