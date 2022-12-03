package fr.eloria.api.plugin.bungee.common.redis.listener;

import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.packet.QueuePacket;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

@Getter
public class QueueListener extends JedisPubSub implements RedisListener {

    private final BungeePlugin plugin;

    public QueueListener(BungeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public JedisPubSub getPubSub() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                QueuePacket packet = GsonUtils.GSON.fromJson(message, QueuePacket.class);

                if (getPlugin().getLoader().getMatchMakingManager().getQueue(packet.getGame()) != null) {
                    if (packet.getAction().equals(QueuePacket.QueueAction.ADD))
                        getPlugin().getLoader().getMatchMakingManager().getQueue(packet.getGame()).addPlayer(packet.getPlayer());

                    if (packet.getAction().equals(QueuePacket.QueueAction.REMOVE))
                        getPlugin().getLoader().getMatchMakingManager().getQueue(packet.getGame()).removePlayer(packet.getPlayer());
                }
            }
        };
    }

    @Override
    public String getName() {
        return "queue";
    }

}
