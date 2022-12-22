package fr.eloria.api.plugin.bungee.common.redis.listener;

import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.packet.QueuePacket;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;

import java.util.function.BiConsumer;

@Getter
public class QueueListener implements RedisListener {

    private final BungeePlugin plugin;

    public QueueListener(BungeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public BiConsumer<String, String> onMessage() {
        return (channel, message) -> {
            QueuePacket packet = GsonUtils.GSON.fromJson(message, QueuePacket.class);

            if (packet.getAction().equals(QueuePacket.QueueAction.ADD))
                getPlugin().getLoader().getMatchMakingManager().addPlayer(packet.getGame(), packet.getPlayer());
            else
                getPlugin().getLoader().getMatchMakingManager().removePlayer(packet.getGame(), packet.getPlayer());
        };
    }

}
